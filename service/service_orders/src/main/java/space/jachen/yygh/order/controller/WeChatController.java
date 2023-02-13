package space.jachen.yygh.order.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.enums.PaymentTypeEnum;
import space.jachen.yygh.order.service.PaymentInfoService;
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
    @Autowired
    private PaymentInfoService paymentInfoService;

    @ApiOperation(value = "查询支付状态")
    @GetMapping("/queryPayStatus/{orderId}")
    public JsonData<Object> queryPayStatus(@PathVariable("orderId") Long orderId) {
        // 调用查询接口
        Map<String, String> resultMap = weChatService.queryPayStatus(orderId);
        if (resultMap == null) {
            return JsonData.fail().message("支付出错");
        }
        if ("SUCCESS".equals(resultMap.get("trade_state"))) {
            // 更改订单状态，处理支付结果
            String outTradeNo = resultMap.get("out_trade_no");
            paymentInfoService.paySuccess(outTradeNo, PaymentTypeEnum.WEIXIN.getStatus(), resultMap);
            return JsonData.ok().message("支付成功");
        }
        return JsonData.ok().message("支付中");
    }

    @ApiOperation("微信支付生成支付二维码")
    @GetMapping("/createNative/{orderId}")
    public JsonData<Map<String,Object>> createNative(@PathVariable("orderId") Long orderId) {
        Map<String,Object> map = weChatService.createNative(orderId);
        return JsonData.ok(map);
    }


}
