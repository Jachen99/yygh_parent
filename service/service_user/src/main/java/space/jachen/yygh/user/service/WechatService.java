package space.jachen.yygh.user.service;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/7 16:42
 */
public interface WechatService {

    /**
     * 第二次调微信接口 通过code加上appid和appsecret获取access_token
     * @param code  临时凭据code
     * @return 返回access_token
     */
    String getAccessToken(String code);

    /**
     * 获取微信登录参数
     * @return  返回
     */
    Map<String, Object> getLoginParam();
}
