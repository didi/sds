package com.didiglobal.sds.admin.controller.request;

/**
 * 降级点返回值请求
 *
 * @author manzhizhen
 */
public class PointReturnValueRequest extends AbstractRequest {

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
