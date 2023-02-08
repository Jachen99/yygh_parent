package space.jachen.yygh.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @ApiOperation("获取用户信息")
    @GetMapping("/auth/getUserInfo")
    public JsonData<Map<String, UserInfo>> getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        log.info("获取用户信息的token："+token);
        Long userId = JwtHelper.getUserId(token);
        UserInfo userInfo = infoService.getById(userId);

        Map<String, UserInfo> map = new HashMap<>();
        map.put("userInfo",userInfo);
        return JsonData.ok(map);
    }

    @ApiOperation("用户认证")
    @PostMapping("/auth/userAuth")
    public JsonData<String> userAuth(@RequestBody UserAuthVo userAuthVo,
                             HttpServletRequest request) {
        String token = request.getHeader("token");
        log.info("用户认证的token："+token);
        Long userId = JwtHelper.getUserId(token);
        infoService.userAuth(userId, userAuthVo);
        return JsonData.ok();
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public JsonData<Map<String,Object>> login(@RequestBody LoginVo loginVo){

        Map<String,Object> map = infoService.login(loginVo);

        return JsonData.ok(map);

    }

}
