package com.didiglobal.sds.client.bean;

/**
 * Created by manzhizhen on 17/3/5.
 */
public class SdsCycleInfo {

    /**
     * 降级点名称
     */
    private String point;

    /**
     * 周期访问量
     */
    private Long visitNum;

    /**
     * 周期异常数
     */
    private Long exceptionNum;

    /**
     * 周期最高并发量
     */
    private Integer concurrentNum;

    /**
     * 周期超时量
     */
    private Long timeoutNum;

    /**
     * 周期降级总量
     */
    private Long downgradeNum;

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public Long getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(Long visitNum) {
        this.visitNum = visitNum;
    }

    public Long getExceptionNum() {
        return exceptionNum;
    }

    public void setExceptionNum(Long exceptionNum) {
        this.exceptionNum = exceptionNum;
    }

    public Integer getConcurrentNum() {
        return concurrentNum;
    }

    public void setConcurrentNum(Integer concurrentNum) {
        this.concurrentNum = concurrentNum;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SdsCycleInfo{");
        sb.append("point='").append(point).append('\'');
        sb.append(", visitNum=").append(visitNum);
        sb.append(", exceptionNum=").append(exceptionNum);
        sb.append(", concurrentNum=").append(concurrentNum);
        sb.append(", timeoutNum=").append(timeoutNum);
        sb.append(", downgradeNum=").append(downgradeNum);
        sb.append('}');
        return sb.toString();
    }
}
