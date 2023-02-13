package space.jachen.yygh.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.jachen.yygh.enums.OrderStatusEnum;
import space.jachen.yygh.enums.PaymentStatusEnum;
import space.jachen.yygh.model.order.OrderInfo;
import space.jachen.yygh.model.order.PaymentInfo;
import space.jachen.yygh.order.mapper.PaymentInfoMapper;
import space.jachen.yygh.order.service.OrderInfoService;
import space.jachen.yygh.order.service.PaymentInfoService;

import java.util.Date;
import java.util.Map;

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

    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * 获取支付记录
     *
     * @param outTradeNo 交易号
     * @param paymentType  支付类型 微信 支付宝
     * @return  PaymentInfo
     */
    @Override
    public PaymentInfo getPaymentInfo(String outTradeNo, Integer paymentType) {
        return baseMapper.selectOne(
                new LambdaQueryWrapper<PaymentInfo>(){{
                    eq(PaymentInfo::getOutTradeNo,outTradeNo);
                    eq(PaymentInfo::getPaymentType,paymentType);
                }}
        );
    }

    /**
     * 更新支付状态
     *
     * @param outTradeNo 交易号
     * @param paymentType 支付类型 微信 支付宝
     * @param paramMap 调用微信查询支付状态接口返回map集合
     */
    @Override
    public void paySuccess(String outTradeNo, Integer paymentType, Map<String, String> paramMap) {
        // 更新订单状态
        OrderInfo orderInfo = orderInfoService.getOne(
                new LambdaQueryWrapper<OrderInfo>() {{
                    eq(OrderInfo::getOutTradeNo, outTradeNo);
                }}
        );
        orderInfo.setOrderStatus(OrderStatusEnum.PAID.getStatus());
        log.info("支付成功后更新订单状态的数据："+orderInfo);
        orderInfoService.updateById(orderInfo);
        // 更新支付记录状态
        PaymentInfo paymentInfo = baseMapper.selectOne(
                new LambdaQueryWrapper<PaymentInfo>() {{
                    eq(PaymentInfo::getOutTradeNo, outTradeNo);
                }}
        );
        paymentInfo.setPaymentStatus(PaymentStatusEnum.PAID.getStatus());
        paymentInfo.setTradeNo(paramMap.get("transaction_id"));
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setCallbackContent(paramMap.toString());
        log.info("支付成功后更新支付记录的数据："+paymentInfo);
        baseMapper.updateById(paymentInfo);
    }

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
