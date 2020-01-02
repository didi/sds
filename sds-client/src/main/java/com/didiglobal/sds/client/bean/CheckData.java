package com.didiglobal.sds.client.bean;

/**
 * 检查数据，用来做策略判断
 */
public class CheckData {

    private String point;

    /**
     * 当前时间
     */
    private Long time;

    /**
     * 当前滑动周期内访问量
     */
    private Long visitCount;

    /**
     * 当前秒访问量
     */
    private Long curSecondVisitCount;

    /**
     * 上一秒访问量
     */
    private Long lastSecondVisitCount;

    /**
     * 并发量获取结果
     */
    private Boolean concurrentAcquire = true;

    /**
     * 当前滑动周期内异常量
     */
    private Long exceptionCount;

    /**
     * 当前滑动周期内超时量
     */
    private Long timeoutCount;

    /**
     * 当前秒的令牌桶计数器
     */
    private Long takeTokenBucketNum;

    /**
     * 当前滑动周期内降级数量
     */
    private Long downgradeCount;

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Long visitCount) {
        this.visitCount = visitCount;
    }

    public Long getCurSecondVisitCount() {
        return curSecondVisitCount;
    }

    public void setCurSecondVisitCount(Long curSecondVisitCount) {
        this.curSecondVisitCount = curSecondVisitCount;
    }

    public Long getLastSecondVisitCount() {
        return lastSecondVisitCount;
    }

    public void setLastSecondVisitCount(Long lastSecondVisitCount) {
        this.lastSecondVisitCount = lastSecondVisitCount;
    }

    public Boolean getConcurrentAcquire() {
        return concurrentAcquire;
    }

    public void setConcurrentAcquire(Boolean concurrentAcquire) {
        this.concurrentAcquire = concurrentAcquire;
    }

    public Long getExceptionCount() {
        return exceptionCount;
    }

    public void setExceptionCount(Long exceptionCount) {
        this.exceptionCount = exceptionCount;
    }

    public Long getTimeoutCount() {
        return timeoutCount;
    }

    public void setTimeoutCount(Long timeoutCount) {
        this.timeoutCount = timeoutCount;
    }

    public Long getTakeTokenBucketNum() {
        return takeTokenBucketNum;
    }

    public void setTakeTokenBucketNum(Long takeTokenBucketNum) {
        this.takeTokenBucketNum = takeTokenBucketNum;
    }

    public Long getDowngradeCount() {
        return downgradeCount;
    }

    public void setDowngradeCount(Long downgradeCount) {
        this.downgradeCount = downgradeCount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CheckData{");
        sb.append("point='").append(point).append('\'');
        sb.append(", time=").append(time);
        sb.append(", visitCount=").append(visitCount);
        sb.append(", curSecondVisitCount=").append(curSecondVisitCount);
        sb.append(", lastSecondVisitCount=").append(lastSecondVisitCount);
        sb.append(", concurrentAcquire=").append(concurrentAcquire);
        sb.append(", exceptionCount=").append(exceptionCount);
        sb.append(", timeoutCount=").append(timeoutCount);
        sb.append(", takeTokenBucketNum=").append(takeTokenBucketNum);
        sb.append(", downgradeCount=").append(downgradeCount);
        sb.append('}');
        return sb.toString();
    }
}
