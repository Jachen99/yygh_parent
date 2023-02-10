package space.jachen.yygh.cmn;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author JaChen
 * @date 2023/2/3 15:08
 */
@FeignClient(value = "service-cmn")
public interface DictFeignClient {
    String baseURL = "/admin/cmn/dict";
    @GetMapping(value = baseURL+"/getName/{parentDictCode}/{value}")
    String getName(@PathVariable("parentDictCode") String parentDictCode, @PathVariable("value") String value);
    @GetMapping(value = baseURL+"/getName/{value}")
    String getName(@PathVariable("value") String value);
}
