package space.jachen.yygh.sms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.common.utils.RandomUtil;
import space.jachen.yygh.sms.service.SMSService;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author JaChen
 * @date 2023/2/6 23:57
 */
@Api(tags = "发送短信接口")
@RestController
@RequestMapping("/yygh/sms")
public class SMSController {

    @Resource
    private SMSService service;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @ApiOperation("发送验证码")
    @GetMapping(value = "/send/{phone}")
    public JsonData<String> code(@PathVariable String phone) {
        //获取Redis中的验证码
        String code = redisTemplate.opsForValue().get(phone);
        //在有效期内，验证码不能重复发送
        if(!StringUtils.isEmpty(code)) return JsonData.ok();
        //生成验证码
        code = RandomUtil.getFourBitRandom();
        //调用方法发送
        boolean isSend = service.send(phone, code);
        if(isSend) {
            //发送成功，验证码放到redis，设置有效时间
            redisTemplate.opsForValue().set(phone, code,5, TimeUnit.MINUTES);
            return JsonData.ok();
        } else {
            return JsonData.fail("发送短信失败");
        }
    }
}