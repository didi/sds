package com.didiglobal.sds.client.strategy.executor;

import com.didiglobal.sds.client.annotation.SdsStrategyExecutor;
import com.didiglobal.sds.client.bean.CheckData;
import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.enums.DowngradeActionType;

/**
 * Created by manzhizhen on 17/7/26.
 */
@SdsStrategyExecutor(sort = 300)
public class ConcurrentStrategyExecutor extends AbstractStrategyExecutor {

    public ConcurrentStrategyExecutor(AbstractStrategyExecutor next) {
        super(next);
    }

    @Override
    protected boolean strategyCheck(CheckData checkData, SdsStrategy strategy) {

        if(checkData.getConcurrentAcquire() == null) {
            return true;
        }

        /**
         * 由于并发策略使用了信号量，所以在统计的同时已经起到了策略判断的功能，所以这里直接返回判断结果
         */
        return checkData.getConcurrentAcquire();
    }

    @Override
    protected DowngradeActionType getStrategyType() {
        return DowngradeActionType.CONCURRENT;
    }
}
