package com.didiglobal.sds.extension.dubbo.filter;

import com.didiglobal.sds.extension.dubbo.constant.SdsDubboConstants;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * Created by sea on 2020-03-28.
 */
@Activate(group = CommonConstants.PROVIDER, order = SdsDubboConstants.SDS_PROVIDER_FILTER_ORDER)
public class SdsProviderFilter extends AbstractSdsDubboFilter {

    @Override
    public String getPoint(Invoker<?> invoker, Invocation invocation) {
        return invoker.getInterface().getSimpleName() + "-" + invocation.getMethodName() + "-ProviderPoint";
    }
}
