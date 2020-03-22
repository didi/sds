package com.didiglobal.sds.client.strategy.executor;

import com.didiglobal.sds.client.annotation.SdsStrategyExecutor;
import com.didiglobal.sds.client.bean.CheckData;
import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.enums.DowngradeActionType;

/**
 * Created by manzhizhen on 17/7/26.
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
                strategy.getExceptionRateThreshold() > 100 ||
                strategy.getExceptionRateStart() >= checkData.getVisitCount()) {
            return true;
        }

        // 注意，在计算异常率时，分母不能直接使用访问量，需要把降级数量移除，因为降级后实际没有真正访问业务方法，不把降级数量移除将导致计算的异常率偏小
        long inputTotal = checkData.getVisitCount() - checkData.getDowngradeCount();

        return inputTotal == 0 || (checkData.getExceptionCount() + 0.0) * 100 / inputTotal <= strategy.getExceptionRateThreshold();
    }

    @Override
    protected DowngradeActionType getStrategyType() {
        return DowngradeActionType.EXCEPTION_RATE;
    }
}
