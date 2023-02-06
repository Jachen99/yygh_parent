package space.jachen.yygh.model.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * UserInfo
 * </p>
 *
 * @author jachen
 */
@Data
@ApiModel(description = "UserInfo")
@TableName("user_info")
@Builder
public class UserInfo  {
	
	private static final long serialVersionUID = 1L;

	@Tolerate
	private UserInfo(){}

	@ApiModelProperty(value = "id")
	@TableId(type = IdType.AUTO)
	private Long id;

	@ApiModelProperty(value = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField("create_time")
	private Date createTime;

	@ApiModelProperty(value = "更新时间")
	@TableField("update_time")
	private Date updateTime;

	@ApiModelProperty(value = "逻辑删除(1:已删除，0:未删除)")
	//@TableLogic
	@TableField("is_deleted")
	private Integer isDeleted;

	@ApiModelProperty(value = "其他参数")
	@TableField(exist = false)
	private Map<String,Object> param = new HashMap<>();

	@ApiModelProperty(value = "微信openid")
	@TableField("openid")
	private String openid;

	@ApiModelProperty(value = "昵称")
	@TableField("nick_name")
	private String nickName;

	@ApiModelProperty(value = "手机号")
	@TableField("phone")
	private String phone;

	@ApiModelProperty(value = "用户姓名")
	@TableField("name")
	private String name;

	@ApiModelProperty(value = "证件类型")
	@TableField("certificates_type")
	private String certificatesType;

	@ApiModelProperty(value = "证件编号")
	@TableField("certificates_no")
	private String certificatesNo;

	@ApiModelProperty(value = "证件路径")
	@TableField("certificates_url")
	private String certificatesUrl;

	@ApiModelProperty(value = "认证状态（0：未认证 1：认证中 2：认证成功 -1：认证失败）")
	@TableField("auth_status")
	private Integer authStatus;

	@ApiModelProperty(value = "状态（0：锁定 1：正常）")
	@TableField("status")
	private Integer status;

}

