package space.jachen.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.jachen.yygh.model.order.OrderInfo;
import space.jachen.yygh.model.order.PaymentInfo;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/2/13 14:15
 */
public interface PaymentInfoService extends IService<PaymentInfo> {

    /**
     * 更新支付状态
     *
     * @param outTradeNo 交易号
     * @param paymentType 支付类型 微信 支付宝
     * @param paramMap 调用微信查询支付状态接口返回map集合
     */
    void paySuccess(String outTradeNo, Integer paymentType, Map<String, String> paramMap);

    /**
     * 保存交易记录
     *
     * @param order 订单对象
     * @param paymentType 支付类型（1：微信 2：支付宝）
     */
    void savePaymentInfo(OrderInfo order, Integer paymentType);
}
