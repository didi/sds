package com.didiglobal.sds.admin.controller.request;

import com.didiglobal.sds.admin.controller.bean.PageInfo;

/**
 * 降级点返回值请求
 *
 * @author yizhenqiang
 */
public class PointReturnValueRequest extends PageInfo {

    private String appGroupName;

    private String appName;

    private String point;

    /**
     * 返回值字符串，一般是json格式
     */
    private String returnValueStr;

    /**
     * 本策略是否可用 0-关闭， 1-开启
     */
    private Integer status;

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

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getReturnValueStr() {
        return returnValueStr;
    }

    public void setReturnValueStr(String returnValueStr) {
        this.returnValueStr = returnValueStr;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PointReturnValueRequest{");
        sb.append("appGroupName='").append(appGroupName).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", point='").append(point).append('\'');
        sb.append(", returnValueStr='").append(returnValueStr).append('\'');
        sb.append(", status=").append(status);
        sb.append(", returnValueStr='").append(returnValueStr).append('\'');
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
