package com.didiglobal.sds.client.test;

import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.SdsClientFactory;
import com.didiglobal.sds.client.service.CycleDataService;
import org.junit.Before;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by manzhizhen on 17/4/19.
 */
public abstract class AbstractDowngradeTest {

    protected static final int DOWNGRADE_RATE = 100;

    protected static final String SERVER_URL = "http://127.0.0.1:8887";
    protected static SdsClient sdsClient = SdsClientFactory.getOrCreateSdsClient("BikeBusinessDepartment", "order", SERVER_URL);

    // 业务方法是否需要抛出异常
    protected static boolean hasException = false;

    private ExecutorService service = null;

    static {
        // 不从sds-admin获取策略配置，完全使用本地手动设置
        CycleDataService.setPullPointStrategySwitch(false);
        CycleDataService.setUploadDataSwitch(false);
    }

    /**
     * 初始化策略
     */
    @Before
    public void init() {

        // 关闭从服务端拉取降级点配置的功能，策略将由本地来自由设置
        CycleDataService.setPullPointStrategySwitch(false);

        initStrategy();

        service = new ThreadPoolExecutor(getThreadNum(), getThreadNum(), 1000L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(100));
    }

    public void executor() {

        long startTime = System.currentTimeMillis();
        try {
            // 保证从完整的10s开始执行
            Thread.sleep(10000 - startTime % 10000 + 100);
        } catch (InterruptedException e) {
        }

        long i = getThreadNum();
        while (i-- > 0) {
            service.submit(new ClientThread());
        }
    }

    /**
     * 模拟接入了SDS的业务方法
     *
     * @return true-执行了正常的业务逻辑， false-被降级了
     */
    protected boolean businessMethod() {
        try {
            if (sdsClient.shouldDowngrade(getPoint())) {
                // 降级后也睡眠同样的时间，便于计算降级量
                sleepMilliseconds(getTakeTime());
                return false;
            }

            // 模拟正常业务逻辑耗时
            sleepMilliseconds(getTakeTime());

            // 如果业务逻辑设置成需要抛异常，那么就抛异常
            if (hasException) {
                throw new IllegalStateException("异常抛了！");
            }

        } catch (Exception e) {
            // 这里用于统计异常量
            sdsClient.exceptionSign(getPoint(), e);
            // 记得捕获完还得抛出去，偷偷吃掉麻烦就大了
            throw e;

        } finally {
            // 回收资源，一般在finally代码中调用
            sdsClient.downgradeFinally(getPoint());
        }

        return true;
    }

    protected abstract void initStrategy();

    /**
     * 获取降级点名称
     *
     * @return
     */
    protected abstract String getPoint();

    /**
     * 获取执行的线程数
     *
     * @return
     */
    protected abstract int getThreadNum();

    /**
     * 获取每个线程执行业务方法的次数
     *
     * @return
     */
    protected abstract long getExecutorTimes();

    /**
     * 业务方法的处理耗时
     * 单位：ms
     *
     * @return
     */
    protected abstract long getTakeTime();

    /**
     * 至少等1个完全周期，即10s，这里为了保险，等11s
     */
    protected void waitResult() {
        sleepMilliseconds(11000);
    }

    protected void sleepMilliseconds(long milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
        }
    }

    private class ClientThread implements Runnable {
        @Override
        public void run() {
            long executorTimes = getExecutorTimes();
            while (executorTimes-- > 0) {
                try {
                    businessMethod();
                } catch (Exception e) {
                }

            }
        }
    }

}
