package com.didiglobal.sds.aspectj;

import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.SdsClientFactory;
import com.didiglobal.sds.client.SdsExceptionCallBack;
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
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: yizhenqiang
 * @Date: Create in 2020-03-14 20:33
 */
@Aspect
public class SdsPointAspect {

    private static Logger logger = SdsLoggerFactory.getDefaultLogger();

    private ConcurrentHashMap<String,Object> exceptionCallBackClass = new ConcurrentHashMap<>();

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
            if (this.handleSdsExceptionHandler(pjp, ex) == null) {
                // 表示不处理这个异常, 继续抛出异常
                throw ex;
            } else {
                // 表示捕获到异常且处理
                return this.handleSdsExceptionHandler(pjp, ex);
            }

        } catch (Throwable throwable) {
            // 这里用于统计异常量
            sdsClient.exceptionSign(point, throwable);

            throw throwable;

        } finally {
            sdsClient.downgradeFinally(point);
        }
    }

    private Object handleSdsExceptionHandler(ProceedingJoinPoint pjp, SdsException ex) throws Exception {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Class<?> targetClass = pjp.getTarget().getClass();
        Method originMethod = this.getDeclaredMethod(targetClass, signature.getName(), signature.getMethod().getParameterTypes());
        // 上面已经判断过，不会发生 NPE
        SdsDowngradeMethod sdsDowngradeMethodAnnotation = originMethod.getAnnotation(SdsDowngradeMethod.class);
        // 判断是否需要处理此异常
        if (!this.containsTargetException(ex, sdsDowngradeMethodAnnotation.includeExceptions())) {
            return null;
        }
        Class<? extends SdsExceptionCallBack> callbackClass = sdsDowngradeMethodAnnotation.callback();
        String callbackClassName = callbackClass.getName();
        if (!exceptionCallBackClass.containsKey(callbackClassName)) {
            // 这里缓存一下, 以免每次都构建新的对象
            exceptionCallBackClass.put(callbackClassName, callbackClass.newInstance());
        }
        SdsExceptionCallBack sdsExceptionCallBack = (SdsExceptionCallBack) exceptionCallBackClass.get(callbackClassName);
        return sdsExceptionCallBack.handle(originMethod, pjp.getArgs(), ex);
    }

    private boolean containsTargetException(Exception targetException, Class<? extends Exception>[] includeExceptions) {
        for (Class<? extends Exception> ex : includeExceptions) {
            if (ex.isInstance(targetException)) {
                return true;
            }
        }
        return false;
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
