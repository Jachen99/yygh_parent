package space.jachen.yygh.rabbitmq;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 消息队列RabbitMQ启动类
 *
 * @author JaChen
 * @date 2023/02/11
 */
@SpringBootApplication
public class RabbitApplicationMain {
    public static void main(String[] args) {
        SpringApplication.run(RabbitApplicationMain.class);
    }

    /**
     * Jackson2JsonMessageConverter 用于将 Java 对象序列化为 JSON 字符串并发送到 RabbitMQ，
     * 以及将 JSON 字符串反序列化为 Java 对象。这样，在 RabbitMQ 中存储的就是 JSON 字符串，可以通过许多不同的语言读取和处理这些消息。
     * 如果不使用 Jackson2JsonMessageConverter，那么在 RabbitMQ 中存储的就是二进制字节数组，必须使用特定的库进行反序列化，
     * 并且不利于跨语言的消息传递。因此，添加 Jackson2JsonMessageConverter 是为了方便消息的传递和处理。
     *
     * @return MessageConverter
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}