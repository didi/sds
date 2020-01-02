package com.didiglobal.sds.client.exception;

import com.didiglobal.sds.client.contant.ExceptionCode;

/**
 * Created by yizhenqiang on 17/5/1.
 */
public class SdsException extends RuntimeException {

    private String point;

    private Integer code;

    private String msg;

    public SdsException() {
        super();
    }

    public SdsException(String point, Integer code, String msg) {
        super(msg);

        this.point = point;
        this.code = code;
        this.msg = msg;
    }

    public SdsException(Integer code, String msg) {
        super(msg);

        this.code = code;
        this.msg = msg;
    }

    public SdsException(ExceptionCode downgrade) {
        super(downgrade.getMsg());

        this.code = downgrade.getCode();
        this.msg = downgrade.getMsg();
    }

    public SdsException(String point, ExceptionCode downgrade) {
        super(downgrade.getMsg());

        this.point = point;
        this.code = downgrade.getCode();
        this.msg = downgrade.getMsg();
    }



    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SdsException{");
        sb.append("point='").append(point).append('\'');
        sb.append(", code=").append(code);
        sb.append(", msg='").append(msg).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
