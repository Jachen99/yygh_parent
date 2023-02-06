package space.jachen.yygh.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * ${end}
 *
 * @author JaChen
 * @date ${DATE} ${TIME}
 */
@ComponentScan({"space.jachen"})
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消数据源自动配置
public class ServiceSMSApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceSMSApplication.class, args);
    }
}