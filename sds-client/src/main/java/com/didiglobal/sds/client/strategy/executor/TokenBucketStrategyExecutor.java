package com.didiglobal.sds.client.strategy.executor;

import com.didiglobal.sds.client.bean.CheckData;
import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.enums.DowngradeActionType;

import static com.didiglobal.sds.client.contant.BizConstant.BUCKET_TIME;
import static com.didiglobal.sds.client.contant.BizConstant.CYCLE_BUCKET_NUM;
import static com.didiglobal.sds.client.enums.DowngradeActionType.TOKEN_BUCKET;

/**
 * 令牌桶策略执行器
 *
 * @auther manzhizhen
 * @date 2018/12/31
 */
public class TokenBucketStrategyExecutor extends AbstractStrategyExecutor {

    public TokenBucketStrategyExecutor(AbstractStrategyExecutor next) {
        super(next);
    }

    @Override
    protected boolean strategyCheck(CheckData checkData, SdsStrategy strategy) {
        if (strategy.getTokenBucketGeneratedTokensInSecond() == null || strategy.
                getTokenBucketGeneratedTokensInSecond() < 0) {
            return true;
        }

        boolean curSecondTokenUse = checkData.getTakeTokenBucketNum().compareTo(strategy.
                getTokenBucketGeneratedTokensInSecond().longValue()) < 0;

        // 如果当前秒的令牌已经不够用，那么就看令牌桶里是否还有
        if (!curSecondTokenUse && strategy.getTokenBucketSize() != null && strategy.getTokenBucketSize() > 0) {
            // 计算滑动周期内桶里是否还有多余的令牌
            return CYCLE_BUCKET_NUM * BUCKET_TIME * strategy.getTokenBucketGeneratedTokensInSecond() + strategy.
                    getTokenBucketSize() - (checkData.getTakeTokenBucketNum() - checkData.getDowngradeCount()) > 0;
        }

        return curSecondTokenUse;
    }

    @Override
    protected DowngradeActionType getStrategyType() {
        return TOKEN_BUCKET;
    }
}
