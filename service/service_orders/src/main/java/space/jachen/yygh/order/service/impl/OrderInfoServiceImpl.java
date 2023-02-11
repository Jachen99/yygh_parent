package space.jachen.yygh.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import space.jachen.yygh.user.PatientFeignClient;
import space.jachen.yygh.vo.hosp.ScheduleOrderVo;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    /**
     * 生成订单
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
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("hoscode",scheduleOrderVo.getHoscode());
        resultMap.put("depcode",scheduleOrderVo.getDepcode());
        resultMap.put("hosScheduleId",scheduleOrderVo.getHosScheduleId());
        resultMap.put("reserveDate",scheduleOrderVo.getReserveDate());
        resultMap.put("reserveTime",scheduleOrderVo.getReserveTime());
        resultMap.put("amount",scheduleOrderVo.getAmount());
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
            // 取号时间
            orderInfo.setFetchTime(fetchTime);
            orderInfo.setFetchAddress(fetchAddress);
            // 把orderInfo数据存入本地yygh_order表
            log.info("打印存入order数据库的" + orderInfo);
            baseMapper.insert(orderInfo);
            // TODO 更新排班数量
            // 可预约数
            Integer reservedNumber = jsonObject.getInteger("reservedNumber");
            //排班剩余预约数
            Integer availableNumber = jsonObject.getInteger("availableNumber");
            // TODO 给就诊人发短信 RabbitMQ
            return orderInfo.getId();
        }else {
            throw new YyghException(ResultCodeEnum.DATA_ERROR.getCode(),"处理订单的时候医院响应状态异常");
        }
    }
}
