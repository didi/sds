/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2016 All Rights Reserved.
 */
package com.didiglobal.sds.client.config;

import com.didiglobal.sds.client.service.SdsDowngradeDelayService;
import com.didiglobal.sds.client.service.SdsDowngradeExceptionService;
import com.didiglobal.sds.client.util.AssertUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 降级点策略配置
 * 
 * @author manzhizhen
 */
public class SdsPointStrategyConfig {

    /**
     * 给降级点设置降级延迟时间
     * 
     * @param point 降级点名称
     * @param delayTime 降级延迟时间，单位毫秒
     * 
     */
    public static void setDowngradeDelay(String point, long delayTime) {
        AssertUtil.notBlack(point, "降级点不能为空！");

        SdsDowngradeDelayService.getInstance().addPointDelay(point, delayTime);
    }

    /**
     * 给降级点设置降级延迟时间
     * 
     * @param point 降级点名称
     * @param delayTime 降级延迟时间，单位毫秒
     * @param retryInterval 降级延迟重试时间间隔，单位毫秒，小于等于0表示不重试
     */
    public static void setDowngradeDelay(String point, long delayTime, long retryInterval) {
        AssertUtil.notBlack(point, "降级点不能为空！");

        SdsDowngradeDelayService.getInstance().addPointDelay(point, delayTime, retryInterval);
    }

    /**
     * 给降级点设置降级异常
     * 
     * @param point 降级点
     * @param exceptions 属于失败请求的异常
     */
    public static void setDowngradeExceptions(String point, List<Class<?>> exceptions) {
        AssertUtil.notBlack(point, "降级点不能为空！");

        SdsDowngradeExceptionService.getInstance().addPointException(point, exceptions, new ArrayList<Class<?>>());
    }

    /**
     * 给降级点设置排除降级异常
     * 
     * @param point 降级点
     * @param exceptExceptions 不属于失败请求的异常
     */
    public static void setDowngradeExceptExceptions(String point, List<Class<?>> exceptExceptions) {
        AssertUtil.notBlack(point, "降级点不能为空！");

        SdsDowngradeExceptionService.getInstance()
            .addPointException(point, new ArrayList<Class<?>>(), exceptExceptions);
    }
}
