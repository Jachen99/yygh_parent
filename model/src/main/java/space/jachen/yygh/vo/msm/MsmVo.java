package space.jachen.yygh.vo.msm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.Map;

@Data
@Builder
@ApiModel(description = "短信实体")
public class MsmVo {

    @Tolerate
    public MsmVo(){}

    @ApiModelProperty(value = "phone")
    private String phone;

    @ApiModelProperty(value = "短信模板code")
    private String templateCode;

    @ApiModelProperty(value = "短信模板参数")
    private Map<String,Object> param;
}
