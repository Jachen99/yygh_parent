package space.jachen.yygh.statistics;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import space.jachen.yygh.vo.order.OrderCountQueryVo;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/15 10:08
 */
@FeignClient(value = "service-orders")
@Repository
public interface OrderFeignClient {
    String baseURL = "/api/yygh/order";
    /**
     * 获取订单统计数据
     */
    @PostMapping(baseURL+"auth/getCountMap")
    Map<String, Object> getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo);
}
