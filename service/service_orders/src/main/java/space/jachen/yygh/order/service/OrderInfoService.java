package space.jachen.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.jachen.yygh.model.order.OrderInfo;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author jachen
 * @since 2023-02-10
 */
public interface OrderInfoService extends IService<OrderInfo> {

    /**
     * 生成订单
     *
     * @param scheduleId  排班id
     * @param patientId  就诊人id
     * @return  返回订单id
     */
    Long saveOrder(String scheduleId, Long patientId);
}
