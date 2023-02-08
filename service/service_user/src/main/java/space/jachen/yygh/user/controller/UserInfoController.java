package space.jachen.yygh.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.common.utils.JwtHelper;
import space.jachen.yygh.model.user.UserInfo;
import space.jachen.yygh.user.service.UserInfoService;
import space.jachen.yygh.vo.user.LoginVo;
import space.jachen.yygh.vo.user.UserAuthVo;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/6 15:20
 */
@Api(tags = "用户登录接口")
@RestController
@RequestMapping("/yygh/user")
@Slf4j
public class UserInfoController {
    @Autowired
    private UserInfoService infoService;

    @ApiOperation("用户认证")
    @PostMapping("/auth/userAuth")
    public JsonData<String> userAuth(@RequestBody UserAuthVo userAuthVo,
                             HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token))return JsonData.fail();
        log.info("用户认证的token："+token);
        Long userId = JwtHelper.getUserId(token);
        infoService.userAuth(userId, userAuthVo);
        return JsonData.ok();
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/auth/getUserInfo")
    public JsonData<Map<String, UserInfo>> getUserInfo(HttpServletRequest request) {
        /*
        不明白为什么响应俩次token?
        因为第一次是OPTIONS请求 预检
        优化 "Options" 请求的方法：
        1、缓存token：缓存从Optimizely获取的token，以避免每次请求都进行重复的Optimizely检查。
        2、判断是否需要Optimizely检查：在特定情况下，您可能不需要进行 Optimizely 检查。例如，如果您的后台不需要该功能，则可以考虑跳过 Optimizely 检查。
        3、可以减少 Optimizely 请求的频率或使用更快速的机制（例如本地缓存）来请求 token。
         */
        String token = request.getHeader("token");
        log.info("获取用户信息的token："+token);
        if (StringUtils.isEmpty(token))return JsonData.fail();
        Long userId = JwtHelper.getUserId(token);
        UserInfo userInfo = infoService.getById(userId);
        Map<String, UserInfo> map = new HashMap<>();
        map.put("userInfo",userInfo);
        return JsonData.ok(map);
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public JsonData<Map<String,Object>> login(@RequestBody LoginVo loginVo){
        Map<String,Object> map = infoService.login(loginVo);
        return JsonData.ok(map);
    }

}
