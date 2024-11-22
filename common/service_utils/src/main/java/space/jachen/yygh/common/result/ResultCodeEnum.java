package space.jachen.yygh.common.result;

import lombok.Getter;

/**
 * @author JaChen
 * 通用状态码枚举类
 */
@Getter
public enum ResultCodeEnum {
    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    SERVICE_ERROR(2012, "服务异常"),
    DATA_ERROR(204, "数据异常"),
    ILLEGAL_REQUEST(205, "非法请求"),
    REPEAT_SUBMIT(206, "重复提交"),
    ARGUMENT_VALID_ERROR(210, "参数校验异常"),
    LOGIN_AUTH(208, "未登陆"),
    PERMISSION(209, "没有权限"),
    ACCOUNT_ERROR(214, "账号不正确"),
    PASSWORD_ERROR(215, "密码不正确"),
    LOGIN_MOBILE_ERROR(216, "登录手机号码错误"),
    ACCOUNT_STOP(217, "账号已停用"),
    NODE_ERROR(218, "该节点下有子节点，不可以删除"),
    SIGN_REQUEST(219, "验签失败");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 消息内容
     */
    private final String message;

    /**
     * 构造方法
     * @param code 状态码
     * @param message 消息内容
     */
    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
