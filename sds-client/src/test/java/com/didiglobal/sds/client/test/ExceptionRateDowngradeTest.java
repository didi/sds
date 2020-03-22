package com.didiglobal.sds.client.test;

import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.counter.PowerfulCycleTimeCounter;
import com.didiglobal.sds.client.service.SdsPowerfulCounterService;
import com.didiglobal.sds.client.service.SdsStrategyService;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 异常率降级测试
 * <p>
 * Created by manzhizhen on 2016/4/24.
 */
public class ExceptionRateDowngradeTest extends AbstractDowngradeTest {

    private final static int EXCEPTION_RATE_THRESHOLD = 50;

    private final static long EXCEPTION_RATE_THRESHOLD_START = 5;


    @Test
    public void test() {
        hasException = true;

        executor();

        waitResult();

        PowerfulCycleTimeCounter powerfulCycleTimeCounter =
                SdsPowerfulCounterService.getInstance().getPointCounterMap().get(getPoint());
        Assert.assertNotNull(powerfulCycleTimeCounter);


        long visitNum = powerfulCycleTimeCounter.getLastCycleVisitValue(System.currentTimeMillis());

        // 只有访问量大于异常率起始值，异常率降级策略才会生效
        Assert.assertTrue(visitNum >= EXCEPTION_RATE_THRESHOLD_START);

        /**
         * 注意：异常率降级实际分析时会比想象的要复杂，为了简单起见，本单元测试使用1个线程来实现
         *
         * 1个线程，共执行10次，所以业务方法会抛10个异常，我们的异常率阈值是50%，异常率起始值是5（说明访问量大于5才能触发异常率降级）
         *
         *  原因解释：
         * 当1个线程第1次调用业务方法时，访问量最终是1，每次都抛异常，但访问量小于等于异常率起始值5，所以策略不会生效；
         * 当1个线程第2次调用业务方法时，访问量最终是2，每次都抛异常，但访问量小于等于异常率起始值5，所以策略不会生效；
         * 当1个线程第3次调用业务方法时，访问量最终是3，每次都抛异常，但访问量小于等于异常率起始值5，所以策略不会生效；
         * 当1个线程第4次调用业务方法时，访问量最终是4，每次都抛异常，但访问量小于等于异常率起始值5，所以策略不会生效；
         * 当1个线程第5次调用业务方法时，访问量最终是5，每次都抛异常，但访问量小于等于异常率起始值5，所以策略不会生效；
         * 当1个线程第6次调用业务方法时，访问量最终是6，每次都抛异常，访问量大于异常率起始值5，调用时实际异常率为5/6大于异常率阈值50%，所以请求被降级；
         * 当1个线程第7次调用业务方法时，访问量最终是7，每次都抛异常，访问量大于异常率起始值5，调用时实际异常率为5/6大于异常率阈值50%，所以请求被降级；
         * 当1个线程第8次调用业务方法时，访问量最终是8，每次都抛异常，访问量大于异常率起始值5，调用时实际异常率为5/6大于异常率阈值50%，所以请求被降级；
         * 当1个线程第9次调用业务方法时，访问量最终是9，每次都抛异常，访问量大于异常率起始值5，调用时实际异常率为5/6大于异常率阈值50%，所以请求被降级；
         * 当1个线程第10次调用业务方法时，访问量最终是10，每次都抛异常，访问量大于异常率起始值5，调用时实际异常率为5/6大于异常率阈值50%，所以请求被降级；
         *
         * 所以总降级量是5。
         *
         * 有人会奇怪，第6~10次调用，计算调用时实际异常率时为啥分母总是6，不应该是6~10吗？这是因为异常率的分母为【访问量-降级量】，
         * 之所以要移除降级量是因为降级时没有实际调用业务方法，如果不移除，将使异常率计算值偏低
         */
        Assert.assertEquals(getExecutorTimes() * EXCEPTION_RATE_THRESHOLD / 100,
                powerfulCycleTimeCounter.getLastCycleDowngradeValue(System.currentTimeMillis()));

    }

    @Override
    public void initStrategy() {
        ConcurrentHashMap<String, SdsStrategy> strategyMap = new ConcurrentHashMap<>();

        SdsStrategy strategy = new SdsStrategy();
        strategy.setPoint(getPoint());
        strategy.setExceptionRateStart(EXCEPTION_RATE_THRESHOLD_START);
        strategy.setExceptionRateThreshold(EXCEPTION_RATE_THRESHOLD);
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
        return "exceptionRatePoint";
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
