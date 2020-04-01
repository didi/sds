package com.didiglobal.sds.extension.dubbo;

import com.didiglobal.sds.client.CommonSdsClient;
import com.didiglobal.sds.extension.dubbo.constant.SdsDubboConstants;
import org.apache.dubbo.rpc.RpcContext;

/**
 * Created by sea on 2020-03-29.
 */
public class DubboSdsClient extends CommonSdsClient {

    public DubboSdsClient(String appGroupName, String appName, String serverAddrList) {
        super(appGroupName, appName, serverAddrList);
    }

    @Override
    protected void setDowngradeStartTime(Long time) {
        RpcContext.getContext().set(SdsDubboConstants.SDS_DOWNGRADE_START_TIME, time);
    }

    @Override
    protected Long getDowngradeStartTime() {
        return (Long) RpcContext.getContext().get(SdsDubboConstants.SDS_DOWNGRADE_START_TIME);
    }
}
