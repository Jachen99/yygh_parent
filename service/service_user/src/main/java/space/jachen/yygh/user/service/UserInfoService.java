package space.jachen.yygh.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import space.jachen.yygh.model.user.UserInfo;
import space.jachen.yygh.vo.user.LoginVo;
import space.jachen.yygh.vo.user.UserAuthVo;
import space.jachen.yygh.vo.user.UserInfoQueryVo;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/6 15:20
 */
public interface UserInfoService extends IService<UserInfo> {


    /**
     * 用户列表（条件查询带分页）
     * @param pageParam   分页条件
     * @param userInfoQueryVo  userInfoQueryVo对象
     * @return  IPage<UserInfo>
     */
    IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo);

    /**
     * 用户认证的接口
     *
     * @param userId     用户id
     * @param userAuthVo 用户认证Vo值对象
     */
    void userAuth(Long userId, UserAuthVo userAuthVo);

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
