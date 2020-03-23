/**
 *
 */
package com.didiglobal.sds.admin.dao.bean;

import java.util.Date;

/**
 * 应用组，同一个应用组下应用名称不能相同，不同的应用组下的应用名称可以相同
 * <p>
 * Created by manzhizhen on 17/7/26.
 */
public class AppGroupDO {

    private Long id;

    /**
     * 应用组名称
     */
    private String appGroupName;

    private String operatorName;

    private String operatorEmail;

    private String creatorName;

    private String creatorEmail;

    private Date createTime;

    private Date modifiedTime;

    public AppGroupDO() {
    }

    public AppGroupDO(String appGroupName, String creatorName, String creatorEmail) {
        this.appGroupName = appGroupName;
        this.creatorName = creatorName;
        this.creatorEmail = creatorEmail;
        this.operatorName = creatorName;
        this.operatorEmail = creatorEmail;
    }

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
        final StringBuilder sb = new StringBuilder("AppGroupDO{");
        sb.append("id=").append(id);
        sb.append(", appGroupName='").append(appGroupName).append('\'');
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
