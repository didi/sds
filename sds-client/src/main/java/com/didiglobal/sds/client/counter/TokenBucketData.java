package com.didiglobal.sds.client.counter;

import com.didiglobal.sds.client.bean.VisitWrapperValue;
import com.didiglobal.sds.client.contant.BizConstant;

/**
 * 令牌桶的实现类（可以认为是QPS的高级替换方案）
 * 速率：每秒生成多少个令牌
 * 桶容量：桶里面最多能存储多少个令牌
 *
 * @auther manzhizhen
 * @date 2018/12/31
 */
public class TokenBucketData {

    /**
     * 每个1秒能生成多少个令牌，-1表示不生效
     */
    private Integer generatedTokensInSecond = -1;

    /**
     * 桶最多能存储多少令牌，-1表示不生效
     */
    private Integer bucketSize = -1;

    /**
     * 主要用来统计数据
     */
    private AbstractCycleData cycleTokenBucketData = new SlidingWindowData(BizConstant.CYCLE_NUM,
            BizConstant.CYCLE_BUCKET_NUM, BizConstant.BUCKET_TIME);

    public TokenBucketData(Integer generatedTokensInSecond, Integer bucketSize) {
        this.generatedTokensInSecond = generatedTokensInSecond;
        this.bucketSize = bucketSize;
    }

    public TokenBucketData() {
    }

    /**
     * 当前秒令牌使用计数器+1
     *
     * @param time
     * @return
     */
    public long takeOneToken(long time) {

        VisitWrapperValue visitWrapperValue = cycleTokenBucketData.incrementAndGet(time);

        return visitWrapperValue.getBucketValue();
    }

    public AbstractCycleData getCycleTokenBucketData() {
        return cycleTokenBucketData;
    }
}
