package space.jachen.yygh.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import space.jachen.yygh.model.order.OrderInfo;
import space.jachen.yygh.vo.order.OrderCountQueryVo;
import space.jachen.yygh.vo.order.OrderCountVo;

import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author jachen
 * @since 2023-02-10
 */
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    // 统计每天平台预约数据
    List<OrderCountVo> selectOrderCount(OrderCountQueryVo orderCountQueryVo);
}
