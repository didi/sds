package com.didiglobal.sds.admin.controller.response;

import com.didiglobal.sds.admin.controller.bean.DashboardPoint;

import java.util.List;

/**
 * @Description: 仪表盘的查询结果
 * @Author: manzhizhen
 * @Date: Create in 2019-09-13 17:13
 */
public class DashboardResponse {

    private String appGroupName;

    private String appName;

    private String point;

    private List<DashboardPoint> visitList;

    private List<DashboardPoint> concurrentList;

    private List<DashboardPoint> exceptionList;

    private List<DashboardPoint> timeoutList;

    private List<DashboardPoint> downgradeList;

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

    public List<DashboardPoint> getVisitList() {
        return visitList;
    }

    public void setVisitList(List<DashboardPoint> visitList) {
        this.visitList = visitList;
    }

    public List<DashboardPoint> getConcurrentList() {
        return concurrentList;
    }

    public void setConcurrentList(List<DashboardPoint> concurrentList) {
        this.concurrentList = concurrentList;
    }

    public List<DashboardPoint> getExceptionList() {
        return exceptionList;
    }

    public void setExceptionList(List<DashboardPoint> exceptionList) {
        this.exceptionList = exceptionList;
    }

    public List<DashboardPoint> getTimeoutList() {
        return timeoutList;
    }

    public void setTimeoutList(List<DashboardPoint> timeoutList) {
        this.timeoutList = timeoutList;
    }

    public List<DashboardPoint> getDowngradeList() {
        return downgradeList;
    }

    public void setDowngradeList(List<DashboardPoint> downgradeList) {
        this.downgradeList = downgradeList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DashboardResponse{");
        sb.append("appGroupName='").append(appGroupName).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", point='").append(point).append('\'');
        sb.append(", visitList=").append(visitList);
        sb.append(", concurrentList=").append(concurrentList);
        sb.append(", exceptionList=").append(exceptionList);
        sb.append(", timeoutList=").append(timeoutList);
        sb.append(", downgradeList=").append(downgradeList);
        sb.append('}');
        return sb.toString();
    }
}
