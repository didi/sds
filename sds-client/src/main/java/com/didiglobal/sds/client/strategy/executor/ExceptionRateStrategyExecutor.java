package com.didiglobal.sds.client.strategy.executor;

import com.didiglobal.sds.client.annotation.SdsStrategyExecutor;
import com.didiglobal.sds.client.bean.CheckData;
import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.enums.DowngradeActionType;

/**
 * Created by yizhenqiang on 17/7/26.
 */
@SdsStrategyExecutor(sort = 600)
public class ExceptionRateStrategyExecutor extends AbstractStrategyExecutor {

    public ExceptionRateStrategyExecutor(AbstractStrategyExecutor next) {
        super(next);
    }

    @Override
    protected boolean strategyCheck(CheckData checkData, SdsStrategy strategy) {

        if (strategy.getExceptionRateStart() == null || strategy.getExceptionRateStart() < 0 ||
                strategy.getExceptionRateThreshold() == null || strategy.getExceptionRateThreshold() < 0 ||
                strategy.getExceptionRateThreshold() > 100) {
            return true;
        }

        long inputTotal = checkData.getVisitCount() - checkData.getDowngradeCount();
        return inputTotal == 0 || inputTotal <= strategy.getExceptionRateStart()
                || (checkData.getExceptionCount() + 0.0) * 100 / inputTotal <= strategy.getExceptionRateThreshold();
    }

    @Override
    protected DowngradeActionType getStrategyType() {
        return DowngradeActionType.EXCEPTION_RATE;
    }
}
