package space.jachen.yygh.order.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import space.jachen.yygh.common.handler.YyghException;
import space.jachen.yygh.common.result.ResultCodeEnum;
import space.jachen.yygh.enums.PaymentTypeEnum;
import space.jachen.yygh.model.order.OrderInfo;
import space.jachen.yygh.order.service.OrderInfoService;
import space.jachen.yygh.order.service.PaymentInfoService;
import space.jachen.yygh.order.service.WeChatService;
import space.jachen.yygh.order.utils.ConstantPropertiesUtils;
import space.jachen.yygh.order.utils.HttpClient;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author JaChen
 * @date 2023/2/13 16:09
 */
@Service
@Slf4j
public class WeChatServiceImpl implements WeChatService {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private PaymentInfoService paymentInfoService;

    /**
     * 根据订单号去微信第三方查询支付状态
     * 因为我们暂时没有域名解析 所以只能通过订单号再去微信支付查询一次支付状态。
     *
     * @param orderId  订单号
     * @return  Map<String,Object>
     */
    @Override
    public Map<String, String> queryPayStatus(Long orderId) {
        try {
            OrderInfo orderInfo = orderInfoService.getById(orderId);
            // 封装参数
            Map<String, String> paramMap = new HashMap<String, String>(){{
                put("appid", ConstantPropertiesUtils.APPID);
                put("mch_id", ConstantPropertiesUtils.PARTNER);
                put("out_trade_no", orderInfo.getOutTradeNo());
                put("nonce_str", WXPayUtil.generateNonceStr());
            }};
            // 设置请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));
            client.setHttps(true);
            client.post();
            // 返回第三方的数据，转成Map
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            log.info("再次查询支付状态微信返回的信息："+resultMap);
            return resultMap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据订单号下单，生成支付链接
     *
     * @param orderId 订单id
     * @return Map<String,String>
     */
    @Override
    public Map<String, Object> createNative(Long orderId) {

        // 检查redis中是否存在该订单 如果有 直接返回
        Object obj = redisTemplate.opsForValue().get(orderId.toString());
        if (obj instanceof Map<?, ?>){
            return (Map<String, Object>)obj;
        }
        try {
            // 保存订单数据到payment_info表
            OrderInfo orderInfo = orderInfoService.getOrderDetailById(orderId);
            paymentInfoService.savePaymentInfo(orderInfo, PaymentTypeEnum.WEIXIN.getStatus());
            // 封装支付接口需要的数据
            HashMap<String, String> paramMap = new HashMap<String, String>() {{
                put("appid", ConstantPropertiesUtils.APPID);
                put("mch_id", ConstantPropertiesUtils.PARTNER);
                put("nonce_str", WXPayUtil.generateNonceStr());
                Date reserveDate = orderInfo.getReserveDate();
                String reserveDateString = new DateTime(reserveDate).toString("yyyy/MM/dd");
                String body = reserveDateString + "就诊" + orderInfo.getDepname();
                put("body", body);
                put("out_trade_no", orderInfo.getOutTradeNo());
                // paramMap.put("total_fee", order.getAmount().multiply(new BigDecimal("100")).longValue()+"");
                put("total_fee", "1");// 为了测试
                put("spbill_create_ip", "127.0.0.1");
                put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify");
                put("trade_type", "NATIVE");
            }};
            // 发送支付请求
            // HTTPClient来根据URL访问第三方接口并且传递参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            // client设置参数
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));
            client.setHttps(true);
            client.post();
            // 接收返回数据
            String XMLContent = client.getContent();
            Map<String, String> xmlToMap = WXPayUtil.xmlToMap(XMLContent);
            log.info("微信接口返回的xml数据："+xmlToMap);
            // 封装结果集
            HashMap<String, Object> resultMap = new HashMap<String, Object>() {{
                put("orderId", orderId);
                put("totalFee", orderInfo.getAmount());
                put("resultCode", xmlToMap.get("result_code"));
                put("codeUrl", xmlToMap.get("code_url"));
            }};
            // 微信支付二维码2小时过期，可采取2小时未支付取消订单
            if (null != resultMap.get("resultCode")) {
                redisTemplate.opsForValue().set(orderId.toString(), resultMap, 120, TimeUnit.MINUTES);
                log.info("此时redis里面的数据为："+redisTemplate.opsForValue().get(orderId.toString()));
            }
            return resultMap;
        } catch (Exception e) {
            throw new YyghException(ResultCodeEnum.SERVICE_ERROR.getCode(),"微信支付服务调用异常~");
        }
    }
}
