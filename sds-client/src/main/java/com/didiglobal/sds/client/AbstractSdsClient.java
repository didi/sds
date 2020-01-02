package com.didiglobal.sds.client;

public abstract class AbstractSdsClient implements SdsClient {

    protected String appGroupName;

    protected String appName;

    protected String serverAddrList;

    public AbstractSdsClient(String appGroupName, String appName, String serverAddrList) {
        this.appGroupName = appGroupName;
        this.appName = appName;
        this.serverAddrList = serverAddrList;
    }

    public String getAppGroupName() {
        return appGroupName;
    }

    public String getAppName() {
        return appName;
    }

    public String getServerAddrList() {
        return serverAddrList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AbstractSdsClient{");
        sb.append("appGroupName='").append(appGroupName).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", serverAddrList='").append(serverAddrList).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
