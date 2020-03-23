package com.didiglobal.sds.admin.service.bean;

import com.didiglobal.sds.admin.controller.bean.DashboardPoint;

import java.util.List;

/**
 * @Description: 仪表盘主要显示的信息
 * @Author: manzhizhen
 * @Date: Create in 2019-10-01 11:18
 */
public class DashboardMainBO {

    private List<DashboardPoint> visitList;

    private List<DashboardPoint> concurrentList;

    private List<DashboardPoint> exceptionList;

    private List<DashboardPoint> timeoutList;

    private List<DashboardPoint> downgradeList;

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
        final StringBuilder sb = new StringBuilder("DashboardMainBO{");
        sb.append("visitList=").append(visitList);
        sb.append(", concurrentList=").append(concurrentList);
        sb.append(", exceptionList=").append(exceptionList);
        sb.append(", timeoutList=").append(timeoutList);
        sb.append(", downgradeList=").append(downgradeList);
        sb.append('}');
        return sb.toString();
    }
}
