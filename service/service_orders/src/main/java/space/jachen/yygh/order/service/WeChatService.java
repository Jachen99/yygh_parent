package space.jachen.yygh.order.service;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/13 16:08
 */
public interface WeChatService {

    /**
     * 根据订单号下单，生成支付链接
     *
     * @param orderId 订单id
     * @return Map<String,String>
     */
    Map<String,Object> createNative(Long orderId);
}
