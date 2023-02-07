package space.jachen.yygh.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.user.service.UserInfoService;
import space.jachen.yygh.vo.user.LoginVo;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/6 15:20
 */
@Api(tags = "用户登录接口")
@RestController
@RequestMapping("/yygh/user")
public class UserInfoController {

    @Autowired
    private UserInfoService infoService;

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public JsonData<Map<String,Object>> login(@RequestBody LoginVo loginVo){

        Map<String,Object> map = infoService.login(loginVo);

        return JsonData.ok(map);

    }

}
