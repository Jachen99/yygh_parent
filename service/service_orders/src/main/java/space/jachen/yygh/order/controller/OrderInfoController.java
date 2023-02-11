package space.jachen.yygh.order.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.order.service.OrderInfoService;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单表 前端控制器
 *
 * @author jachen
 * @since 2023-02-10
 */
@RestController
@RequestMapping("/yygh/order/orderInfo")
public class OrderInfoController {
    @Autowired
    private OrderInfoService orderInfoService;

    @ApiOperation(value = "创建订单")
    @PostMapping("/auth/submitOrder/{scheduleId}/{patientId}")
    public JsonData<Map<String, Long>> submitOrder(
            @ApiParam(name = "scheduleId", value = "排班id", required = true) @PathVariable String scheduleId,
            @ApiParam(name = "patientId", value = "就诊人id", required = true) @PathVariable Long patientId) {
        Long orderId = orderInfoService.saveOrder(scheduleId, patientId);
        Map<String, Long> hashMap = new HashMap<>();
        hashMap.put("orderId",orderId);
        return JsonData.ok(hashMap);
    }
}

