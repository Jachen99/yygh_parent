package space.jachen.yygh.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.jachen.yygh.hosp.HospFeignClient;
import space.jachen.yygh.model.order.OrderInfo;
import space.jachen.yygh.order.mapper.OrderInfoMapper;
import space.jachen.yygh.order.service.OrderInfoService;
import space.jachen.yygh.user.UserFeignClient;
import space.jachen.yygh.vo.hosp.ScheduleOrderVo;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author jachen
 * @since 2023-02-10
 */
@Service
@Slf4j
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private HospFeignClient hospFeignClient;

    @Override
    public Long saveOrder(String scheduleId, Long patientId) {

        ScheduleOrderVo scheduleOrderVo = hospFeignClient.getScheduleOrderVo(scheduleId);
        log.info("打印scheduleOrderVo:\n" + scheduleOrderVo);

        return 1L;
    }
}
