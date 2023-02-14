package space.jachen.yygh.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import space.jachen.yygh.model.order.OrderInfo;
import space.jachen.yygh.vo.order.OrderQueryVo;

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

    /**
     * 获取分页后的订单数据
     *
     * @param orderInfoPage  Page订单对象
     * @param orderQueryVo  OrderQueryVo
     * @return Page<OrderInfo>
     */
    Page<OrderInfo> getPageList(Page<OrderInfo> orderInfoPage, OrderQueryVo orderQueryVo);

    /**
     * 获取订单详情
     *
     * @param orderId 订单id
     * @return OrderInfo
     */
    OrderInfo getOrderDetailById(Long orderId);

    /**
     *
     * @param orderId
     * @return
     */
    Boolean cancelOrder(Long orderId);
}
