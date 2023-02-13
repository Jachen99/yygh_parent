package space.jachen.yygh.order.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.yygh.order.service.OrderInfoService;

/**
 * 微信接口 前端控制器
 *
 * @author JaChen
 * @date 2023/2/13 14:09
 */
@Api(tags = "微信接口")
@RestController
public class weChatController {
    @Autowired
    private OrderInfoService orderInfoService;
//    @PostMapping("")
//    public JsonData createNative(){
//
//    }
}
