package space.jachen.yygh.order.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.enums.OrderStatusEnum;
import space.jachen.yygh.model.order.OrderInfo;
import space.jachen.yygh.order.service.OrderInfoService;
import space.jachen.yygh.vo.order.OrderQueryVo;

import java.util.HashMap;
import java.util.List;
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

    @ApiOperation(value = "获取所有订状态")
    @GetMapping("/auth/getStatusList")
    public JsonData<Map<String, List<Map<String,Object>>>> getStatusList(){
        Map<String, List<Map<String,Object>>> resultMap = new HashMap<String, List<Map<String,Object>>>(){{
            put("statusList", OrderStatusEnum.getStatusList());
        }};
        return JsonData.ok(resultMap);
    }

    @ApiOperation(value = "分页查询所有订单")
    @GetMapping("/auth/{page}/{limit}")
    public JsonData<Map<String, Page<OrderInfo>>> getPageList(@PathVariable Long page, @PathVariable Long limit, OrderQueryVo orderQueryVo){
        Page<OrderInfo> orderInfoPage = new Page<>(page, limit);
        Page<OrderInfo> orderPageList = orderInfoService.getPageList(orderInfoPage,orderQueryVo);
        Map<String, Page<OrderInfo>> pageModel = new HashMap<String, Page<OrderInfo>>() {{
            put("pageModel", orderPageList);
        }};
        return JsonData.ok(pageModel);
    }

    @ApiOperation(value = "订单详情页")
    @GetMapping("/auth/getOrders/{orderId}")
    public JsonData<Map<String, OrderInfo>> getOrders(@PathVariable Long orderId){
        OrderInfo orderInfo = orderInfoService.getOrderDetailById(orderId);
        Map<String, OrderInfo> resultMap = new HashMap<String, OrderInfo>(){{
            put("orderInfo",orderInfo);
        }};
        return JsonData.ok(resultMap);
    }
}

