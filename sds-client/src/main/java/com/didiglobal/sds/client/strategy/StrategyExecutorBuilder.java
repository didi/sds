package com.didiglobal.sds.client.strategy;

import com.didiglobal.sds.client.strategy.executor.AbstractStrategyExecutor;

/**
 * <p>description : StrategyExecutorBuilder
 *
 * @author : masteryourself
 * @version : 1.0.1
 * @date : 2020/3/14 11:43
 */
public interface StrategyExecutorBuilder {

    /**
     * 构建策略链
     *
     * @return AbstractStrategyExecutor 链条
     */
    AbstractStrategyExecutor build();

}
