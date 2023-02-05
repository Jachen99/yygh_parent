package space.jachen.yygh.hosp.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.yygh.common.result.JsonData;

import java.util.HashMap;

/**
 * @author JaChen
 * @date 2023/1/28 16:40
 */
@Api(tags = "登录接口")
@RestController
@RequestMapping("/admin/hosp/user")
public class LoginController {

    @ApiOperation("登录")
    @PostMapping("/login")
    public JsonData<HashMap<String, String>> login(){
        //{"code":20000,"data":{"token":"admin-token"}}
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token","admin-token");
        return JsonData.ok(hashMap);
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public JsonData<HashMap<String, String>> info(){
        //{"code":20000,"data":{"roles":["admin"],"introduction":"I am a super administrator","avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif","name":"Super Admin"}}

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name","季老板");
        hashMap.put("avatar","https://blog.jiguanchen.space/assets/img/head.jpg");
        return JsonData.ok(hashMap);
    }

}