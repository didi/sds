package com.didiglobal.sds.aspectj;

import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.SdsClientFactory;
import com.didiglobal.sds.client.annotation.SdsDowngradeMethod;
import com.didiglobal.sds.client.exception.SdsException;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.client.service.SdsDowngradeReturnValueService;
import com.didiglobal.sds.client.util.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.Arrays;

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
            Object downgradeReturnValue = null;
            // 优先判断是否被 fallback 处理
            if (!StringUtils.isBlank(sdsDowngradeMethodAnnotation.fallback())) {
                downgradeReturnValue = this.handleFallback(pjp, ex);
                if (downgradeReturnValue != null) {
                    return downgradeReturnValue;
                }
            }
            // 再判断是否被 sds-admin 降级处理
            downgradeReturnValue = SdsDowngradeReturnValueService.getInstance().getDowngradeReturnValue(point, originMethod.getGenericReturnType());
            if (downgradeReturnValue != null) {
                return downgradeReturnValue;
            }
            // 如果都没有处理，使用默认策略抛出异常
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
     * 处理自定义的异常
     * 这里提供了两种写法，详细见 {@link com.didiglobal.sds.example.chapter3.OrderManageService}
     * 降级方法要求：
     * 1. 降级方法和注解标注的方法要在同一个类上
     * 2. 方法名称必须是 {@link SdsDowngradeMethod} 中的 fallback() 指定的
     * 3. 降级方法的入参类型必须和限流方法的保持一致或者可以多添加一个参数（类型必须为 {@link SdsException}，且必须放在最后一个位置）
     *
     * @param pjp {@link ProceedingJoinPoint}
     * @param ex  {@link SdsException}
     * @return 降级方法的返回结果
     */
    private Object handleFallback(ProceedingJoinPoint pjp, SdsException ex) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Class<?> targetClass = pjp.getTarget().getClass();
        Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
        Object[] args = pjp.getArgs();
        Method originMethod = this.getDeclaredMethod(targetClass, signature.getName(), parameterTypes);
        // 上面已经判断过，不会发生 NPE
        SdsDowngradeMethod sdsDowngradeMethodAnnotation = originMethod.getAnnotation(SdsDowngradeMethod.class);
        // 获取降级方法名称
        String fallbackMethodName = sdsDowngradeMethodAnnotation.fallback();
        // 优先查找带异常的方法
        Method fallbackMethod = null;
        Class<?>[] newParameterTypes = Arrays.copyOf(parameterTypes, parameterTypes.length + 1);
        newParameterTypes[newParameterTypes.length - 1] = ex.getClass();
        Object[] newArgs = Arrays.copyOf(args, args.length + 1);
        newArgs[newArgs.length - 1] = ex;
        fallbackMethod = this.getDeclaredMethod(targetClass, fallbackMethodName, newParameterTypes);
        if (fallbackMethod == null) {
            // 如果没有查找到，则查找不带异常的方法，注意这里要把参数变回来，即去掉 SdsException
            newArgs = args;
            fallbackMethod = this.getDeclaredMethod(targetClass, fallbackMethodName, parameterTypes);
        }
        if (fallbackMethod == null) {
            logger.warn("找不到 {} 对应的降级方法，请检查您的注解配置", fallbackMethodName);
            return null;
        }
        try {
            return fallbackMethod.invoke(pjp.getThis(), newArgs);
        } catch (Exception e) {
            logger.error("fallbackMethod 处理失败", e);
        }
        return null;
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
