package space.jachen.yygh.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import space.jachen.yygh.enums.RefundStatusEnum;
import space.jachen.yygh.model.order.PaymentInfo;
import space.jachen.yygh.model.order.RefundInfo;
import space.jachen.yygh.order.mapper.RefundInfoMapper;
import space.jachen.yygh.order.service.RefundInfoService;

/**
 * 退款接口的实现类
 *
 * @author JaChen
 * @date 2023/2/13 22:44
 */
@Service
@Slf4j
public class RefundInfoServiceImpl extends ServiceImpl<RefundInfoMapper, RefundInfo> implements RefundInfoService{

    /**
     * 保存退款记录
     *
     * @param paymentInfo  支付记录
     */
    @Override
    public RefundInfo saveRefundInfo(PaymentInfo paymentInfo) {
        RefundInfo refundInfo = baseMapper.selectOne(
                new LambdaQueryWrapper<RefundInfo>() {{
                    eq(RefundInfo::getOutTradeNo, paymentInfo.getOutTradeNo());
                }}
        );
        if (refundInfo != null)
            return refundInfo;
        RefundInfo info = new RefundInfo();
        BeanUtils.copyProperties(paymentInfo,info);
        info.setRefundStatus(RefundStatusEnum.UNREFUND.getStatus());
        log.info("打印退款前paymentInfo表拷贝到RefundInfo表中的数据{}",info);
        baseMapper.insert(info);
        return info;
    }
}
