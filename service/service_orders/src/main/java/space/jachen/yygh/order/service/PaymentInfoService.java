package space.jachen.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.jachen.yygh.model.order.OrderInfo;
import space.jachen.yygh.model.order.PaymentInfo;

/**
 * @author JaChen
 * @date 2023/2/13 14:15
 */
public interface PaymentInfoService extends IService<PaymentInfo> {

    /**
     * 保存交易记录
     *
     * @param order 订单对象
     * @param paymentType 支付类型（1：微信 2：支付宝）
     */
    void savePaymentInfo(OrderInfo order, Integer paymentType);
}
