package com.didiglobal.sds.client.test;

/**
 * Created by yizhenqiang on 17/4/22.
 */
public class SecondDataShow {

    private int visit;
    private int concurrent;
    private int exception;
    private int exceptionRate;
    private int timeout;
    private int downgrade;

    public int getVisit() {
        return visit;
    }

    public void setVisit(int visit) {
        this.visit = visit;
    }

    public int getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(int concurrent) {
        this.concurrent = concurrent;
    }

    public int getException() {
        return exception;
    }

    public void setException(int exception) {
        this.exception = exception;
    }

    public int getExceptionRate() {
        return exceptionRate;
    }

    public void setExceptionRate(int exceptionRate) {
        this.exceptionRate = exceptionRate;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getDowngrade() {
        return downgrade;
    }

    public void setDowngrade(int downgrade) {
        this.downgrade = downgrade;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SecondDataShow{");
        sb.append("visit=").append(visit);
        sb.append(", concurrent=").append(concurrent);
        sb.append(", exception=").append(exception);
        sb.append(", exceptionRate=").append(exceptionRate);
        sb.append(", timeout=").append(timeout);
        sb.append(", downgrade=").append(downgrade);
        sb.append('}');
        return sb.toString();
    }
}
