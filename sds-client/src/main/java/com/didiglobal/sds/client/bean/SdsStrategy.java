/**
 *
 */
package com.didiglobal.sds.client.bean;

/**
 * 降级策略对象
 *
 * @author manzhizhen
 * @version $Id: SdsStrategy.java, v 0.1 2016年4月17日 上午10:26:07 manzhizhen Exp $
 */
public class SdsStrategy {

    /**
     * 降级点，即对应的名称
     */
    private String point;

    /**
     * 访问量阈值，-1表示未生效，0表示彻底关闭流量（相当于开关）
     * 注意：只有全能降级点才会使用
     */
    private Long visitThreshold = -1L;

    /**
     * 2017-07-27 21:30:00'
     * 并发量阈值，-1表示未生效
     * 注意：只有全能降级点才会使用
     */
    private Integer concurrentThreshold = -1;

    /**
     * 异常量阈值，-1表示未生效
     * 注意：只有全能降级点才会使用
     */
    private Long exceptionThreshold = -1L;

    /**
     * 超时时间阈值，单位毫秒，-1表示未生效
     * 和{@link #timeoutCountThreshold} 配合使用
     */
    private Long timeoutThreshold = -1L;

    /**
     * 超时次数阈值，如果超过{@link #timeoutThreshold}的访问量达到该值，就应该降级，-1表示未生效
     */
    private Long timeoutCountThreshold = -1L;

    /**
     * 异常率阈值，取值为[0-100]，15表示异常率阈值为15%，该属性为异常率降级点所独有，-1表示不对异常率进行降级
     * 和{@link #exceptionRateStart} 配合使用
     */
    private Integer exceptionRateThreshold = -1;

    /**
     * 异常率降级判断的起点（标准），和{@link #exceptionRateThreshold}一起使用，访问量超过该值才开始计算异常率，避免采样失准
     * 和{@link #exceptionRateThreshold} 配合使用
     */
    private Long exceptionRateStart = 0L;

    /**
     * 令牌桶每个1秒能生成多少个令牌，-1表示不生效
     */
    private Integer tokenBucketGeneratedTokensInSecond = -1;

    /**
     * 令牌桶中桶最多能存储多少令牌，-1表示不生效
     */
    private Integer tokenBucketSize = -1;

    /**
     * 访问量降级、异常量降级、异常率降级中的降级延迟时间，表示降级的持续时间，单位毫秒，最小值为10000。
     */
    private Long delayTime = -1L;
    /**
     * 异常量降级特有的降级延迟期间重试时间周期，如果小于等于0表示不重试，单位毫秒，最小值为1000。
     */
    private Long retryInterval = -1L;

    /**
     * 降级率, 取值为[0-100]，值为15表示每100笔请求将有15笔被拒绝掉
     */
    private Integer downgradeRate = 0;

    /**
     * 返回值字符串，一般是json格式
     */
    private String returnValueStr = null;

    /**
     * 压测流量降级，0-压测流量对策略降级不产生影响，1-压测流量直接强制降级
     */
    private Integer pressureTestDowngrade = 0;

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public Long getVisitThreshold() {
        return visitThreshold;
    }

    public void setVisitThreshold(Long visitThreshold) {
        this.visitThreshold = visitThreshold;
    }

    public Integer getConcurrentThreshold() {
        return concurrentThreshold;
    }

    public void setConcurrentThreshold(Integer concurrentThreshold) {
        this.concurrentThreshold = concurrentThreshold;
    }

    public Long getExceptionThreshold() {
        return exceptionThreshold;
    }

    public void setExceptionThreshold(Long exceptionThreshold) {
        this.exceptionThreshold = exceptionThreshold;
    }

    public Long getTimeoutThreshold() {
        return timeoutThreshold;
    }

    public void setTimeoutThreshold(Long timeoutThreshold) {
        this.timeoutThreshold = timeoutThreshold;
    }

    public Long getTimeoutCountThreshold() {
        return timeoutCountThreshold;
    }

    public void setTimeoutCountThreshold(Long timeoutCountThreshold) {
        this.timeoutCountThreshold = timeoutCountThreshold;
    }

    public Integer getExceptionRateThreshold() {
        return exceptionRateThreshold;
    }

    public void setExceptionRateThreshold(Integer exceptionRateThreshold) {
        this.exceptionRateThreshold = exceptionRateThreshold;
    }

    public Long getExceptionRateStart() {
        return exceptionRateStart;
    }

    public void setExceptionRateStart(Long exceptionRateStart) {
        this.exceptionRateStart = exceptionRateStart;
    }

    public Integer getTokenBucketGeneratedTokensInSecond() {
        return tokenBucketGeneratedTokensInSecond;
    }

    public void setTokenBucketGeneratedTokensInSecond(Integer tokenBucketGeneratedTokensInSecond) {
        this.tokenBucketGeneratedTokensInSecond = tokenBucketGeneratedTokensInSecond;
    }

    public Integer getTokenBucketSize() {
        return tokenBucketSize;
    }

    public void setTokenBucketSize(Integer tokenBucketSize) {
        this.tokenBucketSize = tokenBucketSize;
    }

    public Long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Long delayTime) {
        this.delayTime = delayTime;
    }

    public Long getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(Long retryInterval) {
        this.retryInterval = retryInterval;
    }

    public Integer getDowngradeRate() {
        return downgradeRate;
    }

    public void setDowngradeRate(Integer downgradeRate) {
        this.downgradeRate = downgradeRate;
    }

    public String getReturnValueStr() {
        return returnValueStr;
    }

    public void setReturnValueStr(String returnValueStr) {
        this.returnValueStr = returnValueStr;
    }

    public Integer getPressureTestDowngrade() {
        return pressureTestDowngrade;
    }

    public void setPressureTestDowngrade(Integer pressureTestDowngrade) {
        this.pressureTestDowngrade = pressureTestDowngrade;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SdsStrategy{");
        sb.append("point='").append(point).append('\'');
        sb.append(", visitThreshold=").append(visitThreshold);
        sb.append(", concurrentThreshold=").append(concurrentThreshold);
        sb.append(", exceptionThreshold=").append(exceptionThreshold);
        sb.append(", timeoutThreshold=").append(timeoutThreshold);
        sb.append(", timeoutCountThreshold=").append(timeoutCountThreshold);
        sb.append(", exceptionRateThreshold=").append(exceptionRateThreshold);
        sb.append(", exceptionRateStart=").append(exceptionRateStart);
        sb.append(", tokenBucketGeneratedTokensInSecond=").append(tokenBucketGeneratedTokensInSecond);
        sb.append(", tokenBucketSize=").append(tokenBucketSize);
        sb.append(", delayTime=").append(delayTime);
        sb.append(", retryInterval=").append(retryInterval);
        sb.append(", downgradeRate=").append(downgradeRate);
        sb.append(", returnValueStr='").append(returnValueStr).append('\'');
        sb.append(", pressureTestDowngrade=").append(pressureTestDowngrade);
        sb.append('}');
        return sb.toString();
    }
}
