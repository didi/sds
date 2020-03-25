package com.didiglobal.sds.example.chapter2;

/**
 * <p>description : BaseResult
 *
 * @author : masteryourself
 * @version : 1.0.1
 * @date : 2020/3/25 10:09
 */
public class BaseResult<T> {

    private boolean success;
    private String message;
    private T data;

    public BaseResult() {
    }

    public BaseResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static <T> BaseResult<T> success(T data) {
        BaseResult<T> result = new BaseResult<>();
        result.setSuccess(true);
        result.setMessage("处理成功");
        result.setData(data);
        return result;
    }

    public static <T> BaseResult<T> error(String message) {
        BaseResult<T> result = new BaseResult<>();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
