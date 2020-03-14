package com.didiglobal.sds.client.strategy;

import com.didiglobal.sds.client.strategy.executor.*;

/**
 * <p>description : DefaultStrategyExecutorBuilder
 *
 * @author : masteryourself
 * @version : 1.0.1
 * @date : 2020/3/14 11:52
 */
public class DefaultStrategyExecutorBuilder implements StrategyExecutorBuilder {

    @Override
    public AbstractStrategyExecutor build() {
        return new VisitStrategyExecutor(
                new TokenBucketStrategyExecutor(
                        new ConcurrentStrategyExecutor(
                                new TimeoutStrategyExecutor(
                                        new ExceptionStrategyExecutor(
                                                new ExceptionRateStrategyExecutor(null)
                                        )))));
    }

}
