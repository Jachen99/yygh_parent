package space.jachen.yygh.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import space.jachen.yygh.common.handler.YyghException;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.common.result.ResultCodeEnum;
import space.jachen.yygh.common.utils.HttpRequestHelper;
import space.jachen.yygh.enums.OrderStatusEnum;
import space.jachen.yygh.hosp.HospFeignClient;
import space.jachen.yygh.model.order.OrderInfo;
import space.jachen.yygh.model.user.Patient;
import space.jachen.yygh.order.mapper.OrderInfoMapper;
import space.jachen.yygh.order.service.OrderInfoService;
import space.jachen.yygh.rabbitmq.config.MqConst;
import space.jachen.yygh.rabbitmq.service.RabbitService;
import space.jachen.yygh.user.PatientFeignClient;
import space.jachen.yygh.vo.hosp.ScheduleOrderVo;
import space.jachen.yygh.vo.msm.MsmVo;
import space.jachen.yygh.vo.order.OrderMqVo;
import space.jachen.yygh.vo.order.OrderQueryVo;

import java.util.*;

/**
 * 订单表 服务实现类
 *
 * @author jachen
 * @since 2023-02-10
 */
@Service
@Slf4j
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    @Autowired
    private PatientFeignClient patientFeignClient;
    @Autowired
    private HospFeignClient hospFeignClient;
    @Autowired
    private RabbitService rabbitService;

    @Override
    public Boolean cancelOrder(Long orderId) {
        OrderInfo orderInfo = baseMapper.selectById(orderId);
        // 截止日期
        Date quitTime = orderInfo.getQuitTime();
        DateTime dateTime = new DateTime(quitTime);
        if (dateTime.isBeforeNow()){
            throw new YyghException(ResultCodeEnum.FAIL.getCode(),"已经超过取消预约的截止日期，取消预约失败~");
        }
        // 查询是否已经支付
        if (orderInfo.getOrderStatus() == 1){
            // 申请微信退款


            // 向退款表插入数据
        }
        // 医院唯一表示
        String hosRecordId = orderInfo.getHosRecordId();
        // 排班id
        String scheduleId = orderInfo.getScheduleId();
        // 封装传给医院的信息
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("hosRecordId", hosRecordId);
        }};
        JSONObject jsonObject = HttpRequestHelper.sendRequest(map, "http://localhost:9998/order/updateCancelStatus");
        // 获取医院响应回来的信息
        if (jsonObject.getInteger("code")!=200){
            throw new YyghException(ResultCodeEnum.FAIL.getCode(),"医院模拟系统服务响应异常，取消预约失败~");
        }
        JSONObject data = jsonObject.getJSONObject("data");
        Integer reservedNumber = data.getInteger("reservedNumber");
        Integer availableNumber = data.getInteger("availableNumber");
        // 更新订单状态为已取消
        orderInfo.setOrderStatus(OrderStatusEnum.CANCLE.getStatus());
        baseMapper.updateById(orderInfo);
        // 向rabbitMQ发消息 更新预约数和可预约数
        MsmVo msmVo = MsmVo.builder().phone(orderInfo.getPatientPhone()).build();
        OrderMqVo orderMqVo = OrderMqVo.builder()
                .scheduleId(scheduleId)
                .reservedNumber(reservedNumber)
                .availableNumber(availableNumber)
                .msmVo(msmVo).build();
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_ORDER,MqConst.ROUTING_ORDER,orderMqVo);
        return true;
    }

    /**
     * 获取订单详情
     *
     * @param orderId 订单id
     * @return OrderInfo
     */
    @Override
    public OrderInfo getOrderDetailById(Long orderId) {
        return baseMapper.selectById(orderId);
    }

    /**
     * 获取分页后的订单数据
     * wrapper条件为 ：
     * 就诊人id patientId 和 订单状态orderStatus 进行查询即可
     *
     * @param orderInfoPage  Page订单对象
     * @param orderQueryVo  OrderQueryVo
     * @return Page<OrderInfo>
     */
    @Override
    public Page<OrderInfo> getPageList(Page<OrderInfo> orderInfoPage, OrderQueryVo orderQueryVo) {

        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        // 添加查询条件 用户id、就诊人id和订单状态
        wrapper = StringUtils.isEmpty(orderQueryVo.getUserId()) ?
                wrapper : wrapper.eq(OrderInfo::getUserId,orderQueryVo.getUserId());
        wrapper = StringUtils.isEmpty(orderQueryVo.getPatientId()) ?
                wrapper : wrapper.eq(OrderInfo::getPatientId,orderQueryVo.getPatientId());
        wrapper = StringUtils.isEmpty(orderQueryVo.getOrderStatus()) ?
                wrapper : wrapper.eq(OrderInfo::getOrderStatus,orderQueryVo.getOrderStatus());
        Page<OrderInfo> page = baseMapper.selectPage(orderInfoPage, wrapper);
        List<OrderInfo> records = page.getRecords();
        records.forEach(this::packageOrderStatus);
        return page;
    }

    /**
     * 封装返回order状态的方法
     *
     * @param item orderInfo
     */
    private void packageOrderStatus(OrderInfo item) {
        Integer orderStatus = item.getOrderStatus();
        // 更新订单状态
        item.getParam().put("orderStatusString",OrderStatusEnum.getStatusNameByStatus(orderStatus));
    }

    /**
     * 生成订单信息
     *
     * @param scheduleId  排班id
     * @param patientId  就诊人id
     * @return  返回订单id
     */
    @Override
    public Long saveOrder(String scheduleId, Long patientId) {

        // openfeign获取排班信息和就诊人信息
        ScheduleOrderVo scheduleOrderVo = hospFeignClient.getScheduleOrderVo(scheduleId);
        log.info("打印获取到的scheduleOrderVo:\n" + scheduleOrderVo);
        JsonData<Map<String, Patient>> patientMap = patientFeignClient.getDetailById(patientId);
        Map<String, Patient> patientMapData = patientMap.getData();
        // 直接获取参数的方法
        Patient patient = patientMapData.get("patient");
        log.info("patient:\n" + patient);
        // 遍历获取参数的方法
        Patient patient2 = new Patient();
        patientMapData.forEach((k,v)->{
            BeanUtils.copyProperties(v,patient2);
            log.info("打印获取到的patientMapData:\n" + (k+v));
        });
        // 封装传给医院的数据
        HashMap<String, Object> resultMap = new HashMap<String, Object>(){{
            put("hoscode",scheduleOrderVo.getHoscode());
            put("depcode",scheduleOrderVo.getDepcode());
            put("hosScheduleId",scheduleOrderVo.getHosScheduleId());
            put("reserveDate",scheduleOrderVo.getReserveDate());
            put("reserveTime",scheduleOrderVo.getReserveTime());
            put("amount",scheduleOrderVo.getAmount());
        }};
        // 调用医院本部系统 进行数据出来 预约数更新
        JSONObject result = HttpRequestHelper.sendRequest(resultMap, "http://localhost:9998/order/submitOrder");
        if (result.getInteger("code") == 200){
            // 获取result中的map数据
            JSONObject jsonObject = result.getJSONObject("data");
            String resultCode = result.getString("resultCode");
            String resultMsg = result.getString("resultMsg");
            // 唯一记录标识 医院预约记录主键
            String hosRecordId = jsonObject.getString("hosRecordId");
            // 预约序号
            Integer number = jsonObject.getInteger("number");
            // 预约时间
            String fetchTime = jsonObject.getString("fetchTime");
            // 区号地址
            String fetchAddress = jsonObject.getString("fetchAddress");
            // 封装订单信息到OrderInfo
            OrderInfo orderInfo = new OrderInfo();
            BeanUtils.copyProperties(scheduleOrderVo,orderInfo);
            // 生成订单交易号
            String outTradeNo = System.currentTimeMillis() + ""+ new Random().nextInt(100);
            orderInfo.setOutTradeNo(outTradeNo);
            orderInfo.setScheduleId(scheduleId);
            orderInfo.setUserId(patient.getUserId());
            orderInfo.setPatientId(patientId);
            orderInfo.setPatientName(patient.getName());
            orderInfo.setPatientPhone(patient.getPhone());
            // 待支付
            orderInfo.setOrderStatus(OrderStatusEnum.UNPAID.getStatus());
            //设置添加数据--医院接口返回数据
            orderInfo.setHosRecordId(hosRecordId);
            // 序号
            orderInfo.setNumber(number);
            // 取号时间 格式化
            orderInfo.setFetchTime(fetchTime);
            orderInfo.setFetchAddress(fetchAddress);
            // 把orderInfo数据存入本地yygh_order表
            log.info("打印存入order数据库的" + orderInfo);
            baseMapper.insert(orderInfo);
            // 可预约数
            Integer reservedNumber = jsonObject.getInteger("reservedNumber");
            // 排班剩余预约数
            Integer availableNumber = jsonObject.getInteger("availableNumber");
            /**
             *  Send to RabbitMQ
             */
            // 封装可预约数、剩余可预约数
            OrderMqVo orderMqVo = OrderMqVo.builder().scheduleId(scheduleId)
                    .reservedNumber(reservedNumber).availableNumber(availableNumber).build();
            // 封装短信信息
            Map<String, Object> noteMap = new HashMap<String, Object>(){{
                computeIfAbsent("title",k->(orderInfo.getHosname()+"|"+orderInfo.getDepname()+"|"+orderInfo.getTitle()));
                computeIfAbsent("amount",k->orderInfo.getAmount());
                computeIfAbsent("reserveDate", k->orderInfo.getReserveDate());
                computeIfAbsent("name",k->orderInfo.getPatientName());
                computeIfAbsent("quitTime",k->new DateTime(orderInfo.getQuitTime()).toString("yyyy-MM-dd HH:mm"));
            }};
            MsmVo msmVo = MsmVo.builder().param(noteMap).phone(orderInfo.getPatientPhone()).build();
            orderMqVo.setMsmVo(msmVo);
            // 发送mq消息
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_ORDER,MqConst.QUEUE_ORDER,orderMqVo);
            return orderInfo.getId();
        }else {
            log.info("下单失败了~");
            throw new YyghException(ResultCodeEnum.DATA_ERROR.getCode(),"处理订单的时候医院响应状态异常~");
        }
    }
}
