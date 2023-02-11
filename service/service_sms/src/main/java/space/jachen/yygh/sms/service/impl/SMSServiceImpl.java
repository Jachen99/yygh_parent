package space.jachen.yygh.sms.service.impl;

import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import space.jachen.yygh.common.utils.RandomUtil;
import space.jachen.yygh.sms.service.SMSService;
import space.jachen.yygh.sms.utils.HttpUtils;
import space.jachen.yygh.vo.msm.MsmVo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/6 23:50
 */
@Service
public class SMSServiceImpl implements SMSService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 发送短信接口实现方法,这里为了测试，我们调用的是之前发送验证码的方法即可。
     *
     * @param msmVo 短信实体
     * @return  boolean
     */
    @Override
    public boolean send(MsmVo msmVo) {
        if(!StringUtils.isEmpty(msmVo.getPhone())) {
            // String code = (String)msmVo.getParam().get("code");
            // 仅为了测试
            String code = RandomUtil.getFourBitRandom();
            // 调用当前类中发送短信的方法
            return this.send(msmVo.getPhone(),code);
        }
        return false;
    }

    /**
     * 发送验证码的接口
     *
     * @param phone  手机号
     * @param code  验证码
     * @return  返回boolean
     */
    @Override
    public boolean send(String phone, String code) {

        // 先在redis查询该用户是否存在验证码
        String redisCode = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(redisCode)){
            return true;
        }
        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "POST";
        String appcode = "496d5e490b234e4d98196bfbf9e53dbc";
        Map<String, String> headers = new HashMap<>();
        // 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        // 根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();
        bodys.put("content", "code:" + code);
        bodys.put("phone_number", phone);
        bodys.put("template_id", "TPL_0000");
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
