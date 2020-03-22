package com.didiglobal.sds.admin.dao.bean;

import java.util.Date;

/**
 * 降级预案实体
 * <p>
 * Created by manzhizhen on 18/9/1.
 */
public class SdsSchemeDO {

    private Long id;

    private String appGroupName;

    private String appName;

    /**
     * 降级预案名称
     */
    private String sdsSchemeName;

    private String operatorName;

    private String operatorEmail;

    private String creatorName;

    private String creatorEmail;

    private Date createTime;

    private Date modifiedTime;

    public SdsSchemeDO() {
    }

    public SdsSchemeDO(String appGroupName, String appName, String sdsSchemeName, String creatorName,
                           String creatorEmail) {
        this.appGroupName = appGroupName;
        this.appName = appName;
        this.sdsSchemeName = sdsSchemeName;
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
        final StringBuilder sb = new StringBuilder("SdsSchemeDO{");
        sb.append("id=").append(id);
        sb.append(", appGroupName='").append(appGroupName).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", sdsSchemeName='").append(sdsSchemeName).append('\'');
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
