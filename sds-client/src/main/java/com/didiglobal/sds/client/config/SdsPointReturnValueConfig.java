package com.didiglobal.sds.client.config;

/**
 * 降级点返回值配置
 *
 * @author manzhizhen
 * @date 2019-04-21
 */
public class SdsPointReturnValueConfig {

    /**
     * 设置某个降级点降级后的返回值
     *
     * 优先级：服务端配置id优先级高于此方法设置的返回值的返回值
     *
     * @param point
     * @param returnValue
     */
    public static void setPointDowngradeReturnValue(String point, Object returnValue) {

    }

}
