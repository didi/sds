package com.didiglobal.sds.client.test;

import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.SdsClientFactory;
import com.didiglobal.sds.client.contant.ExceptionCode;
import com.didiglobal.sds.client.exception.SdsException;
import com.didiglobal.sds.client.service.CycleDataService;
import org.junit.Before;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by yizhenqiang on 17/4/19.
 */
public abstract class AbstractDowngradeTest {

    protected static final String SERVER_URL = "http://10.179.100.222:8887";
    protected static SdsClient sdsClient = SdsClientFactory.getOrCreateSdsClient("黑马", "mzz-study", SERVER_URL);

    // 业务方法是否抛出异常
    protected static boolean hasException = false;

    private ExecutorService service = null;

    public void executor() {

        long startTime = System.currentTimeMillis();
        try {
            // 保证从完整的一分钟开始执行
            Thread.sleep(6000 - startTime % 6000);
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }

        long i = getThreadNum();
        while (i-- > 0) {
            service.submit(new ClientThread());
        }

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
        }
    }


    /**
     * 模拟业务方法
     * 如果执行了业务逻辑，则返回true，降级了则返回false
     *
     * @return
     */
    protected boolean businessMethod() {
        try {
            if (sdsClient.shouldDowngrade(getPoint())) {
                // 模拟正常业务逻辑
                try {
                    Thread.sleep(getTakeTime());
                } catch (Exception e) {

                }
                if (hasException) {
                    throw new IllegalStateException("异常跑了！");
                }

                return true;
            }


            // 降级后也睡眠同样的时间，便于计算降级量
            try {
                Thread.sleep(getTakeTime());
            } catch (Exception e) {
            }

            throw new SdsException(ExceptionCode.DOWNGRADE);


        } catch (Exception e) {
            // 这里用于统计异常量
            sdsClient.exceptionSign(getPoint(), e);
            // 记得捕获完还得抛出去，偷偷吃掉麻烦就大了
            throw e;

        } finally {
            // 回收资源，一般在finally代码中调用
            sdsClient.downgradeFinally(getPoint());
        }
    }

    /**
     * 初始化策略
     */
    @Before
    public void init() {

        // 关闭从服务端拉取降级点配置的功能，策略将由本地来自由设置
        CycleDataService.setPullPointStrategySwitch(false);

        initStrategy();

        service = new ThreadPoolExecutor(getThreadNum(), getThreadNum(), 99999L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1000));
    }

    protected abstract void initStrategy();

    /**
     * 获取降级点名称
     * @return
     */
    protected abstract String getPoint();

    /**
     * 获取执行的线程数
     * @return
     */
    protected abstract int getThreadNum();

    /**
     * 获取每个线程执行业务方法的次数
     * @return
     */
    protected abstract long getExecutorTimes();

    /**
     * 业务方法的处理耗时
     * @return
     */
    protected abstract long getTakeTime();

    private class ClientThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    businessMethod();
                } catch (Exception e) {
//                    e.printStackTrace();
                }

            }
        }
    }
}
