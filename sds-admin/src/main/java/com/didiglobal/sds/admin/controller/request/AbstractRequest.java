package com.didiglobal.sds.admin.controller.request;

import com.didiglobal.sds.admin.controller.bean.PageInfo;

/**
 * @Author: manzhizhen
 * @Date: Create in 2020-03-22 20:48
 */
public abstract class AbstractRequest extends PageInfo {

    protected String operatorName = "";

    protected String operatorEmail = "";

    protected String creatorName = "";

    protected String creatorEmail = "";

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
}
