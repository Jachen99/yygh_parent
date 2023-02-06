package space.jachen.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.jachen.yygh.model.user.UserInfo;
import space.jachen.yygh.vo.user.LoginVo;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/6 15:20
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 用户登录
     * @param loginVo  登录Vo对象
     * @return  Map<String, UserInfo>
     */
    Map<String, Object> login(LoginVo loginVo);
}
