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

        System.out.println(checkData.getTakeTokenBucketNum());

        // 如果当前桶的令牌还没用完，那么直接返回
        if (checkData.getTakeTokenBucketNum().compareTo(strategy.
                getTokenBucketGeneratedTokensInSecond().longValue()) < 0) {
            return true;
        }

        // 如果当前桶的令牌已经用完，但又没额外设置桶容量（即默认桶容量和桶每秒生成的令牌数相同），那就直接拒绝
        if (strategy.getTokenBucketSize() <= strategy.getTokenBucketGeneratedTokensInSecond()) {
            return false;
        }

        // 如果当前秒的令牌已经不够用，那么就看历史桶中是否有剩余令牌能匀一下
        return CYCLE_BUCKET_NUM * BUCKET_TIME * strategy.getTokenBucketGeneratedTokensInSecond() - checkData.getTakeTokenBucketNum() + checkData.getDowngradeCount() > 0;
    }

    @Override
    protected DowngradeActionType getStrategyType() {
        return TOKEN_BUCKET;
    }
}
