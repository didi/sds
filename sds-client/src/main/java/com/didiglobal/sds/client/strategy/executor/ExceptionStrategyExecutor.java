package com.didiglobal.sds.client.strategy.executor;

import com.didiglobal.sds.client.annotation.SdsStrategyExecutor;
import com.didiglobal.sds.client.bean.CheckData;
import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.enums.DowngradeActionType;

/**
 * Created by manzhizhen on 17/7/26.
 */
@SdsStrategyExecutor(sort = 500)
public class ExceptionStrategyExecutor extends AbstractStrategyExecutor {

    public ExceptionStrategyExecutor(AbstractStrategyExecutor next) {
        super(next);
    }

    @Override
    protected boolean strategyCheck(CheckData checkData, SdsStrategy strategy) {
        if (strategy.getExceptionThreshold() == null || strategy.getExceptionThreshold() < 0) {
            return true;
        }

        return checkData.getExceptionCount() <= strategy.getExceptionThreshold();
    }

    @Override
    protected DowngradeActionType getStrategyType() {
        return DowngradeActionType.EXCEPTION;
    }
}
