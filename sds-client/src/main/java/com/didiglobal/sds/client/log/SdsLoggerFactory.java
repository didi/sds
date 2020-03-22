package com.didiglobal.sds.client.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 客户端日志
 *
 * @author manzhizhen
 * @version $Id: ServerBizLogger.java, v 0.1 2015年9月22日 上午10:07:27 Administrator Exp $
 */
public class SdsLoggerFactory {

    /**
     * 返回api正常业务日志
     */
    public static Logger getDefaultLogger() {
        return LoggerFactory.getLogger(SdsLoggerConstants.DEFAULT_LOGGER_NAME);
    }

    /**
     * 返回心跳日志
     */
    public static Logger getHeartbeatLogger() {
        return LoggerFactory.getLogger(SdsLoggerConstants.HEARTBEAT_LOGGER_NAME);
    }

    /**
     * 返回拉取策略日志
     */
    public static Logger getPullStrategyLogger() {
        return LoggerFactory.getLogger(SdsLoggerConstants.PULL_STRATEGY_LOGGER_NAME);
    }
}
