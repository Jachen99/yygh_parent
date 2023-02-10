package space.jachen.yygh.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * ${end}
 *
 * @author JaChen
 * @date ${DATE} ${TIME}
 */
@SpringBootApplication
@ComponentScan(basePackages = {"space.jachen"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"space.jachen"})
@MapperScan(basePackages = "space.jachen.yygh.order.mapper")
public class ServiceOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOrderApplication.class, args);
    }
}