package space.jachen.yygh.user.service.Impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import space.jachen.yygh.common.handler.YyghException;
import space.jachen.yygh.common.result.ResultCodeEnum;
import space.jachen.yygh.model.user.UserInfo;
import space.jachen.yygh.user.service.UserInfoService;
import space.jachen.yygh.user.service.WechatService;
import space.jachen.yygh.user.utils.ConstantPropertiesUtil;
import space.jachen.yygh.user.utils.HttpClientUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/7 16:48
 */
@Service
@Slf4j
public class WechatServiceImpl implements WechatService {

    @Resource
    private UserInfoService userInfoService;

    /**
     * 第二次调微信接口 完成登录
     * 通过code加上appid和appsecret获取access_token
     * @param code  临时凭据code
     * @return 返回access_token
     */
    @Override
    public String getAccessToken(String code) {

        // 封装的结果集
        Map<String, Object> packageMap = new HashMap<>();

        // 临时票据
        log.info("微信端响应回来的登录临时票据："+code);

        // 1、封装响应路径（appid、appkey、code）
        // 用StringBuffer进行字符串拼接
        StringBuffer baseAccessTokenUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");
        String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code);
        try {
            // 2、调用HttpClient请求微信接口 获取opneid和access_token
            String accessToken = HttpClientUtils.get(accessTokenUrl);
            log.info("accessToken:"+accessToken);
            // 格式转换
            JSONObject object = JSONObject.parseObject(accessToken);
            String openid = object.getString("openid");
            String access_token = object.getString("access_token");

            // 根据openid判断是不是第一次使用微信登录，进行登录校验
            UserInfo userInfo = userInfoService.findByOpenId(openid);
            // 3、 第一次微信注册 数据库中每页openid
            if (userInfo==null){
                // 3.1、通过openid和access_token请求微信地址，得到扫描人信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
                String resultInfo = HttpClientUtils.get(userInfoUrl);
                log.info("resultInfo:"+resultInfo);
                // 3.2、 格式转换
                JSONObject infoObject = JSONObject.parseObject(resultInfo);
                // 3.3、获取用户信息存入数据库
                String nickname = infoObject.getString("nickname");
                String headimgurl = infoObject.getString("headimgurl");
                log.info("微信注册用户头像："+headimgurl);
                userInfo = UserInfo.builder().nickName(nickname).img(headimgurl)
                        .status(1).openid(openid).build(); // 注意 ：这里没有判断手机号 我们再if外面判断
                userInfoService.save(userInfo);
            }
            //4、有openid 不是第一次登录
            //4.1 、封装name和token字符串
            packageMap = userInfoService.packageResult(userInfo);
            //4.2 、如果openid不为空，绑定手机号，如果openid为空，不需要绑定手机号
            if(StringUtils.isEmpty(userInfo.getPhone())) {
                packageMap.put("openid", userInfo.getOpenid());
            } else {
                packageMap.put("openid", "");
            }
            log.info("登录后重定向返回的数据：token="+packageMap.get("token")
            +"openid="+packageMap.get("openid")+"name=" +URLEncoder.encode(packageMap.get("name").toString(),"utf-8"));
            // 5、重定向 跳转到前端页面
            return "redirect:http://localhost:3000/weixin/callback?token=" +packageMap.get("token")
                    + "&openid=" +packageMap.get("openid") +"&name=" +URLEncoder.encode(packageMap.get("name").toString(),"utf-8");
        } catch (Exception e) {
            throw new YyghException(ResultCodeEnum.SERVICE_ERROR.getCode(),"调取微信接口的http客户端出错了~");
        }
    }

    /**
     * 获取微信登录参数
     * @return  返回
     */
    @Override
    public Map<String, Object> getLoginParam() {

        // 重定向地址，需要进行UrlEncode
        String redirectUri = null;
        try {
            redirectUri = URLEncoder.encode(
                    ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new YyghException(ResultCodeEnum.DATA_ERROR.getCode(),"微信接口数据源异常");
        }

        // 把微信接口需要的信息封装在map返回给前端
        Map<String, Object> map = new HashMap<>();
        map.put("appid", ConstantPropertiesUtil.WX_OPEN_APP_ID);
        map.put("redirectUri", redirectUri);
        map.put("scope", "snsapi_login");
        map.put("state", System.currentTimeMillis()+"");  // 防止csrf攻击

        return map;
    }
}
