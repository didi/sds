package com.didiglobal.sds.client.test;

import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.SdsClientFactory;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by yizhenqiang on 18/11/19.
 */
public class TempTest {

    protected static final String SERVER_URL = "http://10.179.100.222:8887";
    protected static SdsClient sdsClient = SdsClientFactory.getOrCreateSdsClient("黑马", "mzz-study", SERVER_URL);

    @Test
    public void strategyTest() {

        try {
            TimeUnit.SECONDS.sleep(1000L);
        } catch (InterruptedException e) {

        }

    }
}
