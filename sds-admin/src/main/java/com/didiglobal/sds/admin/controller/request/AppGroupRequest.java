package com.didiglobal.sds.admin.controller.request;

import com.didiglobal.sds.admin.controller.bean.PageInfo;

/**
 * Created by yizhenqiang on 18/9/29.
 */
public class AppGroupRequest extends PageInfo {

    private String appGroupName;

    private String newAppGroupName;

    private String operatorName;

    private String operatorEmail;

    private String creatorName;

    private String creatorEmail;

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
