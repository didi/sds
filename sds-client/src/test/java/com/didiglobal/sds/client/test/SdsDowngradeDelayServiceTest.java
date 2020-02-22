package com.didiglobal.sds.client.test;

import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.SdsClientFactory;
import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.service.SdsStrategyService;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 降级延长测试
 * Created by yizhenqiang on 17/4/19.
 */
public class SdsDowngradeDelayServiceTest extends AbstractDowngradeTest {

    private static final String SERVER_URL = "http://localhost:9080/sds-server/home/heartbeat/touch";
    private static SdsClient sdsClient = SdsClientFactory.getOrCreateSdsClient("didi", "test", SERVER_URL);

    @Test
    public void visDowngradeDelayTest() {
        executor();

        System.out.println("所有线程运行结束");
    }

    protected void initStrategy() {
        ConcurrentHashMap<String, SdsStrategy> strategyMap = new ConcurrentHashMap<>();

        SdsStrategy strategy = new SdsStrategy();
        strategy.setPoint(getPoint());
        strategy.setVisitThreshold(10L);
        strategy.setDowngradeRate(100);
        strategy.setDelayTime(20000L);

        strategyMap.put(getPoint(), strategy);

        SdsStrategyService.getInstance().reset(strategyMap);
    }

    @Override
    protected long getExecutorTimes() {
        return 1;
    }

    @Override
    protected String getPoint() {
        return "testPoint";
    }

    @Override
    protected int getThreadNum() {
        return 1;
    }

    @Override
    protected long getTakeTime() {
        return 0;
    }
}
