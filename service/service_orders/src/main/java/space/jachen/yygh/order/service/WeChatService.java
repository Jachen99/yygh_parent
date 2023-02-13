package space.jachen.yygh.order.service;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/13 16:08
 */
public interface WeChatService {

    /***
     * 退款的接口
     *
     * @param outTradeNo 订单号
     * @return  Boolean
     */
    Boolean refund(String outTradeNo);

    /**
     * 根据订单号去微信第三方查询支付状态
     * 因为我们暂时没有域名解析 所以只能通过订单号再去微信支付查询一次支付状态。
     *
     * @param orderId  订单号
     * @return  Map<String,Object>
     */
    Map<String,String> queryPayStatus(Long orderId);

    /**
     * 根据订单号下单，生成支付链接
     *
     * @param orderId 订单id
     * @return Map<String,String>
     */
    Map<String,Object> createNative(Long orderId);
}
