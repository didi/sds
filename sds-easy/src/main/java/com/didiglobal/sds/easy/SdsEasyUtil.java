package com.didiglobal.sds.easy;

import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.SdsClientFactory;
import com.didiglobal.sds.client.util.StringUtils;

/**
 * 让我们更便捷的使用SDS
 */
public class SdsEasyUtil {

    /**
     * 执行有降级逻辑环绕的业务方法，降级后将返回指定的默认值
     *
     * @param point          降级点
     * @param downgradeValue 降级后返回的值
     * @param bizFunction    业务方法
     * @param <T>            返回值类型
     * @return
     */
    public static <T> T invokerMethod(String point, T downgradeValue, BizFunction<T> bizFunction) {
        if (bizFunction == null) {
            return downgradeValue;
        }

        /**
         * 如果降级点为空，那么就相当于没有降级功能
         */
        if (StringUtils.isBlank(point)) {
            return bizFunction.invokeBizMethod();
        }

        SdsClient sdsClient = SdsClientFactory.getSdsClient();

        // 如果客户端没初始化，那么直接执行业务方法
        if (sdsClient == null) {
            return bizFunction.invokeBizMethod();
        }

        try {
            if (sdsClient.shouldDowngrade(point)) {
                return downgradeValue;
            }

            return bizFunction.invokeBizMethod();

        } catch (Throwable e) {
            sdsClient.exceptionSign(point, e);
            throw e;

        } finally {
            sdsClient.downgradeFinally(point);
        }
    }

    /**
     * 执行有降级逻辑环绕的业务方法，降级后将返回指定的默认值，适用于无返回值的场景
     *
     * @param point                    降级点
     * @param bizFunctionWithoutReturn 无返回值的业务方法
     * @return
     */
    public static void invokerMethodWithoutReturn(String point, BizFunctionWithoutReturn bizFunctionWithoutReturn) {
        if (bizFunctionWithoutReturn == null) {
            return;
        }

        /**
         * 如果降级点为空，那么就相当于没有降级功能
         */
        if (StringUtils.isBlank(point)) {
            bizFunctionWithoutReturn.invokeBizMethod();
            return;
        }

        SdsClient sdsClient = SdsClientFactory.getSdsClient();

        // 如果客户端没初始化，那么直接执行业务方法
        if (sdsClient == null) {
            bizFunctionWithoutReturn.invokeBizMethod();
            return;
        }

        try {
            if (sdsClient.shouldDowngrade(point)) {
                return;
            }

            bizFunctionWithoutReturn.invokeBizMethod();

        } catch (Throwable e) {
            sdsClient.exceptionSign(point, e);
            throw e;

        } finally {
            sdsClient.downgradeFinally(point);
        }
    }

    /**
     * 执行有降级逻辑环绕的业务方法，降级后将指定的降级逻辑
     *
     * @param point             降级点
     * @param downgradeFunction 降级后执行的方法
     * @param bizFunction       业务方法
     * @param <T>
     * @return
     */
    public static <T> T invokerMethodWithDowngradeFunction(String point, DowngradeFunction<T> downgradeFunction,
                                                           BizFunction<T> bizFunction) {
        if (bizFunction == null) {
            if (downgradeFunction == null) {
                throw new IllegalArgumentException("SdsEasyUtil#invokerMethodWithDowngradeFunction downgradeFunction " +
                        "and bizFunction cannot null");
            }

            return downgradeFunction.invokeDowngradeMethod();
        }

        /**
         * 如果降级点为空，那么就相当于没有降级功能
         */
        if (StringUtils.isBlank(point)) {
            return bizFunction.invokeBizMethod();
        }

        SdsClient sdsClient = SdsClientFactory.getSdsClient();
        if (sdsClient == null) {
            return bizFunction.invokeBizMethod();
        }

        try {
            if (sdsClient.shouldDowngrade(point) && downgradeFunction != null) {
                return downgradeFunction.invokeDowngradeMethod();
            }

            return bizFunction.invokeBizMethod();

        } catch (Throwable e) {
            sdsClient.exceptionSign(point, e);
            throw e;

        } finally {
            sdsClient.downgradeFinally(point);
        }
    }

    /**
     * 执行有降级逻辑环绕的业务方法，降级后将指定的降级逻辑，适用于无返回值的场景
     *
     * @param point                          降级点
     * @param downgradeFunctionWithoutReturn 降级后执行的方法
     * @param bizFunctionWithoutReturn       业务方法
     */
    public static void invokerMethodWithDowngradeFunctionWithoutReturn(String point, DowngradeFunctionWithoutReturn
            downgradeFunctionWithoutReturn, BizFunctionWithoutReturn bizFunctionWithoutReturn) {
        if (bizFunctionWithoutReturn == null) {
            if (downgradeFunctionWithoutReturn == null) {
                throw new IllegalArgumentException("SdsEasyUtil#invokerMethodWithDowngradeFunctionWithoutReturn " +
                        "downgradeFunction and bizFunction cannot null");
            }

            downgradeFunctionWithoutReturn.invokeDowngradeMethod();
            return;
        }

        /**
         * 如果降级点为空，那么就相当于没有降级功能
         */
        if (StringUtils.isBlank(point)) {
            bizFunctionWithoutReturn.invokeBizMethod();
            return;
        }

        SdsClient sdsClient = SdsClientFactory.getSdsClient();
        if (sdsClient == null) {
            bizFunctionWithoutReturn.invokeBizMethod();
            return;
        }

        try {
            if (sdsClient.shouldDowngrade(point) && downgradeFunctionWithoutReturn != null) {
                downgradeFunctionWithoutReturn.invokeDowngradeMethod();
                return;
            }

            bizFunctionWithoutReturn.invokeBizMethod();

        } catch (Throwable e) {
            sdsClient.exceptionSign(point, e);
            throw e;

        } finally {
            sdsClient.downgradeFinally(point);
        }
    }

    /**
     * 一键熔断开关
     * 有时候我只需要做熔断功能，不需要其他限流方式，类似于一键开关，那么就可以使用该方法
     *
     * @param point 降级点
     * @return true-熔断打开，不应该执行业务逻辑，业务方应该直接走降级逻辑; false-熔断关闭，应该执行业务逻辑
     */
    public static boolean oneButtonFuseSwitch(String point) {
        if (StringUtils.isBlank(point)) {
            return false;
        }

        SdsClient sdsClient = SdsClientFactory.getSdsClient();
        if (sdsClient == null) {
            return false;
        }

        try {
            if (sdsClient.shouldDowngrade(point)) {
                return true;
            }

        } finally {
            sdsClient.downgradeFinally(point);
        }

        return false;
    }

}
