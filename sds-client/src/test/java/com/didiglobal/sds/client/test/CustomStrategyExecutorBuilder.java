package com.didiglobal.sds.client.test;

import com.didiglobal.sds.client.strategy.StrategyExecutorBuilder;
import com.didiglobal.sds.client.strategy.executor.AbstractStrategyExecutor;

/**
 * <p>description : CustomStrategyExecutorBuilder
 *
 * @author : masteryourself
 * @version : 1.0.1
 * @date : 2020/3/14 12:11
 */
public class CustomStrategyExecutorBuilder implements StrategyExecutorBuilder {

    @Override
    public AbstractStrategyExecutor build() {
        throw new UnsupportedOperationException("maybe occur error");
    }

}
