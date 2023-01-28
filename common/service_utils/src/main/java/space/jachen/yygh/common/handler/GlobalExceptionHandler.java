package space.jachen.yygh.common.handler;

/**
 * @author JaChen
 * @date 2023/1/28 11:07
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import space.jachen.yygh.common.result.JsonData;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一异常处理类
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonData<String> error(Exception e){
        log.error(e.getMessage());
        return JsonData.fail();
    }


    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public JsonData<String> error(ArithmeticException e){
        log.error(e.getMessage());
        return JsonData.fail("执行了特定异常");
    }


    @ExceptionHandler(YyghException.class)
    @ResponseBody
    public JsonData<Map<Integer, String>> error(YyghException e){
        log.error(e.getMessage());
        String msg = e.getMsg();
        Integer code = e.getCode();
        Map<Integer, String> map = new HashMap<>();
        map.put(code,msg);
        return JsonData.fail(map);
    }

}