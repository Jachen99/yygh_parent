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
     * 根据openid获取用户信息
     * @param openid  授权用户唯一标识
     * @return  UserInfo
     */
    UserInfo findByOpenId(String openid);

    /**
     * 封装登录信息结果集的通用方法
     * @param userInfo UserInfo对象
     * @return  返回一个Map<String,Object> 封装返回信息
     */
    Map<String, Object> packageResult(UserInfo userInfo);

    /**
     * 用户登录
     * @param loginVo  登录Vo对象
     * @return  Map<String, UserInfo>
     */
    Map<String, Object> login(LoginVo loginVo);
}
