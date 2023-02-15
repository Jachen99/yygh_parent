package space.jachen.yygh.statistics.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.statistics.OrderFeignClient;
import space.jachen.yygh.vo.order.OrderCountQueryVo;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/15 10:14
 */
@Api(tags = "统计管理接口")
@RestController
@RequestMapping("/admin/statistics")
public class StatisticsController {
    @Autowired
    private OrderFeignClient orderFeignClient;
    @ApiOperation(value = "获取订单统计数据")
    @PostMapping("getCountMap")
    public JsonData<Map<String, Object>> getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo) {
        Map<String, Object> map = orderFeignClient.getCountMap(orderCountQueryVo);
        return JsonData.ok(map);
    }
}