package space.jachen.yygh.hosp;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import space.jachen.yygh.vo.hosp.ScheduleOrderVo;

/**
 * @author JaChen
 * @date 2023/2/11 10:51
 */
@FeignClient(value = "service-hosp")
public interface HospFeignClient {
    String baseURL = "/user/hosp/hospital";
    @GetMapping(baseURL+"/getScheduleOrderVo/{scheduleId}")
    ScheduleOrderVo getScheduleOrderVo(@PathVariable String scheduleId);
}
