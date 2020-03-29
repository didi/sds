/**
 *
 */
package com.didiglobal.sds.admin.service.bean;

import java.util.Date;

/**
 * <h2>心跳的记录</h2>
 * <p>
 * 如果每分钟一次心跳，保存7天，记录数将是千万级。
 * </p>
 *
 * Created by manzhizhen on 17/7/26.
 */
public class HeartbeatBO {

    /**
     * 周期内访问量
     */
    private Long visitNum = 0L;

    /**
     * 周期内最大并发量
     */
    private Integer maxConcurrentNum = 0;

    /**
     * 周期内异常量
     */
    private Long exceptionNum = 0L;

    /**
     * 周期内超时量
     */
    private Long timeoutNum = 0L;

    /**
     * 周期内被降级量
     */
    private Long downgradeNum = 0L;

    /**
     * 页面展示时间
     */
    private Date showTime;

    public Long getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(Long visitNum) {
        this.visitNum = visitNum;
    }

    public Integer getMaxConcurrentNum() {
        return maxConcurrentNum;
    }

    public void setMaxConcurrentNum(Integer maxConcurrentNum) {
        this.maxConcurrentNum = maxConcurrentNum;
    }

    public Long getExceptionNum() {
        return exceptionNum;
    }

    public void setExceptionNum(Long exceptionNum) {
        this.exceptionNum = exceptionNum;
    }

    public Long getTimeoutNum() {
        return timeoutNum;
    }

    public void setTimeoutNum(Long timeoutNum) {
        this.timeoutNum = timeoutNum;
    }

    public Long getDowngradeNum() {
        return downgradeNum;
    }

    public void setDowngradeNum(Long downgradeNum) {
        this.downgradeNum = downgradeNum;
    }

    public Date getShowTime() {
        return showTime;
    }

    public void setShowTime(Date showTime) {
        this.showTime = showTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HeartbeatBO{");
        sb.append("visitNum=").append(visitNum);
        sb.append(", maxConcurrentNum=").append(maxConcurrentNum);
        sb.append(", exceptionNum=").append(exceptionNum);
        sb.append(", timeoutNum=").append(timeoutNum);
        sb.append(", downgradeNum=").append(downgradeNum);
        sb.append(", showTime=").append(showTime);
        sb.append('}');
        return sb.toString();
    }
}
