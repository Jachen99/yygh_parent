package space.jachen.yygh.model.hosp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import space.jachen.yygh.model.base.BaseMongoEntity;

/**
 * <p>
 * Department
 * </p>
 *
 * @author jachen
 */
@Data
@ApiModel(description = "Department")
@Document("Department")
@Builder
public class Department extends BaseMongoEntity {
	
	private static final long serialVersionUID = 1L;

	@Tolerate
	public Department(){}

	@ApiModelProperty(value = "医院编号")
	@Indexed //普通索引
	private String hoscode;

	@ApiModelProperty(value = "科室编号")
	@Indexed(unique = true) //唯一索引
	private String depcode;

	@ApiModelProperty(value = "科室名称")
	private String depname;

	@ApiModelProperty(value = "科室描述")
	private String intro;

	@ApiModelProperty(value = "大科室编号")
	private String bigcode;

	@ApiModelProperty(value = "大科室名称")
	private String bigname;

}

