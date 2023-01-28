package space.jachen.yygh.common.result;

import lombok.Data;

@Data
public class JsonData<T> {
    //返回码
    private Integer code;

    //返回消息
    private String message;

    //返回数据
    private T data;

    public JsonData(){}

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

    public static<T> JsonData<T> ok(){
        return JsonData.ok(null);
    }

    /**
     * 操作成功
     * @param data  baseCategory1List
     * @param <T>
     * @return
     */
    public static<T> JsonData<T> ok(T data){
        return build(data, ResultCodeEnum.SUCCESS);
    }

    public static<T> JsonData<T> fail(){
        return JsonData.fail(null);
    }

    /**
     * 操作失败
     * @param data
     * @param <T>
     * @return
     */
    public static<T> JsonData<T> fail(T data){
        return build(data, ResultCodeEnum.FAIL);
    }

    public JsonData<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public JsonData<T> code(Integer code){
        this.setCode(code);
        return this;
    }
}
