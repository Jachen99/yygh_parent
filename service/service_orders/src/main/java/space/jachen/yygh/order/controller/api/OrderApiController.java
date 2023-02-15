package space.jachen.yygh.order.controller.api;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.yygh.order.service.OrderInfoService;
import space.jachen.yygh.vo.order.OrderCountQueryVo;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/15 10:02
 */
@RestController
@RequestMapping("api/yygh/order/")
public class OrderApiController {
    @Autowired
    private OrderInfoService orderService;

    @ApiOperation(value = "获取订单统计数据")
    @GetMapping("auth/getCountMap")
    public Map<String, Object> getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo) {
        return orderService.getCountMap(orderCountQueryVo);
    }
}
