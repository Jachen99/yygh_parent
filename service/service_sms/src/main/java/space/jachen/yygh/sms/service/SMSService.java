package space.jachen.yygh.sms.service;

import space.jachen.yygh.vo.msm.MsmVo;

/**
 * @author JaChen
 * @date 2023/2/6 23:49
 */
public interface SMSService {

    /**
     * 发送短信接口
     *
     * @param msmVo 短信实体
     * @return  boolean
     */
    boolean send(MsmVo msmVo);

    /**
     * 发送验证码的接口
     *
     * @param phone  手机号
     * @param code  验证码
     * @return  返回boolean
     */
    boolean send(String phone, String code);
}
