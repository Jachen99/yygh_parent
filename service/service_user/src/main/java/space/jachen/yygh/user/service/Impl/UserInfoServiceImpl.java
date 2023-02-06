package space.jachen.yygh.user.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import space.jachen.yygh.common.handler.YyghException;
import space.jachen.yygh.common.result.ResultCodeEnum;
import space.jachen.yygh.common.utils.JwtHelper;
import space.jachen.yygh.model.user.UserInfo;
import space.jachen.yygh.user.mapper.UserInfoMapper;
import space.jachen.yygh.user.service.UserInfoService;
import space.jachen.yygh.vo.user.LoginVo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/6 15:21
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {


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
        // TODO: 整合redis完成验证码校验

        // 3、查询用户原始信息
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new
                LambdaQueryWrapper<UserInfo>().eq(UserInfo::getPhone, phone);
        UserInfo userInfo = baseMapper.selectOne(lambdaQueryWrapper);

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

        // 4.3、获取返回的用户名
        String name = userInfo.getName();
        // name 赋值顺序 name > nikeName > phone
        name = StringUtils.isEmpty(name) ? userInfo.getNickName() : name;
        name = StringUtils.isEmpty(name) ? phone : name;

        // 5、封装返回信息
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name",name);
        //  封装token信息
        String token = JwtHelper.createToken(userInfo.getId(), name);
        hashMap.put("token",token);
        return hashMap;
    }
}
