package com.didiglobal.sds.extension.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.SdsClientFactory;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.client.service.SdsDowngradeReturnValueService;
import org.slf4j.Logger;

import java.lang.reflect.Method;

@Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
public class SdsDubboFilter implements Filter {

    private static Logger logger = SdsLoggerFactory.getDefaultLogger();

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        SdsClient sdsClient = SdsClientFactory.getSdsClient();

        if (sdsClient == null) {
            logger.warn("SdsDubboFilter#invoke 获取SdsClient实例为null，请检查是否已经初始化成功。");
            return invoker.invoke(invocation);
        }

        Throwable exception = null;
        Result result = new RpcResult();
        String point = invoker.getInterface().getSimpleName() + "-" + invocation.getMethodName() + "-" + (RpcContext.getContext().isProviderSide() ? "ProviderPoint" : "ConsumerPoint");
        try {

            try {
                if (sdsClient.shouldDowngrade(point)) {
                    // 优先使用返回值
                    Method method = invoker.getInterface().getMethod(invocation.getMethodName(),
                            invocation.getParameterTypes());
                    Object returnValue = SdsDowngradeReturnValueService.getInstance().getDowngradeReturnValue(point,
                            method.getGenericReturnType());

                    return new RpcResult(returnValue);
                }
            } catch (Throwable e) {
                logger.warn("SdsDubboFilter#invoke 内部处理异常", e);
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
