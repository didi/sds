package com.didiglobal.sds.easy;

/**
 * 业务方法
 * 注意：Sds内置的函数接口，类等于JDK8的Supplier<T>，为了支持JDK8之前的版本，所以定制了该接口
 *
 * @param <T>
 */
@FunctionalInterface
public interface BizFunction<T> {

    /**
     * 执行业务逻辑并返回结果
     *
     * @return
     */
    T invokeBizMethod();
}
