package space.jachen.yygh.sms.service;

/**
 * @author JaChen
 * @date 2023/2/6 23:49
 */
public interface SMSService {

    /**
     * 发送验证码的接口
     * @param phone  手机号
     * @param code  验证码
     * @return  返回boolean
     */
    boolean send(String phone, String code);
}
