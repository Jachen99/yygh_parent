package space.jachen.yygh.hosp.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import space.jachen.yygh.hosp.service.ScheduleService;
import space.jachen.yygh.model.hosp.Schedule;
import space.jachen.yygh.rabbitmq.config.MqConst;
import space.jachen.yygh.rabbitmq.service.RabbitService;
import space.jachen.yygh.vo.msm.MsmVo;
import space.jachen.yygh.vo.order.OrderMqVo;

import java.io.IOException;

/**
 * RabbitMQ监听器
 * 用于接收service-order发送的订单对象消息
 * 同时向RabbitMQ发送消息用于更细可预约号数量以便于给用户发送短信。
 *
 * @author JaChen
 * @date 2023/2/11 22:16
 */
@Component
public class HospitalReceiver {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private RabbitService rabbitService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_ORDER,durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_ORDER),
            key = {MqConst.ROUTING_ORDER}
    ))
    public void receiver(OrderMqVo orderMqVo, Message message, Channel channel) throws IOException {
        // 下单成功更新预约数
        Schedule schedule = scheduleService.getById(orderMqVo.getScheduleId());
        schedule.setReservedNumber(orderMqVo.getReservedNumber());
        schedule.setAvailableNumber(orderMqVo.getAvailableNumber());
        scheduleService.update(schedule);
        // 发送短信
        MsmVo msmVo = orderMqVo.getMsmVo();
        if(null != msmVo) {
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_MSM, MqConst.ROUTING_MSM_ITEM, msmVo);
        }
    }

}
