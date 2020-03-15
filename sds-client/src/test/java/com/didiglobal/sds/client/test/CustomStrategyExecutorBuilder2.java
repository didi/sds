package com.didiglobal.sds.client.test;

import com.didiglobal.sds.client.strategy.StrategyExecutorBuilder;
import com.didiglobal.sds.client.strategy.executor.*;

/**
 * <p>description : CustomStrategyExecutorBuilder2
 *
 * @author : masteryourself
 * @version : 1.0.1
 * @date : 2020/3/14 12:12
 */
public class CustomStrategyExecutorBuilder2 implements StrategyExecutorBuilder {

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
