package com.didiglobal.sds.client.contant;

/**
 * Created by yizhenqiang on 17/5/1.
 */
public enum ExceptionCode {
    DOWNGRADE(300000, "此请求已经被降级");


    private Integer code;
    private String msg;

    ExceptionCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
