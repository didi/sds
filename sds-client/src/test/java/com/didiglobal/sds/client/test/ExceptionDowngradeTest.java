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
 * Created by yizhenqiang on 2016/4/24.
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
         * 5个线程，每个线程共执行10次，所以业务方法会抛50个异常，我们的异常阈值是5，但降级数量并不会是50-5=45
         * 原因解释：和场景有关，本单元测试场景可以认为是5个线程一起执行业务方法，而且业务方法耗时也是一致的，所以实际上每个线程的第二次调用并不会触发降级，
         * 因为异常是业务方法执行中才抛出来的，影响的是每个线程的第三次调用，所以降级量应该是 50-5-5= 40
         */
        Assert.assertEquals(getThreadNum() * getExecutorTimes() - EXCEPTION_THRESHOLD - EXCEPTION_THRESHOLD,
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
        return 5;
    }

    @Override
    protected long getTakeTime() {
        return 1000;
    }

    @Override
    public void initStrategy() {
        ConcurrentHashMap<String, SdsStrategy> strategyMap = new ConcurrentHashMap<>();

        SdsStrategy strategy = new SdsStrategy();
        strategy.setPoint(getPoint());
        strategy.setExceptionThreshold(EXCEPTION_THRESHOLD);
        strategy.setDowngradeRate(DOWNGRADE_RATE);

        strategyMap.put(getPoint(), strategy);

        SdsStrategyService.getInstance().reset(strategyMap);
    }

}
