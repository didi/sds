package com.didiglobal.sds.admin.controller.request;

import com.didiglobal.sds.admin.controller.bean.PageInfo;

/**
 * @Description: 监控大盘页面的查询请求
 * @Author: manzhizhen
 * @Date: Create in 2019-10-02 16:39
 */
public class DashboardPointRequest extends PageInfo {

    private String appGroupName;

    private String appName;

    private String point;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DashboardPointRequest{");
        sb.append("appGroupName='").append(appGroupName).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", point='").append(point).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
