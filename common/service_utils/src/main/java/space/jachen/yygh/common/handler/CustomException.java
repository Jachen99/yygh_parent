package space.jachen.yygh.common.handler;

import lombok.Getter;
import lombok.Setter;
import space.jachen.yygh.common.result.ResultCodeEnum;

/**
 * @author jachen
 * 自定义业务异常类，用于表示应用中的特定错误场景
 */
@Getter
@Setter
public class CustomException extends RuntimeException {
    private final Integer code; // 状态码
    private String message; // 错误消息

    /**
     * 使用自定义状态码和错误消息构造异常
     *
     * @param code    错误状态码
     * @param message 错误消息
     */
    public CustomException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 使用枚举类型构造异常
     *
     * @param resultCodeEnum 结果枚举类型
     */
    public CustomException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }


    /**
     * 构造异常
     * @param e  异常
     */
    public CustomException(Exception e, Integer code, String message) {
        super(e.getMessage());
        this.code = code;
        this.message = message;
    }
}
