/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2016 All Rights Reserved.
 */
package com.didiglobal.sds.client.service;

import com.didiglobal.sds.client.exception.SdsException;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.client.util.AssertUtil;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 降级异常过滤服务类
 *
 * @author manzhizhen
 * @version $Id: SdsDowngradeExceptionService.java, v 0.1 2016年1月28日 上午9:59:56 Administrator Exp $
 */
final public class SdsDowngradeExceptionService {

    private Logger logger = SdsLoggerFactory.getDefaultLogger();

    private final static SdsDowngradeExceptionService downgradeExceptionService = new SdsDowngradeExceptionService();

    /**
     * key-降级点，value-降级点异常过滤
     * 这里无需使用ConcurrentHashMap，因为更新和新增都是单线程的
     */
    private ConcurrentHashMap<String, DowngradeException> pointExceptionMap = new ConcurrentHashMap<>();

    private SdsDowngradeExceptionService() {
    }

    public static SdsDowngradeExceptionService getInstance() {
        return downgradeExceptionService;
    }

    /**
     * 给一个降级点新增异常过滤
     *
     * @param point
     * @param exceptions
     * @param exceptExceptions
     */
    public void addPointException(String point, List<Class<?>> exceptions, List<Class<?>> exceptExceptions) {
        AssertUtil.notBlack(point, "降级点不能为空！");

        if (pointExceptionMap.containsKey(point)) {
            logger.warn("SdsDowngradeExceptionService 新增降级点异常失败，降级点异常已经存在：" + point);
            return;

        }

        DowngradeException downgradeException = new DowngradeException(exceptions, exceptExceptions);
        DowngradeException old = pointExceptionMap.putIfAbsent(point, downgradeException);
        if (old == null) {
            logger.info("SdsDowngradeExceptionService 新增【降级点异常】成功, point:" + point + " downgradeException:"
                    + downgradeException);

        } else {
            logger.warn("SdsDowngradeExceptionService 新增【降级点异常】失败, 降级点异常已存在， point:" + point
                    + " 原downgradeException:" + pointExceptionMap.get(point));

        }

    }

    /**
     * 判断该异常是否是降级异常
     * 注意：如果该降级点没有配置降级异常，则所有异常默认都是失败异常
     *
     * @param point
     * @param exception
     * @return
     */
    public boolean isDowngradeException(String point, Throwable exception) {
        AssertUtil.notBlack(point, "降级点不能为空！");

        if (exception == null || exception instanceof SdsException) {
            return false;
        }

        DowngradeException downgradeException = pointExceptionMap.get(point);
        if (downgradeException != null) {
            return downgradeException.isFailException(exception);
        }

        /**
         * 如果没配置降级异常，则默认所有异常都是失败异常
         */
        return true;
    }

    /**
     * 降级异常类
     *
     * @author manzhizhen
     * @version $Id: SdsDowngradeExceptionService.java, v 0.1 2016年1月28日 上午10:26:09 Administrator Exp $
     */
    static class DowngradeException {
        /**
         * 抛出属于该异常类的异常属于失败
         */
        private List<Class<?>> exceptions;
        /**
         * 抛出属于该异常类的异常不属于失败
         */
        private List<Class<?>> exceptExceptions;

        public DowngradeException(List<Class<?>> exceptions, List<Class<?>> exceptExceptions) {
            this.exceptions = exceptions;
            this.exceptExceptions = exceptExceptions;
        }

        public List<Class<?>> getExceptions() {
            return exceptions;
        }

        public List<Class<?>> getExceptExceptions() {
            return exceptExceptions;
        }

        /**
         * 该异常是否是失败异常
         *
         * @param exception
         * @return
         */
        public boolean isFailException(Throwable exception) {
            if (exception == null) {
                return true;
            }

            if (exceptions.size() > 0) {
                for (Class<?> excClass : exceptions) {
                    if (excClass.isAssignableFrom(exception.getClass())) {
                        return true;
                    }
                }

                return false;
            }

            if (exceptExceptions.size() > 0) {
                for (Class<?> excClass : exceptExceptions) {
                    if (excClass.isAssignableFrom(exception.getClass())) {
                        return false;
                    }
                }

                return true;
            }

            /**
             * 如果exceptions和exceptExceptions都没设置，则默认所有异常都是失败异常
             */
            return true;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "DowngradeException [" + (exceptions != null ? "exceptions=" + exceptions + ", " : "")
                    + (exceptExceptions != null ? "exceptExceptions=" + exceptExceptions : "") + "]";
        }

    }
}
