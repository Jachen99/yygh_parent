package space.jachen.yygh.common.result;

import lombok.Data;

/**
 * @author jachen
 * 通用的 JSON 返回格式
 */
@Data
public class JsonData<T> {
    private Integer code; // 状态码
    private String message; // 消息内容
    private T data; // 数据内容

    // 构造私有化，避免直接实例化
    private JsonData() {}


     // 返回数据
    protected static <T> JsonData<T> build(T data) {
        JsonData<T> jsonData = new JsonData<T>();
        if (data != null)
            jsonData.setData(data);
        return jsonData;
    }

    public static <T> JsonData<T> build(T body, Integer code, String message) {
        JsonData<T> jsonData = build(body);
        jsonData.setCode(code);
        jsonData.setMessage(message);
        return jsonData;
    }

    public static <T> JsonData<T> build(T body, ResultCodeEnum resultCodeEnum) {
        JsonData<T> jsonData = build(body);
        jsonData.setCode(resultCodeEnum.getCode());
        jsonData.setMessage(resultCodeEnum.getMessage());
        return jsonData;
    }


     /**
     * 静态方法创建成功响应 kod=200 message=成功 data=null
     */
    public static <T> JsonData<T> ok() {
        JsonData<T> jsonData = new JsonData<>();
        jsonData.setCode(ResultCodeEnum.SUCCESS.getCode());
        jsonData.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        jsonData.setData(null);
        return jsonData;
    }


    /**
     * 静态方法创建成功响应
     */
    public static <T> JsonData<T> ok(T data) {
        JsonData<T> jsonData = new JsonData<>();
        jsonData.setCode(ResultCodeEnum.SUCCESS.getCode());
        jsonData.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        jsonData.setData(data);
        return jsonData;
    }

    /**
     * 静态方法创建失败响应
     */
    public static <T> JsonData<T> fail(String message) {
        JsonData<T> jsonData = new JsonData<>();
        jsonData.setCode(ResultCodeEnum.FAIL.getCode());
        jsonData.setMessage(message);
        return jsonData;
    }

    /**
     * 自定义失败响应
     */
    public static <T> JsonData<T> fail(T data) {
        JsonData<T> jsonData = new JsonData<>();
        jsonData.setCode(ResultCodeEnum.FAIL.getCode());
        jsonData.setMessage(ResultCodeEnum.FAIL.getMessage());
        jsonData.setData(data);
        return jsonData;
    }


        /**
     * 自定义失败响应
     */
    public static <T> JsonData<T> fail() {
        JsonData<T> jsonData = new JsonData<>();
        jsonData.setCode(ResultCodeEnum.FAIL.getCode());
        jsonData.setMessage(ResultCodeEnum.FAIL.getMessage());
        jsonData.setData(null);
        return jsonData;
    }
}
