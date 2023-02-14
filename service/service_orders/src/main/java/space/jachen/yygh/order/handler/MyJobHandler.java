package space.jachen.yygh.order.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import space.jachen.yygh.enums.OrderStatusEnum;
import space.jachen.yygh.model.order.OrderInfo;
import space.jachen.yygh.order.mapper.OrderInfoMapper;
import space.jachen.yygh.rabbitmq.config.MqConst;
import space.jachen.yygh.rabbitmq.service.RabbitService;
import space.jachen.yygh.vo.msm.MsmVo;

import java.util.Date;
import java.util.List;

/**
 * @author JaChen
 * @date 2023/2/14 15:53
 */
@Component
public class MyJobHandler {

    @Autowired
    private RabbitService rabbitService;
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    /**
     * 就诊提醒的handler方法
     *
     * @param param  null
     * @return  ReturnT<String>
     */
    @XxlJob(value = "demoJobHandler1",init = "init",destroy = "destroy")
    public ReturnT<String> execute(String param){
        System.out.println("execute方法执行了 ====》 ");
        // 获取查询的时间
        DateTime dateTime = new DateTime().plusDays(1);
        String dateString = dateTime.toString("yyyy-MM-dd");
        Date date = new DateTime(dateString).toDate();
        LambdaQueryWrapper<OrderInfo> queryWrapper = new LambdaQueryWrapper<OrderInfo>(){{
            eq(OrderInfo::getReserveDate,date);  // 查询就诊日期
            ne(OrderInfo::getOrderStatus, OrderStatusEnum.CANCLE.getStatus());  // 查询没有取消的订单
        }};
        List<OrderInfo> orderInfoList = orderInfoMapper.selectList(queryWrapper);
        for(OrderInfo orderInfo : orderInfoList) {
            // 短信提示
            MsmVo msmVo = new MsmVo();
            msmVo.setPhone(orderInfo.getPatientPhone());
            // 发送mq发送短信
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_MSM, MqConst.ROUTING_MSM_ITEM, msmVo);
        }
        return ReturnT.SUCCESS;
    }

    private void init(){
        System.out.println("MyJobHandler init >>>>> " + true);
        String jobParam = XxlJobHelper.getJobParam();
        System.out.println("jobParam = " + jobParam);
    }

    private void destroy(){

        System.out.println("MyJobHandler init >>>>> " + true);

    }

}
