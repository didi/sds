package com.didiglobal.sds.extension.dubbo.filter;

import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.SdsClientFactory;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.client.service.SdsDowngradeReturnValueService;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import java.lang.reflect.Method;


/**
 * Created by sea on 2020-04-08.
 */
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER})
public class SdsApacheDubboFilter implements Filter {

    protected static Logger logger = SdsLoggerFactory.getDefaultLogger();

    SdsClient sdsClient;

    //for spring inject
    public void setSdsClient(SdsClient sdsClient) {
        this.sdsClient = sdsClient;
    }

    public SdsApacheDubboFilter() {

    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (sdsClient == null) {
            logger.info("init sdsClient by java system properties");
            sdsClient = SdsClientFactory.getSdsClient();
        }

        if (sdsClient == null) {
            logger.warn("SdsApacheDubboFilter#invoke SdsClientFactory.getSdsClient() null, please check SdsClient is initialized ");
            return invoker.invoke(invocation);
        }

        Throwable exception = null;
        Result result = null;
        String point = invoker.getInterface().getSimpleName() + "-" + invocation.getMethodName() + "-" + (RpcContext.getContext().isProviderSide() ? "ProviderPoint" : "ConsumerPoint");

        try {
            try {
                if (sdsClient.shouldDowngrade(point)) {
                    // 优先使用返回值
                    Method method = invoker.getInterface().getMethod(invocation.getMethodName(),
                            invocation.getParameterTypes());

                    Object returnValue = SdsDowngradeReturnValueService.getInstance().getDowngradeReturnValue(point,
                                method.getGenericReturnType());

                    return AsyncRpcResult.newDefaultAsyncResult(returnValue, invocation);
                }
            } catch (Throwable e) {
                logger.warn("SdsApacheDubboFilter#invoke sds error occur", e);
            }

            result = invoker.invoke(invocation);
            return result;
        } catch (Throwable e) {
            exception = e;
            throw new RpcException(e);
        } finally {
            if (result != null && result.getException() != null) {
                exception = result.getException();
            }

            sdsClient.exceptionSign(point, exception);

            sdsClient.downgradeFinally(point);
        }
    }
}
