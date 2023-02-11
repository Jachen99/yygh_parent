package space.jachen.yygh.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.model.user.Patient;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/10 21:06
 */
@FeignClient("service-user")
public interface PatientFeignClient {
    String baseURL = "/yygh/user/patient";
    // 根据id获取就诊人信息
    @GetMapping(baseURL+"/auth/get/{id}")
    JsonData<Map<String, Patient>> getDetailById(@PathVariable Long id);
}
