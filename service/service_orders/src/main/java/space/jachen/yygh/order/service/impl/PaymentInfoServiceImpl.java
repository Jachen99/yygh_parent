package space.jachen.yygh.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import space.jachen.yygh.model.order.OrderInfo;
import space.jachen.yygh.model.order.PaymentInfo;
import space.jachen.yygh.order.mapper.PaymentInfoMapper;
import space.jachen.yygh.order.service.PaymentInfoService;

import java.util.Date;

/**
 * 微信支付服务接口 实现类
 * 被WeChatServiceImpl调用
 *
 * @author JaChen
 * @date 2023/2/13 14:16
 */
@Service
@Slf4j
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentInfoService {

    /**
     * 保存交易记录.
     * 在下单支付中，我们要在调用微信接口进行支付的同时，
     * 还要在本地数据库payment_info表中保存交易的数据
     *
     * @param orderInfo 订单对象
     * @param paymentType 支付类型（1：微信 2：支付宝）
     */
    @Override
    public void savePaymentInfo(OrderInfo orderInfo, Integer paymentType) {
        // 先查询一下数据库是否存在该订单信息 避免重复消费
        PaymentInfo paymentInfo = baseMapper.selectOne(
                new LambdaQueryWrapper<PaymentInfo>() {{
                    eq(PaymentInfo::getOutTradeNo, orderInfo.getOutTradeNo());
                }}
        );
        if (paymentInfo == null){
            // 拼接交易内容
            String subject = new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd")+"|"
                    +orderInfo.getHosname()+"|"+orderInfo.getDepname()+"|"+orderInfo.getTitle();
            // 保存交易记录
            PaymentInfo info = PaymentInfo.builder()
                    .orderId(orderInfo.getId())
                    .paymentType(paymentType)
                    .outTradeNo(orderInfo.getOutTradeNo()) // 交易号
                    .paymentStatus(orderInfo.getOrderStatus())
                    .subject(subject)
                    .totalAmount(orderInfo.getAmount()).build();
            info.setCreateTime(new Date());
            // 保存数据库
            log.info("点击支付后，保存数据库中的交易内容:"+info);
            baseMapper.insert(info);
        }
    }
}
