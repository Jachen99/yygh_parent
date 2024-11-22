package space.jachen.yygh.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import space.jachen.yygh.common.result.JsonData;
import space.jachen.yygh.common.result.ResultCodeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jachen
 * 全局异常处理器，统一管理和处理应用中的异常
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 通用异常处理
     *
     * @param e 捕获的异常
     * @return 通用失败响应
     */
    @ExceptionHandler(Exception.class)
    public JsonData<String> handleException(Exception e) {
        log.error("系统异常：{}", e.getMessage(), e);
        return JsonData.fail(ResultCodeEnum.SERVICE_ERROR.getMessage());
    }

    /**
     * 特定异常（ArithmeticException）处理
     *
     * @param e 算术异常
     * @return 指定消息的失败响应
     */
    @ExceptionHandler(ArithmeticException.class)
    public JsonData<String> handleArithmeticException(ArithmeticException e) {
        log.warn("算术异常：{}", e.getMessage(), e);
        return JsonData.fail("算术异常，请检查计算逻辑");
    }

    /**
     * 自定义异常处理
     *
     * @param e 自定义业务异常
     * @return 包含错误码和消息的响应
     */
    @ExceptionHandler(CustomException.class)
    public JsonData<Map<String, Object>> handleCustomException(CustomException e) {
        log.error("业务异常：{}, 状态码：{}", e.getMessage(), e.getCode(), e);

        // 构造返回数据
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("code", e.getCode());
        errorDetails.put("message", e.getMessage());

        return JsonData.fail(errorDetails);
    }
}
