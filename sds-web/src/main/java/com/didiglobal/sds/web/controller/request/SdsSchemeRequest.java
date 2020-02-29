package com.didiglobal.sds.web.controller.request;

import com.didiglobal.sds.web.controller.bean.PageInfo;

/**
 * Created by yizhenqiang on 18/9/29.
 */
public class SdsSchemeRequest extends PageInfo {

    private String appGroupName;

    private String appName;

    private String sdsSchemeName;

    private String newSdsSchemeName;

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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
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
        final StringBuilder sb = new StringBuilder("SdsSchemeRequest{");
        sb.append("appGroupName='").append(appGroupName).append('\'');
        sb.append(", appName='").append(appName).append('\'');
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
