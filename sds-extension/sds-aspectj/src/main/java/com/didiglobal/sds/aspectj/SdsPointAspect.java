package com.didiglobal.sds.aspectj;

import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.SdsClientFactory;
import com.didiglobal.sds.client.annotation.SdsDowngradeMethod;
import com.didiglobal.sds.client.exception.SdsException;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;

import java.lang.reflect.Method;

/**
 * @Author: manzhizhen
 * @Date: Create in 2020-03-14 20:33
 */
@Aspect
public class SdsPointAspect {

    private static Logger logger = SdsLoggerFactory.getDefaultLogger();

    @Pointcut("@annotation(com.didiglobal.sds.client.annotation.SdsDowngradeMethod)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object invoke(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Class<?> targetClass = pjp.getTarget().getClass();

        Method originMethod = getDeclaredMethod(targetClass, signature.getName(),
                signature.getMethod().getParameterTypes());
        if (originMethod == null) {
            throw new IllegalStateException(
                    "获取类方法失败，class：" + targetClass.getName() + ", method:" + signature.getMethod().getName());
        }

        SdsDowngradeMethod sdsDowngradeMethodAnnotation = originMethod.getAnnotation(SdsDowngradeMethod.class);
        if (sdsDowngradeMethodAnnotation == null) {
            throw new IllegalStateException("这不应该发生，请联系管理员！！");
        }

        SdsClient sdsClient = SdsClientFactory.getSdsClient();
        if (sdsClient == null) {
            return pjp.proceed();
        }

        String point = sdsDowngradeMethodAnnotation.point();
        try {
            // 如果需要被降级，那么直接抛异常，不执行业务方法
            if (sdsClient.shouldDowngrade(point)) {
                throw new SdsException(point);
            }

            return pjp.proceed();

        } catch (SdsException ex) {
            ex.setPoint(point);
            throw ex;

        } catch (Throwable throwable) {
            // 这里用于统计异常量
            sdsClient.exceptionSign(point, throwable);

            throw throwable;

        } finally {
            sdsClient.downgradeFinally(point);
        }
    }

    /**
     * 获取该类的声明的方法
     *
     * @param clazz
     * @param methodName
     * @param parameterTypes
     * @return
     */
    private Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(methodName, parameterTypes);

        } catch (NoSuchMethodException e) {
            logger.warn("SdsPointAspect#getDeclaredMethod has exception", e);
        }

        return null;
    }
}
