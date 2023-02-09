package space.jachen.yygh.user.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.model.user.UserInfo;
import space.jachen.yygh.user.service.UserInfoService;
import space.jachen.yygh.vo.user.UserInfoQueryVo;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 平台用户管理控制器
 * @author JaChen
 * @date 2023/2/8 20:55
 */
@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/admin/user")
public class UserController {
    @Resource
    private UserInfoService userInfoService;

    @ApiOperation("用户审批")
    @GetMapping("/approval/{id}/{authStatus}")
    public JsonData<Object> userApproval(
            @PathVariable(value = "id")Long userId,
            @PathVariable Integer authStatus){
        userInfoService.approval(userId,authStatus);
        return JsonData.ok();
    }

    @ApiOperation("平台用户详情")
    @GetMapping("/show/{id}")
    public JsonData<Map<String,Object>> userDetailShow(@PathVariable(value = "id") Long userId){

        Map<String,Object> result = userInfoService.userDetailShow(userId);
        return JsonData.ok(result);
    }

    @ApiOperation("平台用户列表")
    @GetMapping("/{page}/{limit}")
    public JsonData<Map<String, IPage<UserInfo>>> list(@PathVariable Long page,
                         @PathVariable Long limit,
                         UserInfoQueryVo userInfoQueryVo) {
        Page<UserInfo> pageParam = new Page<>(page,limit);
        IPage<UserInfo> pages =
                userInfoService.selectPage(pageParam,userInfoQueryVo);
        Map<String, IPage<UserInfo>> hashMap = new HashMap<>();
        hashMap.put("pages",pages);
        return JsonData.ok(hashMap);
    }
}