package com.didiglobal.sds.client.strategy.executor;

import com.didiglobal.sds.client.annotation.SdsStrategyExecutor;
import com.didiglobal.sds.client.bean.CheckData;
import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.enums.DowngradeActionType;

/**
 * Created by manzhizhen on 17/7/26.
 */
@SdsStrategyExecutor(sort = 100)
public class VisitStrategyExecutor extends AbstractStrategyExecutor {

    public VisitStrategyExecutor(AbstractStrategyExecutor next) {
        super(next);
    }

    @Override
    public boolean strategyCheck(CheckData checkData, SdsStrategy strategy) {

        if (strategy.getVisitThreshold() == null || strategy.getVisitThreshold() < 0) {
            return true;
        }

        // 注意，这里一定要将降级量减去，否则一旦流量大于访问量阈值，将一直被降级下去
        return (checkData.getVisitCount() - checkData.getDowngradeCount()) <= strategy.getVisitThreshold();
    }

    @Override
    protected DowngradeActionType getStrategyType() {
        return DowngradeActionType.VISIT;
    }
}
