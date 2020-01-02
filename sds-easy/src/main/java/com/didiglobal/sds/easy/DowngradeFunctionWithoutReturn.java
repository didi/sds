package com.didiglobal.sds.easy;

/**
 * 降级后执行的无返回值的方法
 * Sds内置的函数接口，类等于JDK8的Supplier<T>，为了支持JDK8之前的版本，所以定制了该接口
 */
@FunctionalInterface
public interface DowngradeFunctionWithoutReturn {

    /**
     * 执行降级逻辑
     *
     */
    void invokeDowngradeMethod();
}
