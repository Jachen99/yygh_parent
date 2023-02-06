package space.jachen.yygh.user;

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
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "space.jachen")
@ComponentScan(basePackages = "space.jachen")
@MapperScan("space.jachen.yygh.user.mapper")
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
    }
}