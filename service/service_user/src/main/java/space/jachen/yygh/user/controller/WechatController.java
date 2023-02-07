package space.jachen.yygh.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.user.service.WechatService;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/7 14:21
 */
@Api(tags = "微信登录接口")
@Controller
@RequestMapping("yygh/user/wx")
public class WechatController {


    @Resource
    private WechatService wechatService;

    /**
     * 获取微信登录参数
     * @return 返回登录的验证
     */
    @ResponseBody
    @GetMapping("/getLoginParam")
    public JsonData<Map<String, Object>> getLoginParam() {
        Map<String, Object> loginParam = wechatService.getLoginParam();
        return JsonData.ok(loginParam);
    }


    /**
     * 微信自动调用的方法
     * @param code  临时凭据code
     * @param state  时间戳  防止csrf攻击
     * @return access_token
     */
    @ApiOperation(value = "获取临时凭证")
    @GetMapping("/callback")
    public String callback(String code, String state) {
        //1、打印授权临时票据code和时间戳
        System.out.println("code = " + code);
        System.out.println("state = " + state);
        return wechatService.getAccessToken(code);
    }
}
