package space.jachen.yygh.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import space.jachen.yygh.vo.msm.MsmVo;

@Data
@Builder
@ApiModel(description = "OrderMqVo")
public class OrderMqVo {

	@Tolerate
	public OrderMqVo(){}
	@ApiModelProperty(value = "可预约数")
	private Integer reservedNumber;

	@ApiModelProperty(value = "剩余预约数")
	private Integer availableNumber;

	@ApiModelProperty(value = "排班id")
	private String scheduleId;

	@ApiModelProperty(value = "短信实体")
	private MsmVo msmVo;

}

