package space.jachen.yygh.common.handler;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author jachen
 * 自定义异常类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class YyghException extends CustomException  {
    /**
     * 状态码
     */
    @ApiModelProperty(value = "状态码")
    private Integer code;

    /**
     * 错误消息
     */
    private String msg;

    /**
     * 构造方法（包含异常信息）
     * @param code 状态码
     * @param msg 错误消息
     * @param e 异常对象
     */
    public YyghException(Integer code, String msg, Exception e) {
        super(e, code, msg);
        this.code = code;
        this.msg = msg;
    }


        /**
     * 构造方法（包含异常信息）
     * @param code 状态码
     * @param msg 错误消息
     */
    public YyghException(Integer code, String msg) {
        super(code, msg);
        this.code = code;
        this.msg = msg;
    }
}
