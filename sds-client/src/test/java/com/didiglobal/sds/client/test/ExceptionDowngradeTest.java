package com.didiglobal.sds.client.test;


import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.counter.PowerfulCycleTimeCounter;
import com.didiglobal.sds.client.service.SdsPowerfulCounterService;
import com.didiglobal.sds.client.service.SdsStrategyService;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 异常量降级测试
 * <p>
 * Created by manzhizhen on 2016/4/24.
 */
public class ExceptionDowngradeTest extends AbstractDowngradeTest {

    private final static long EXCEPTION_THRESHOLD = 5;

    @Test
    public void test() {
        hasException = true;

        executor();

        waitResult();

        PowerfulCycleTimeCounter powerfulCycleTimeCounter =
                SdsPowerfulCounterService.getInstance().getPointCounterMap().get(getPoint());
        Assert.assertNotNull(powerfulCycleTimeCounter);

        /**
         * 1个线程，共执行10次，所以业务方法会抛10个异常，我们的异常阈值是5，降级数量是10-5=5
         */
        Assert.assertEquals(getThreadNum() * getExecutorTimes() - EXCEPTION_THRESHOLD,
                powerfulCycleTimeCounter.getLastCycleDowngradeValue(System.currentTimeMillis()));

    }

    @Override
    protected long getExecutorTimes() {
        return 10;
    }

    @Override
    protected String getPoint() {
        return "exceptionPoint";
    }

    @Override
    protected int getThreadNum() {
        return 1;
    }

    @Override
    protected long getTakeTime() {
        return 950;
    }

    @Override
    public void initStrategy() {
        ConcurrentHashMap<String, SdsStrategy> strategyMap = new ConcurrentHashMap<>();

        SdsStrategy strategy = new SdsStrategy();
        strategy.setPoint(getPoint());
        strategy.setExceptionThreshold(EXCEPTION_THRESHOLD);

        strategyMap.put(getPoint(), strategy);

        SdsStrategyService.getInstance().resetAll(strategyMap);
    }

}
