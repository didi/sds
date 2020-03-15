package com.didiglobal.sds.admin.controller.bean;

import java.io.Serializable;

/**
 * Created by tianyulei on 2019/8/11
 **/
public class PointDictVO implements Serializable {
    private static final long serialVersionUID = 1735890133713939995L;

    /**
     * 应用组名称
     */
    private String appGroupName;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 降级点，即对应的名称
     */
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
        final StringBuilder sb = new StringBuilder("PointDictVO{");
        sb.append("appGroupName='").append(appGroupName).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", point='").append(point).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
