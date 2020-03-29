package com.didiglobal.sds.client.exception;

import com.didiglobal.sds.client.contant.ExceptionCode;

import static com.didiglobal.sds.client.contant.ExceptionCode.DOWNGRADE;

/**
 * 降级后抛的异常
 * <p>
 * Created by manzhizhen on 17/5/1.
 */
public class SdsException extends RuntimeException {

    private String point;

    private int code = DOWNGRADE.getCode();

    private String msg = DOWNGRADE.getMsg();

    public SdsException() {
        super(DOWNGRADE.getMsg());
    }

    public SdsException(String point, int code, String msg) {
        super(msg);

        this.point = point;
        this.code = code;
        this.msg = msg;
    }

    public SdsException(int code, String msg) {
        super(msg);

        this.code = code;
        this.msg = msg;
    }

    public SdsException(String point) {
        super(DOWNGRADE.getMsg());

        this.point = point;
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
