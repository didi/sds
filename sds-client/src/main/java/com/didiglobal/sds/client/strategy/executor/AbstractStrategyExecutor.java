package com.didiglobal.sds.client.strategy.executor;

import com.didiglobal.sds.client.bean.CheckData;
import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.enums.DowngradeActionType;

/**
 * Created by manzhizhen on 17/7/26.
 */
public abstract class AbstractStrategyExecutor {

    protected AbstractStrategyExecutor next;

    public AbstractStrategyExecutor(AbstractStrategyExecutor next) {
        this.next = next;
    }

    /**
     * 策略检查
     *
     * @param checkData 当前数据
     * @param strategy  当前策略
     * @return true: 通过，不用降级； false：不通过，需要降级
     */
    protected abstract boolean strategyCheck(CheckData checkData, SdsStrategy strategy);

    /**
     * 获取策略类型
     *
     * @return true: 通过，不用降级； false：不通过，需要降级
     */
    protected abstract DowngradeActionType getStrategyType();

    /**
     * 开始执行策略（链）
     *
     * @param checkData
     * @param strategy
     * @return 降级检查结果，null-通过检查，不用降级，DowngradeActionType：没通过，降级触发的类型
     */
    public DowngradeActionType execute(CheckData checkData, SdsStrategy strategy) {
        boolean checkSuccess = strategyCheck(checkData, strategy);

        if (checkSuccess && next != null) {
            return next.execute(checkData, strategy);
        }

        if (!checkSuccess) {
            return getStrategyType();
        }

        return null;
    }

    public AbstractStrategyExecutor getNext() {
        return next;
    }

    public void setNext(AbstractStrategyExecutor next) {
        this.next = next;
    }


}
