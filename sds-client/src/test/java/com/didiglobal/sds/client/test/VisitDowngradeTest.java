package com.didiglobal.sds.client.test;

import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.counter.PowerfulCycleTimeCounter;
import com.didiglobal.sds.client.service.SdsPowerfulCounterService;
import com.didiglobal.sds.client.service.SdsStrategyService;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 访问量降级测试
 * <p>
 * Created by manzhizhen on 2016/4/24.
 */
public class VisitDowngradeTest extends AbstractDowngradeTest {

    private final static long VISITT_HRESHOLD = 5L;

    @Test
    public void test() {

        executor();

        waitResult();

        PowerfulCycleTimeCounter powerfulCycleTimeCounter =
                SdsPowerfulCounterService.getInstance().getPointCounterMap().get(getPoint());
        Assert.assertNotNull(powerfulCycleTimeCounter);

        Assert.assertEquals(getExecutorTimes() * getThreadNum() - VISITT_HRESHOLD,
                powerfulCycleTimeCounter.getLastCycleDowngradeValue(System.currentTimeMillis()));

    }

    @Override
    protected void initStrategy() {
        ConcurrentHashMap<String, SdsStrategy> strategyMap = new ConcurrentHashMap<>();

        SdsStrategy strategy = new SdsStrategy();
        strategy.setPoint(getPoint());

        strategy.setVisitThreshold(VISITT_HRESHOLD);
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
        return "visitPoint";
    }

    @Override
    protected int getThreadNum() {
        return 1;
    }

    @Override
    protected long getTakeTime() {
        return 950;
    }
}
