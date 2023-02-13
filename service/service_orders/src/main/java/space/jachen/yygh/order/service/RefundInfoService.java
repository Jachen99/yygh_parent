package space.jachen.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.jachen.yygh.model.order.PaymentInfo;
import space.jachen.yygh.model.order.RefundInfo;

/**
 * @author JaChen
 * @date 2023/2/13 22:43
 */
public interface RefundInfoService extends IService<RefundInfo> {

    /**
     * 保存退款记录
     *
     * @param paymentInfo  支付记录
     */
    RefundInfo saveRefundInfo(PaymentInfo paymentInfo);
}
