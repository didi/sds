/**
 *
 */
package com.didiglobal.sds.client.bean;

import java.util.List;


/**
 * 服务端应答对象
 *
 * @author manzhizhen
 * @version $Id: HeartBeatResponse.java, v 0.1 2016年2月20日 下午9:37:55 Administrator Exp $
 */
public class HeartBeatResponse {

    private String sdsSchemeName;

    private Boolean changed;

    private Long version;

    private List<SdsStrategy> strategies;

    private String errorMsg;

    public HeartBeatResponse() {
    }

    public HeartBeatResponse(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public HeartBeatResponse(String sdsSchemeName, Boolean changed, Long version, List<SdsStrategy> strategies) {
        this.sdsSchemeName = sdsSchemeName;
        this.changed = changed;
        this.version = version;
        this.strategies = strategies;
    }

    public String getSdsSchemeName() {
        return sdsSchemeName;
    }

    public void setSdsSchemeName(String sdsSchemeName) {
        this.sdsSchemeName = sdsSchemeName;
    }

    public Boolean isChanged() {
        return changed;
    }

    public void setChanged(Boolean changed) {
        this.changed = changed;
    }

    public List<SdsStrategy> getStrategies() {
        return strategies;
    }

    public void setStrategies(List<SdsStrategy> strategies) {
        this.strategies = strategies;
    }

    public Boolean getChanged() {
        return changed;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HeartBeatResponse{");
        sb.append("sdsSchemeName='").append(sdsSchemeName).append('\'');
        sb.append(", changed=").append(changed);
        sb.append(", version=").append(version);
        sb.append(", strategies=").append(strategies);
        sb.append(", errorMsg='").append(errorMsg).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
