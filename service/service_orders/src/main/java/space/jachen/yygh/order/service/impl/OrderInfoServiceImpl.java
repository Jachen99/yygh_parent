package space.jachen.yygh.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import space.jachen.yygh.model.order.OrderInfo;
import space.jachen.yygh.order.mapper.OrderInfoMapper;
import space.jachen.yygh.order.service.OrderInfoService;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author jachen
 * @since 2023-02-10
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Override
    public Long saveOrder(String scheduleId, Long patientId) {

        return null;
    }
}
