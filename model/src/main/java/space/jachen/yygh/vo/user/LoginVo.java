package space.jachen.yygh.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@ApiModel(description="登录对象")
@Builder
public class LoginVo {

    @ApiModelProperty(value = "openid")
    private String openid;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "验证码")
    private String code;

    @ApiModelProperty(value = "IP")
    private String ip;

    @Tolerate
    private LoginVo(){};
}
