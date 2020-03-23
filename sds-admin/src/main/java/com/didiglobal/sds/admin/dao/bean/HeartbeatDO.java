package com.didiglobal.sds.admin.dao.bean;

import java.util.Date;

/**
 * 心跳信息DO
 * Created by manzhizhen on 18/7/1.
 */
public class HeartbeatDO {
    // ID
    private Long id;

    private String appGroupName;

    // 应用名称
    private String appName;

    private String point;

    // 降级数量
    private Long downgradeNum;
    // 单位时间内的访问量
    private Long visitNum;
    // 单位时间内的异常量
    private Long exceptionNum;
    // 单位时间内的超时量
    private Long timeoutNum;
    // 单位时间内的最大并发量
    private Integer maxConcurrentNum;
    private String appIp;
    // 统计周期的结束时间
    private Date statisticsCycleTime;
    private Date gmtCreate;
    private Date gmtModify;

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

    public Long getDowngradeNum() {
        return downgradeNum;
    }

    public void setDowngradeNum(Long downgradeNum) {
        this.downgradeNum = downgradeNum;
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

    public Long getTimeoutNum() {
        return timeoutNum;
    }

    public void setTimeoutNum(Long timeoutNum) {
        this.timeoutNum = timeoutNum;
    }

    public Integer getMaxConcurrentNum() {
        return maxConcurrentNum;
    }

    public void setMaxConcurrentNum(Integer maxConcurrentNum) {
        this.maxConcurrentNum = maxConcurrentNum;
    }

    public String getAppIp() {
        return appIp;
    }

    public void setAppIp(String appIp) {
        this.appIp = appIp;
    }

    public Date getStatisticsCycleTime() {
        return statisticsCycleTime;
    }

    public void setStatisticsCycleTime(Date statisticsCycleTime) {
        this.statisticsCycleTime = statisticsCycleTime;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HeartbeatDO{");
        sb.append("id=").append(id);
        sb.append(", appGroupName='").append(appGroupName).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", point='").append(point).append('\'');
        sb.append(", downgradeNum=").append(downgradeNum);
        sb.append(", visitNum=").append(visitNum);
        sb.append(", exceptionNum=").append(exceptionNum);
        sb.append(", timeoutNum=").append(timeoutNum);
        sb.append(", maxConcurrentNum=").append(maxConcurrentNum);
        sb.append(", appIp='").append(appIp).append('\'');
        sb.append(", statisticsCycleTime=").append(statisticsCycleTime);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", gmtModify=").append(gmtModify);
        sb.append('}');
        return sb.toString();
    }
}

