package com.didiglobal.sds.admin.controller.response;

import com.didiglobal.sds.admin.constants.SdsCode;

public class SdsResponse<T> {

    private Integer code;

    private String msg;

    private T data;

    public SdsResponse() {
    }

    public SdsResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public SdsResponse(T data) {
        this.code = SdsCode.SUCCESS.getCode();
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SdsResponse{");
        sb.append("code=").append(code);
        sb.append(", msg='").append(msg).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
