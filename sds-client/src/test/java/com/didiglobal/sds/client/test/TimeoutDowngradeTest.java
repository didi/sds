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
public class TimeoutDowngradeTest extends AbstractDowngradeTest {

    private final static long TIMEOUT_THRESHOLD = 940L;
    private final static long TIMEOUT_COUNT_THRESHOLD = 5L;



    @Test
    public void test() {

        /**
         * 我们先确保业务方法执行耗时要高于设置的阈值，确保每次业务方法调用都会被SDS判定为超时
         */
        Assert.assertTrue(getTakeTime() > TIMEOUT_THRESHOLD);

        executor();

        waitResult();

        PowerfulCycleTimeCounter powerfulCycleTimeCounter =
                SdsPowerfulCounterService.getInstance().getPointCounterMap().get(getPoint());
        Assert.assertNotNull(powerfulCycleTimeCounter);

        /**
         * 加入1个线程，共调用10次业务方法
         * 有由于我们设定的场景是每次调用业务方法都会超时，而超时量阈值是5，所以被降级的请求数应该是 10 - 5 = 5，
         * 但实际情况是，超时量和异常量降级方式类似，都是业务方法执行完后我们才知道是否超时或抛异常，
         * 所以第6次吊哦用业务方法时由于统计的超时量是5，即第6次调用业务方法并不会触发降级，所以是1个线程时，降级量应该是 10 - 5 - 1 = 4
         *
         * 同理，如果是2个线程，也是一样的道理，降级量应该是 20 - 5 - 1 = 14
         */
        Assert.assertEquals(getExecutorTimes() * getThreadNum() - TIMEOUT_COUNT_THRESHOLD - 1,
                powerfulCycleTimeCounter.getLastCycleDowngradeValue(System.currentTimeMillis()));

    }

    @Override
    protected void initStrategy() {
        ConcurrentHashMap<String, SdsStrategy> strategyMap = new ConcurrentHashMap<>();

        SdsStrategy strategy = new SdsStrategy();
        strategy.setPoint(getPoint());

        strategy.setTimeoutThreshold(TIMEOUT_THRESHOLD);
        strategy.setTimeoutCountThreshold(TIMEOUT_COUNT_THRESHOLD);
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
        return "timeoutPoint";
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
