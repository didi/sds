package com.didiglobal.sds.client.test;

import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.counter.PowerfulCycleTimeCounter;
import com.didiglobal.sds.client.service.SdsPowerfulCounterService;
import com.didiglobal.sds.client.service.SdsStrategyService;
import org.junit.Assert;
import org.junit.Test;

/**
 * 令牌桶降级测试
 * <p>
 * Created by manzhizhen on 2020/4/10.
 */
public class TokenBucketDowngradeTest extends AbstractDowngradeTest {

    private final static int TOKEN_BUCKET_GENERATED_TOKENS_IN_SECOND = 15;
    private final static int TOKEN_BUCKET_SIZE = 20;

    @Test
    public void test() {

        executor();

        waitResult();

        PowerfulCycleTimeCounter powerfulCycleTimeCounter =
                SdsPowerfulCounterService.getInstance().getPointCounterMap().get(getPoint());
        Assert.assertNotNull(powerfulCycleTimeCounter);

        Assert.assertEquals(getExecutorTimes() * getThreadNum() - Math.max(TOKEN_BUCKET_GENERATED_TOKENS_IN_SECOND,
                TOKEN_BUCKET_SIZE),
                powerfulCycleTimeCounter.getLastCycleDowngradeValue(System.currentTimeMillis()));

    }

    @Override
    protected void initStrategy() {
        SdsStrategy strategy = new SdsStrategy();
        strategy.setPoint(getPoint());

        // 桶的时间长度是1秒，每秒生成15个令牌
        strategy.setTokenBucketGeneratedTokensInSecond(TOKEN_BUCKET_GENERATED_TOKENS_IN_SECOND);
        // 每个桶最多20个令牌
        strategy.setTokenBucketSize(TOKEN_BUCKET_SIZE);

        SdsStrategyService.getInstance().resetOne(getPoint(), strategy);
    }

    @Override
    protected long getExecutorTimes() {
        return 100;
    }

    @Override
    protected String getPoint() {
        return "tokenBucketPoint";
    }

    @Override
    protected int getThreadNum() {
        return 1;
    }

    @Override
    protected long getTakeTime() {
        return 1;
    }
}
