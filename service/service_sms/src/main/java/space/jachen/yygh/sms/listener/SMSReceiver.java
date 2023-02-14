package space.jachen.yygh.sms.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import space.jachen.yygh.rabbitmq.config.MqConst;
import space.jachen.yygh.sms.service.SMSService;
import space.jachen.yygh.vo.msm.MsmVo;

/**
 * 消息队列的消费者 用于监听消息以便于发送短信给下单的用户。
 *
 * @author JaChen
 * @date 2023/2/11 16:43
 */
@Component
public class SMSReceiver {
    @Autowired
    private SMSService smsService;

    /**
     * RabbitListener：这是一个类级别的注解，用于标识一个消息监听器组件。
     * QueueBinding：这是一个方法级别的注解，用于绑定队列，交换机和路由键，它包含了下面三个注解的信息：
     * 1、Queue：用于配置队列的信息，其中的 value 属性表示队列名，durable 属性表示是否持久化，为 true 时表示队列在服务器重启后仍然存在。
     * 2、Exchange：用于配置交换机的信息，其中的 value 属性表示交换机名。
     * 3、key：用于配置路由键，表示消息发送到那个队列。
     *
     * @param msmVo 短信实体
     * @param message 封装了消息的所有属性，如消息体、消息头、消息属性等。
     * @param channel 与 RabbitMQ 服务器进行的通信，可创建多个。
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_MSM_ITEM, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_MSM),
            key = {MqConst.ROUTING_MSM_ITEM}
    ))
    public void send(MsmVo msmVo, Message message, Channel channel) {
        System.out.println("向就诊人" + msmVo.getPhone() + "发送了一条消息");
        smsService.send(msmVo);
    }
}
