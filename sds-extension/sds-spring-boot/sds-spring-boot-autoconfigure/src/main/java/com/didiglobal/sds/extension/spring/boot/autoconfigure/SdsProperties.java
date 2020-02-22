package com.didiglobal.sds.extension.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 对应properties文件的：
 * sds.app.group.name, sds.app.name, sds.server.addr.list
 */
@ConfigurationProperties("sds")
public class SdsProperties {

    private String appGroupName;

    private String appName;

    private String serverAddrList;

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

    public String getServerAddrList() {
        return serverAddrList;
    }

    public void setServerAddrList(String serverAddrList) {
        this.serverAddrList = serverAddrList;
    }
}
