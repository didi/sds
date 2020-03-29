package com.didiglobal.sds.admin.util;

/**
 * Created by manzhizhen on 17/6/28.
 */
public class HttpResponse {

    private String result;

    private int code;

    private Exception exception;

    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HttpResponse{");
        sb.append("result='").append(result).append('\'');
        sb.append(", code=").append(code);
        sb.append(", exception=").append(exception);
        sb.append('}');
        return sb.toString();
    }
}
