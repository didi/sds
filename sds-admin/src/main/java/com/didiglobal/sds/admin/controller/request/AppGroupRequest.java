package com.didiglobal.sds.admin.controller.request;

/**
 * Created by manzhizhen on 18/9/29.
 */
public class AppGroupRequest extends AbstractRequest {

    private String appGroupName;

    private String newAppGroupName;

    public String getAppGroupName() {
        return appGroupName;
    }

    public void setAppGroupName(String appGroupName) {
        this.appGroupName = appGroupName;
    }

    public String getNewAppGroupName() {
        return newAppGroupName;
    }

    public void setNewAppGroupName(String newAppGroupName) {
        this.newAppGroupName = newAppGroupName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AppGroupRequest{");
        sb.append("appGroupName='").append(appGroupName).append('\'');
        sb.append(", newAppGroupName='").append(newAppGroupName).append('\'');
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
