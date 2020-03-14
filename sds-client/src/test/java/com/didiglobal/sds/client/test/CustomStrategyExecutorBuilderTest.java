package com.didiglobal.sds.client.test;

import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.SdsClientFactory;
import com.didiglobal.sds.client.strategy.executor.AbstractStrategyExecutor;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * <p>description : CustomStrategyExecutorBuilderTest
 *
 * @author : masteryourself
 * @version : 1.0.1
 * @date : 2020/3/14 12:03
 */
public class CustomStrategyExecutorBuilderTest {

    @Test
    public void testInitStrategyChain() throws Exception {
        SdsClient client = SdsClientFactory.getOrCreateSdsClient("demo", "demo", "127.0.0.1/sds/heartbeat/pullstrategy");
        Field strategyExecutorChain = client.getClass().getDeclaredField("strategyExecutorChain");
        strategyExecutorChain.setAccessible(true);
        AbstractStrategyExecutor executor = (AbstractStrategyExecutor) strategyExecutorChain.get(client);
        int count = 0;
        while (executor != null) {
            executor = executor.getNext();
            count++;
        }
        Assert.assertEquals(6, count);
    }

}
