package space.jachen.yygh.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import space.jachen.yygh.common.handler.YyghException;
import space.jachen.yygh.common.result.ResultCodeEnum;
import space.jachen.yygh.common.utils.JwtHelper;
import space.jachen.yygh.enums.AuthStatusEnum;
import space.jachen.yygh.model.user.UserInfo;
import space.jachen.yygh.user.mapper.UserInfoMapper;
import space.jachen.yygh.user.service.UserInfoService;
import space.jachen.yygh.vo.user.LoginVo;
import space.jachen.yygh.vo.user.UserAuthVo;
import space.jachen.yygh.vo.user.UserInfoQueryVo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/6 15:21
 */
@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private StringRedisTemplate template;

    /**
     * 用户列表（条件查询带分页）
     * @param pageParam   分页条件
     * @param userInfoQueryVo  userInfoQueryVo对象
     * @return  IPage<UserInfo>
     */
    @Override
    public Page<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo) {

        // 1、获取条件
        String name = userInfoQueryVo.getKeyword(); //用户名称
        Integer status = userInfoQueryVo.getStatus();//用户状态
        Integer authStatus = userInfoQueryVo.getAuthStatus(); //认证状态
        String createTimeBegin = userInfoQueryVo.getCreateTimeBegin(); //开始时间
        String createTimeEnd = userInfoQueryVo.getCreateTimeEnd(); //结束时间
        // 2、封装查询条件
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper =  !StringUtils.isEmpty(name) ? wrapper.like(UserInfo::getName,name) : wrapper;
        wrapper =  !StringUtils.isEmpty(status) ? wrapper.eq(UserInfo::getStatus,status) : wrapper;
        wrapper =  !StringUtils.isEmpty(authStatus) ? wrapper.eq(UserInfo::getAuthStatus,authStatus) : wrapper;
        wrapper =  !StringUtils.isEmpty(createTimeBegin) ? wrapper.ge(UserInfo::getCreateTime,createTimeBegin) : wrapper;
        wrapper =  !StringUtils.isEmpty(createTimeEnd) ? wrapper.le(UserInfo::getCreateTime,createTimeEnd) : wrapper;
        // 3、查询数据库
        Page<UserInfo> userInfoPage = baseMapper.selectPage(pageParam, wrapper);
        userInfoPage.getRecords().forEach(this::packageUserInfo);
        return userInfoPage;
    }

    /**
     * 编号变成对应值封装
     *
     * @param userInfo UserInfo对象
     */
    private void packageUserInfo(UserInfo userInfo) {
        //处理认证状态编码
        userInfo.getParam().put("authStatusString",
                AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus()));
        //处理用户状态 0  1
        String statusString = userInfo.getStatus() ==0 ?"锁定" : "正常";
        userInfo.getParam().put("statusString",statusString);
    }

    /**
     * 用户认证的接口
     * @param userId     用户id
     * @param userAuthVo 用户认证Vo值对象
     */
    @Override
    public void userAuth(Long userId, UserAuthVo userAuthVo) {
        UserInfo userInfo = baseMapper.selectById(userId);
        if (userInfo!=null){
            userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
            BeanUtils.copyProperties(userAuthVo,userInfo);
            baseMapper.updateById(userInfo);
            log.info("存入数据库用户认证的数据："+userInfo);
        }else {
            throw new YyghException(ResultCodeEnum.LOGIN_MOBLE_ERROR.getCode(),"用户信息异常，无法进行用户认证");
        }
    }

    /**
     * 根据openid获取用户信息
     * @param openid  授权用户唯一标识
     * @return  UserInfo
     */
    @Override
    public UserInfo findByOpenId(String openid) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getOpenid,openid);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 用户登录
     * @param loginVo  登录Vo对象
     * @return  Map<String, UserInfo>  key为name
     */
    @Override
    public Map<String, Object> login(LoginVo loginVo) {

        // 1、接受参数
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();

        // 2、参数判空校验
        if (StringUtils.isEmpty(phone)||StringUtils.isEmpty(code)){
            throw new YyghException(ResultCodeEnum
                    .ARGUMENT_VALID_ERROR.getCode(), "登录参数为空");
        }
        // 新增1、 整合redis完成验证码校验
        String redisCode = template.opsForValue().get(phone);
        if (!code.equals(redisCode)){
            throw new YyghException(ResultCodeEnum.ARGUMENT_VALID_ERROR.getCode(),"验证码不正确");
        }

        // 新增2 整合微信登录与手机登录
        // openid存在说明微信不是第一次登录了
        // 进入分类讨论
        // 如果不进该条件 继续之前走之前的逻辑
        String openid = loginVo.getOpenid();
        if (!StringUtils.isEmpty(openid)){
            UserInfo userInfoByPhone = this.getByPhone(phone);
            //No1、手机号不存在 直接插入
            if(userInfoByPhone == null) {
                //根据openid查询微信信息
                UserInfo userInfo = this.getByOpenid(openid);
                userInfo.setPhone(phone);
                baseMapper.updateById(userInfo);
                return this.packageResult(userInfo);
            }else { //No2、手机号存在 合并后更新
                UserInfo wechatInfo = this.getByOpenid(openid);
                //把微信数据设置到手机号数据中
                userInfoByPhone.setOpenid(wechatInfo.getOpenid());
                userInfoByPhone.setNickName(wechatInfo.getNickName());
                baseMapper.deleteById(wechatInfo.getId());
                baseMapper.updateById(userInfoByPhone);
                return this.packageResult(userInfoByPhone);
            }
        }

        // 3、查询用户原始信息
        UserInfo userInfo = this.getByPhone(phone);

        // 4、用户身份校验
        // 4.1、 对第一次登录的用户 初始化信息
        if (userInfo == null){
            userInfo = UserInfo.builder().status(1).phone(phone).build();
            this.save(userInfo);  // 等价于 baseMapper.insert(info)
        }

        // 4.2、判断登录状态 0为禁止登录
        if (userInfo.getStatus()==0){
            throw new YyghException(ResultCodeEnum
                    .ACCOUNT_STOP.getCode(), "账号被锁定");
        }

        return this.packageResult(userInfo);
    }

    /**
     * 封装登录信息结果集的通用方法
     * @param userInfo UserInfo对象
     * @return  返回一个Map<String,Object> 封装返回信息
     */
    @Override
    public Map<String, Object> packageResult(UserInfo userInfo) {
        // 4.3、获取返回的用户名
        String name = userInfo.getName();
        // 页面展示的name 赋值顺序 name > nikeName > phone
        name = StringUtils.isEmpty(name) ? userInfo.getNickName() : name;
        name = StringUtils.isEmpty(name) ? userInfo.getPhone() : name;

        // 5、封装返回信息
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name",name);
        //  封装token信息 用户唯一表示 用于单点登录
        String token = JwtHelper.createToken(userInfo.getId(), name);
        hashMap.put("token",token);
        return hashMap;
    }

    /**
     * 根据手机号获取UserInfo的方法
     * @param phone  手机号
     * @return 返回UserInfo对象
     */
    public UserInfo getByPhone(String phone){
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new
                LambdaQueryWrapper<UserInfo>().eq(UserInfo::getPhone, phone);
        return baseMapper.selectOne(lambdaQueryWrapper);
    }

    /**
     * 根据openid获取UserInfo的方法
     * @param openid  授权用户唯一标识
     * @return UserInfo
     */
    public UserInfo getByOpenid(String openid){
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new
                LambdaQueryWrapper<UserInfo>().eq(UserInfo::getOpenid, openid);
        return baseMapper.selectOne(lambdaQueryWrapper);
    }
}
