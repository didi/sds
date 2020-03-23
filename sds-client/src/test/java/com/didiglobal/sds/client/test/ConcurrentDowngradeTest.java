package com.didiglobal.sds.client.test;

import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.counter.PowerfulCycleTimeCounter;
import com.didiglobal.sds.client.service.SdsPowerfulCounterService;
import com.didiglobal.sds.client.service.SdsStrategyService;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 并发量降级测试
 * <p>
 * Created by manzhizhen on 2016/4/24.
 */
public class ConcurrentDowngradeTest extends AbstractDowngradeTest {

    private final static int CONCURRENT_THRESHOLD = 2;

    @Test
    public void test() {

        /**
         * 实际场景：5个线程，每秒访问一次业务方法，每个线程访问10次
         * 策略：并发阈值2，降级比例100%
         */
        executor();

        waitResult();

        PowerfulCycleTimeCounter powerfulCycleTimeCounter =
                SdsPowerfulCounterService.getInstance().getPointCounterMap().get(getPoint());
        Assert.assertNotNull(powerfulCycleTimeCounter);

        long lastCycleDowngradeValue = powerfulCycleTimeCounter.getLastCycleDowngradeValue(System.currentTimeMillis());

        /**
         * 因为同时只允许两个线程来调用该方法，所以每次有3个线程会被降级，所以降级数量应该是30次
         */
        Assert.assertEquals((getThreadNum() - CONCURRENT_THRESHOLD) * 10, lastCycleDowngradeValue);


    }

    @Override
    protected void initStrategy() {

        ConcurrentHashMap<String, SdsStrategy> strategyMap = new ConcurrentHashMap<>();

        SdsStrategy strategy = new SdsStrategy();
        strategy.setPoint(getPoint());
        strategy.setConcurrentThreshold(CONCURRENT_THRESHOLD);
        strategy.setDowngradeRate(DOWNGRADE_RATE);

        strategyMap.put(getPoint(), strategy);

        SdsStrategyService.getInstance().resetAll(strategyMap);
    }

    @Override
    protected long getExecutorTimes() {
        return 10;
    }

    @Override
    protected String getPoint() {
        return "concurrentPoint";
    }

    @Override
    protected int getThreadNum() {
        return 5;
    }

    @Override
    protected long getTakeTime() {
        return 950;
    }
}
