package com.didiglobal.sds.admin.dao.bean;

import java.util.Date;

/**
 * 降级点降级策略实体
 * <p>
 * Created by manzhizhen on 17/7/26.
 */
public class PointStrategyDO {

    /**
     * id
     */
    private Long id;

    private String appGroupName;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 降级点，即对应的名称
     */
    private String point;

    private String sdsSchemeName;

    private String newSdsSchemeName;

    /**
     * 访问量阈值，-1表示未生效，0表示彻底关闭流量（相当于开关）
     * 注意：只有全能降级点才会使用
     */
    private Long visitThreshold = Long.valueOf(-1L);

    /**
     * 2017-07-27 21:30:00'
     * 并发量阈值，-1表示未生效
     * 注意：只有全能降级点才会使用
     */
    private Integer concurrentThreshold = Integer.valueOf(-1);

    /**
     * 异常量阈值，-1表示未生效
     * 注意：只有全能降级点才会使用
     */
    private Long exceptionThreshold = Long.valueOf(-1);

    /**
     * 超时阈值，单位毫秒，-1表示未生效
     * 和{@link #timeoutCountThreshold} 配合使用
     */
    private Long timeoutThreshold = Long.valueOf(-1);

    /**
     * 超时次数阈值，如果超过{@link #timeoutThreshold}的访问量达到该值，就应该降级，-1表示未生效
     */
    private Long timeoutCountThreshold = Long.valueOf(-1);

    /**
     * 异常率阈值，取值为[0-100]，15表示异常率阈值为15%，该属性为异常率降级点所独有，-1表示不对异常率进行降级
     * 和{@link #exceptionRateStart} 配合使用
     */
    private Integer exceptionRateThreshold = Integer.valueOf(-1);

    /**
     * 异常率降级判断的起点（标准），当访问量超过该值才开始计算异常率，避免采样失准
     * 和{@link #exceptionRateThreshold} 配合使用
     */
    private Long exceptionRateStart = Long.valueOf(0);

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
    private Long delayTime = Long.valueOf(-1);
    /**
     * 异常量降级特有的降级延迟期间重试时间周期，如果小于等于0表示不重试，单位毫秒，最小值为1000。
     */
    private Long retryInterval = Long.valueOf(-1);

    /**
     * 降级率, 取值为[0-100]，值为15表示每100笔请求将有15笔被拒绝掉
     */
    private Integer downgradeRate = Integer.valueOf(0);

    /**
     * 本策略是否可用 0-关闭， 1-开启
     */
    private Integer status;

    /**
     * 压测流量降级，0-压测流量对策略降级不产生影响，1-压测流量直接强制降级
     */
    private Integer pressureTestDowngrade = 0;

    private String operatorName;

    private String operatorEmail;

    private String creatorName;

    private String creatorEmail;

    private Date createTime;

    private Date modifiedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppGroupName() {
        return appGroupName;
    }

    public void setAppGroupName(String appGroupName) {
        this.appGroupName = appGroupName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getSdsSchemeName() {
        return sdsSchemeName;
    }

    public void setSdsSchemeName(String sdsSchemeName) {
        this.sdsSchemeName = sdsSchemeName;
    }

    public String getNewSdsSchemeName() {
        return newSdsSchemeName;
    }

    public void setNewSdsSchemeName(String newSdsSchemeName) {
        this.newSdsSchemeName = newSdsSchemeName;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPressureTestDowngrade() {
        return pressureTestDowngrade;
    }

    public void setPressureTestDowngrade(Integer pressureTestDowngrade) {
        this.pressureTestDowngrade = pressureTestDowngrade;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorEmail() {
        return operatorEmail;
    }

    public void setOperatorEmail(String operatorEmail) {
        this.operatorEmail = operatorEmail;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PointStrategyDO{");
        sb.append("id=").append(id);
        sb.append(", appGroupName='").append(appGroupName).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", point='").append(point).append('\'');
        sb.append(", sdsSchemeName='").append(sdsSchemeName).append('\'');
        sb.append(", newSdsSchemeName='").append(newSdsSchemeName).append('\'');
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
        sb.append(", status=").append(status);
        sb.append(", pressureTestDowngrade=").append(pressureTestDowngrade);
        sb.append(", operatorName='").append(operatorName).append('\'');
        sb.append(", operatorEmail='").append(operatorEmail).append('\'');
        sb.append(", creatorName='").append(creatorName).append('\'');
        sb.append(", creatorEmail='").append(creatorEmail).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append(", modifiedTime=").append(modifiedTime);
        sb.append('}');
        return sb.toString();
    }
}
