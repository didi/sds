package com.didiglobal.sds.web.controller.request;

import java.io.Serializable;

/**
 * Created by tianyulei on 2019/8/11
 **/
public class PointDictRequest implements Serializable {
    private static final long serialVersionUID = -6263204439209038271L;

    private String appGroupName;

    private String appName;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PointDictRequest{");
        sb.append("appGroupName='").append(appGroupName).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
