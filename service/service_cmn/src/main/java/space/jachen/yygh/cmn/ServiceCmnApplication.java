package space.jachen.yygh.cmn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 数据字典
 *
 * @author JaChen
 * @date 2023-01-28
 */
@SpringBootApplication
@ComponentScan(basePackages = "space.jachen")
@MapperScan(basePackages = "space.jachen.yygh.cmn.mapper")
public class ServiceCmnApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCmnApplication.class, args);
    }
}