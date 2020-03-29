package com.didiglobal.sds.admin.aop;

import com.alibaba.fastjson.JSON;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.admin.controller.HeartbeatController;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Map;

/**
 * 打印Controller出入口日志的
 * <p>
 * Created by manzhizhen on 18/5/7.
 */
@Component
@Aspect
public class SdsAspectAop {

    private static Logger logger = SdsLoggerFactory.getDefaultLogger();
    private static Logger heartbeatLogger = SdsLoggerFactory.getHeartbeatLogger();
    private static Logger pullPointStrategyLogger = SdsLoggerFactory.getPullStrategyLogger();

    @Pointcut("execution (public * com.didiglobal.sds.admin.controller.*Controller.*(..)) "
            //          +  "&& !execution (public * HeartbeatController.heartbeat(..))"
    )
    public void controllerMethod() {
    }

    @Around("controllerMethod()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.nanoTime();

        Object target = pjp.getTarget();
        String serviceName = target.getClass().getSimpleName();

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String methodName = methodSignature.getMethod().getName();

        // 获取参数
        Object[] args = pjp.getArgs();

        Object result = null;

        try {
            result = pjp.proceed();
            double costTime = (System.nanoTime() - startTime + 0.0) / 1000000;

            if (HeartbeatController.class.getSimpleName().equals(serviceName)) {

                if ("heartbeat".equals(methodName)) {
                    heartbeatLogger.info(serviceName + "#" + methodName + ", request:" + requestArgsWrapper(args) +
                            ", response:" + responseWrapper(result) + ", costTime:" + costTime);

                } else {
                    pullPointStrategyLogger.info(serviceName + "#" + methodName + ", request:" +
                            requestArgsWrapper(args) + ", response:" + responseWrapper(result) + ", costTime:" +
                            costTime);
                }

            } else {
                logger.info(serviceName + "#" + methodName + ", request:" + requestArgsWrapper(args) + ", response:" +
                        responseWrapper(result) + ", costTime:" + costTime);
            }

            return result;

        } catch (Exception e) {
            double costTime = (System.nanoTime() - startTime + 0.0) / 1000000;

            if (HeartbeatController.class.getSimpleName().equals(serviceName)) {
                heartbeatLogger.error(serviceName + "#" + methodName + ", request:" + requestArgsWrapper(args) +
                        ", response:" + responseWrapper(result) + ", costTime:" + costTime, e);

            } else {
                logger.error(serviceName + "#" + methodName + ", request:" + requestArgsWrapper(args) + ", response:" +
                        responseWrapper(result) + ", costTime:" + costTime, e);
            }

            throw e;
        }
    }

    /**
     * 对请求进行日志包装
     *
     * @param args
     * @return
     */
    private String requestArgsWrapper(Object[] args) {
        try {
            StringBuilder requestLog = new StringBuilder("[");
            if (args == null || args.length == 0) {
                return requestLog.append("]").toString();
            }

            for (Object arg : args) {
                // ServletResponse不需要打印
                if (arg instanceof ServletResponse) {
                    continue;
                }

                if (arg instanceof ServletRequest) {
                    Map<String, String[]> parameterMap = ((ServletRequest) arg).getParameterMap();

                    requestLog.append(JSON.toJSON(parameterMap)).append(", ");
                    continue;
                }

                requestLog.append(JSON.toJSON(arg)).append(", ");
            }

            if (requestLog.length() >= 3) {
                // 去除多余的", "
                requestLog.delete(requestLog.length() - 2, requestLog.length());
            }

            requestLog.append("]");

            return requestLog.toString();

        } catch (Exception e) {
            logger.warn("SdsAspectAop#requestArgsWrapper has exception, args:{}", args, e);
            return null;
        }
    }

    /**
     * 对请求进行日志包装
     *
     * @param response
     * @return
     */
    private String responseWrapper(Object response) {
        try {
            // 无法有效的输出ServletResponse对象，不应该出现ServletRequest对象
            if (response == null || response instanceof ServletResponse || response instanceof ServletRequest) {
                return null;
            }

            return JSON.toJSONString(response);

        } catch (Exception e) {
            logger.warn("SdsAspectAop#responseWrapper has exception, response:{}", response, e);
            return null;
        }
    }
}
