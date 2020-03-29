package com.didiglobal.sds.client.contant;

/**
 * Created by manzhizhen on 17/5/1.
 */
public enum ExceptionCode {
    DOWNGRADE(1, "此请求已经被降级");


    private int code;
    private String msg;

    ExceptionCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
