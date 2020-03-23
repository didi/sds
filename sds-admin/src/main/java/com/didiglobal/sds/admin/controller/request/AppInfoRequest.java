package com.didiglobal.sds.admin.controller.request;

/**
 * app信息请求
 *
 * @author manzhizhen
 */
public class AppInfoRequest extends AbstractRequest {

    private String appGroupName;

    private String appName;

    private String newAppName;

    private String sdsSchemeName;

    private String newSdsSchemeName;

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

    public String getNewAppName() {
        return newAppName;
    }

    public void setNewAppName(String newAppName) {
        this.newAppName = newAppName;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AppInfoRequest{");
        sb.append("appGroupName='").append(appGroupName).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", newAppName='").append(newAppName).append('\'');
        sb.append(", sdsSchemeName='").append(sdsSchemeName).append('\'');
        sb.append(", newSdsSchemeName='").append(newSdsSchemeName).append('\'');
        sb.append(", operatorName='").append(operatorName).append('\'');
        sb.append(", operatorEmail='").append(operatorEmail).append('\'');
        sb.append(", creatorName='").append(creatorName).append('\'');
        sb.append(", creatorEmail='").append(creatorEmail).append('\'');
        sb.append(", page=").append(page);
        sb.append(", pageSize=").append(pageSize);
        sb.append('}');
        return sb.toString();
    }
}
