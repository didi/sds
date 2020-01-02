package com.didiglobal.sds.web.controller.request;

import com.didiglobal.sds.web.controller.bean.PageInfo;

/**
 * app信息请求
 *
 * @author yizhenqiang
 */
public class AppInfoRequest extends PageInfo {

    private String appGroupName;

    private String appName;

    private String newAppName;

    private String strategyGroupName;

    private String newStrategyGroupName;

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

    public String getNewAppName() {
        return newAppName;
    }

    public void setNewAppName(String newAppName) {
        this.newAppName = newAppName;
    }

    public String getStrategyGroupName() {
        return strategyGroupName;
    }

    public void setStrategyGroupName(String strategyGroupName) {
        this.strategyGroupName = strategyGroupName;
    }

    public String getNewStrategyGroupName() {
        return newStrategyGroupName;
    }

    public void setNewStrategyGroupName(String newStrategyGroupName) {
        this.newStrategyGroupName = newStrategyGroupName;
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
        final StringBuilder sb = new StringBuilder("AppInfoRequest{");
        sb.append("appGroupName='").append(appGroupName).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", newAppName='").append(newAppName).append('\'');
        sb.append(", strategyGroupName='").append(strategyGroupName).append('\'');
        sb.append(", newStrategyGroupName='").append(newStrategyGroupName).append('\'');
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
