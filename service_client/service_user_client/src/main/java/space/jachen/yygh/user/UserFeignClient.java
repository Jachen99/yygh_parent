package space.jachen.yygh.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import space.jachen.yygh.model.user.Patient;

/**
 * @author JaChen
 * @date 2023/2/10 21:06
 */
@FeignClient("service-user")
public interface UserFeignClient {
    String baseURL = "/yygh/user/patient";
    // 根据id获取就诊人信息
    @GetMapping(baseURL+"/auth/get/{id}")
    Patient getDetailById(Long id);
}
