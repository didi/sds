package com.didiglobal.sds.admin.dao.bean;

import java.util.Date;

/**
 * 降级后的返回值
 *
 * @auther manzhizhen
 * @date 2019/4/21
 */
public class PointReturnValueDO {

    private Long id;

    private String appGroupName;

    /**
     * 应用名称
     */
    private String appName;

    private String point;

    /**
     * 返回值字符串，一般是json格式
     */
    private String returnValueStr;

    /**
     * 本降级点返回值是否生效 0-关闭， 1-开启
     */
    private Integer status;

    private String operatorName;

    private String operatorEmail;

    private String creatorName;

    private String creatorEmail;

    private Date createTime;

    private Date modifiedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PointReturnValueDO{");
        sb.append("id=").append(id);
        sb.append(", appGroupName='").append(appGroupName).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", point='").append(point).append('\'');
        sb.append(", returnValueStr='").append(returnValueStr).append('\'');
        sb.append(", status=").append(status);
        sb.append(", operatorName='").append(operatorName).append('\'');
        sb.append(", operatorEmail='").append(operatorEmail).append('\'');
        sb.append(", creatorName='").append(creatorName).append('\'');
        sb.append(", creatorEmail='").append(creatorEmail).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append(", modifiedTime=").append(modifiedTime);
        sb.append('}');
        return sb.toString();
    }
}
