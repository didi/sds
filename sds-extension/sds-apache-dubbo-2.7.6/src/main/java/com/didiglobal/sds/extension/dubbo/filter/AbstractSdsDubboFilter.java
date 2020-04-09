package com.didiglobal.sds.extension.dubbo.filter;

import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.client.service.SdsDowngradeReturnValueService;
import com.didiglobal.sds.extension.dubbo.DubboSdsClientFactory;
import com.didiglobal.sds.extension.dubbo.constant.SdsDubboConstants;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

/**
 * Created by sea on 2020-03-28.
 */
public abstract class AbstractSdsDubboFilter implements Filter, Filter.Listener {

    protected static Logger logger = SdsLoggerFactory.getDefaultLogger();

    SdsClient sdsClient;

    //for spring inject
    public void setSdsClient(SdsClient sdsClient) {
        this.sdsClient = sdsClient;
    }

    public abstract String getPoint(Invoker<?> invoker, Invocation invocation);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (sdsClient == null) {
            logger.info("init sdsClient by java system properties");
            sdsClient = DubboSdsClientFactory.INSTANCE.getDubboSdsClient();
        }

        if (sdsClient == null) {
            logger.warn("{}#invoke SdsClientFactory.getSdsClient() null, please check SdsClient is initialized ", getClass().getSimpleName());
            return invoker.invoke(invocation);
        }

        String point = getPoint(invoker, invocation);
        RpcContext.getContext().set(SdsDubboConstants.SDS_DUBBO_POINT, point);

        try {
            if (sdsClient.shouldDowngrade(point)) {
                // 优先使用返回值
                Method method = invoker.getInterface().getMethod(invocation.getMethodName(),
                        invocation.getParameterTypes());

                Object returnValue;
                if (CompletableFuture.class.isAssignableFrom(method.getReturnType())) {
                    String content = SdsDowngradeReturnValueService.getInstance().getPointDowngradeReturnValueStr(point);
                    returnValue = CompletableFuture.completedFuture(content);
                } else {
                    returnValue = SdsDowngradeReturnValueService.getInstance().getDowngradeReturnValue(point,
                            method.getGenericReturnType());
                }

                return AsyncRpcResult.newDefaultAsyncResult(returnValue, invocation);
            }
        } catch (Throwable e) {
            logger.warn("{}#invoke sds error occur", getClass().getSimpleName(), e);
        }

        return invoker.invoke(invocation);
    }

    @Override
    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
        onComplete(appResponse.getException(), invoker, invocation);
    }

    @Override
    public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {
        onComplete(t, invoker, invocation);
    }

    void onComplete(Throwable t, Invoker<?> invoker, Invocation invocation) {
        SdsClient sdsClient = getSdsClient();
        String point = (String) RpcContext.getContext().get(SdsDubboConstants.SDS_DUBBO_POINT);

        if (sdsClient == null || point == null) {
            return ;
        }

        sdsClient.exceptionSign(point, t);
        sdsClient.downgradeFinally(point);
        RpcContext.getContext().remove(SdsDubboConstants.SDS_DUBBO_POINT);
        RpcContext.getContext().remove(SdsDubboConstants.SDS_DOWNGRADE_START_TIME);
    }

    public SdsClient getSdsClient() {
        return sdsClient;
    }
}
