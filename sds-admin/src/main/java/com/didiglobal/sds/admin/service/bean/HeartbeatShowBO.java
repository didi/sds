package com.didiglobal.sds.admin.service.bean;

import java.util.List;

public class HeartbeatShowBO {

    /**
     * 应用组名字
     */
    private String appGroupName;

    /**
     * 应用名称（冗余数据）
     */
    private String appName;

    /**
     * 降级点
     */
    private String point;

    private List<HeartbeatBO> heartbeatList;

    public HeartbeatShowBO() {
    }

    public HeartbeatShowBO(String appGroupName, String appName, String point, List<HeartbeatBO> heartbeatList) {
        this.appGroupName = appGroupName;
        this.appName = appName;
        this.point = point;
        this.heartbeatList = heartbeatList;
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

    public List<HeartbeatBO> getHeartbeatList() {
        return heartbeatList;
    }

    public void setHeartbeatList(List<HeartbeatBO> heartbeatList) {
        this.heartbeatList = heartbeatList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HeartbeatShowBO{");
        sb.append("appGroupName='").append(appGroupName).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", point='").append(point).append('\'');
        sb.append(", heartbeatList=").append(heartbeatList);
        sb.append('}');
        return sb.toString();
    }
}
