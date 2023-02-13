package space.jachen.yygh.order.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.order.service.WeChatService;

import java.util.Map;

/**
 * 微信接口 前端控制器
 *
 * @author JaChen
 * @date 2023/2/13 14:09
 */
@Api(tags = "微信接口")
@RestController
@RequestMapping("/yygh/order/wx")
public class WeChatController {
    @Autowired
    private WeChatService weChatService;

    @ApiOperation("微信支付生成支付二维码")
    @GetMapping("/createNative/{orderId}")
    public JsonData<Map<String,Object>> createNative(@PathVariable("orderId") Long orderId) {
        Map<String,Object> map = weChatService.createNative(orderId);
        return JsonData.ok(map);
    }
}
