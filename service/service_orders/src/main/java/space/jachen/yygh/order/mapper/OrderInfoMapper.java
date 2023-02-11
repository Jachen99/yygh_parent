package space.jachen.yygh.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import space.jachen.yygh.model.order.OrderInfo;

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

}
