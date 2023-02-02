package space.jachen.yygh.hosp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * 启动类
 *
 * @author JaChen
 * @date 2023/01/28
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableMongoAuditing
@ComponentScan(basePackages = "space.jachen") // 配置扩大扫描包 让它扫到公共模块下的swagger2
public class ServiceHospApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class, args);
    }
}