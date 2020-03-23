package com.didiglobal.sds.client.strategy.executor;

import com.didiglobal.sds.client.annotation.SdsStrategyExecutor;
import com.didiglobal.sds.client.bean.CheckData;
import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.enums.DowngradeActionType;

/**
 * Created by manzhizhen on 17/7/26.
 */
@SdsStrategyExecutor(sort = 400)
public class TimeoutStrategyExecutor extends AbstractStrategyExecutor {

    public TimeoutStrategyExecutor(AbstractStrategyExecutor next) {
        super(next);
    }

    @Override
    public boolean strategyCheck(CheckData checkData, SdsStrategy strategy) {
        if (strategy.getTimeoutCountThreshold() == null || strategy.getTimeoutCountThreshold() < 0) {
            return true;
        }

        return checkData.getTimeoutCount() <= strategy.getTimeoutCountThreshold();
    }

    @Override
    protected DowngradeActionType getStrategyType() {
        return DowngradeActionType.TIMEOUT;
    }
}
