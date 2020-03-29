package com.didiglobal.sds.client.service;


import com.didiglobal.sds.client.counter.AbstractCycleData;
import com.didiglobal.sds.client.contant.BizConstant;
import com.didiglobal.sds.client.counter.ConcurrentData;
import com.didiglobal.sds.client.counter.SlidingWindowData;
import com.didiglobal.sds.client.counter.TokenBucketData;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static com.didiglobal.sds.client.contant.BizConstant.BUCKET_TIME;
import static com.didiglobal.sds.client.contant.BizConstant.CYCLE_BUCKET_NUM;

/**
 * 周期数据统计服务
 * <p>
 * <p>
 * Created by manzhizhen on 2017/1/1.
 */
final public class CycleDataService {

    /**
     * 所有周期的统计数据
     */
    private final static CopyOnWriteArrayList<AbstractCycleData> allCycleTimeData = new CopyOnWriteArrayList<>();

    /**
     * 负责清理下个周期的数据并上传上一个周期的数据
     */
    private final static ScheduledExecutorService cleanAndUploadExecutor;

    /**
     * 从服务端拉取最新降级点策略
     */
    private final static ScheduledExecutorService pullPointStrategyExecutor;

    /**
     * 拉取最新策略开关
     * true-开启，false-关闭
     */
    private static volatile boolean pullPointStrategySwitch = true;

    /**
     * 向sds-admin发送统计数据开关
     * true-开启，false-关闭
     */
    private static volatile boolean uploadDataSwitch = true;

    static {
        cleanAndUploadExecutor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "SdsCleanAndUploadThread");
            }
        });
        // 注意，这里不使用scheduleAtFixedRate，因为从长期角度来讲，scheduleAtFixedRate没有每次任务执完去计算下次执行时间准
        cleanAndUploadExecutor.schedule(new CycleClearAndUploadTask(), calDistanceNextExecuteTime(),
                TimeUnit.MILLISECONDS);

        pullPointStrategyExecutor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "SdsPullPointStrategyThread");
            }
        });
        // 注意，拉取配置应该比心跳数据上传周期更短，这里就选择1/2周期，即每5秒一次
        pullPointStrategyExecutor.scheduleWithFixedDelay(new CyclePullPointStrategyTask(), 0,
                CYCLE_BUCKET_NUM * BUCKET_TIME / 2, TimeUnit.SECONDS);


    }

    private CycleDataService() {
    }

    /**
     * 新建一个周期数据对象
     *
     * @return
     */
    public static AbstractCycleData createCycleData() {
        AbstractCycleData abstractCycleData = new SlidingWindowData();
        allCycleTimeData.add(abstractCycleData);

        return abstractCycleData;
    }

    /**
     * 新建一个并发周期数据对象
     *
     * @return
     */
    public static ConcurrentData createConcurrentCycleData() {
        ConcurrentData concurrentData = new ConcurrentData();
        AbstractCycleData abstractCycleData = concurrentData.getCycleMaxConcurrentData();
        allCycleTimeData.add(abstractCycleData);

        return concurrentData;
    }

    /**
     * 新建一个令牌桶数据对象
     *
     * @return
     */
    public static TokenBucketData createTokenBucketCycleData() {
        TokenBucketData tokenBucketData = new TokenBucketData();
        AbstractCycleData abstractCycleData = tokenBucketData.getCycleTokenBucketData();
        allCycleTimeData.add(abstractCycleData);

        return tokenBucketData;
    }

    /**
     * 将上一统计周期的桶数据汇总写入CycleTimeData#lastMinuteTotalCount，并将下一统计周期的数据清零
     * 为了保证正确性，该任务应该在每次统计周期中间来执行
     */
    private static class CycleClearAndUploadTask implements Runnable {

        private Logger logger = SdsLoggerFactory.getDefaultLogger();

        @Override
        public void run() {

            try {
                long now = System.currentTimeMillis();

                // 在每10秒的第5秒会开始清理所有CycleTimeData下一周期的数据
                for (AbstractCycleData cycleTimeData : allCycleTimeData) {
                    if (cycleTimeData == null) {
                        continue;
                    }

                    cycleTimeData.clearNextCycleValue(now);
                }

            } catch (Exception e) {
                logger.warn("CycleClearAndUploadTask#run has exception:" + e.getMessage(), e);
            }

            /**
             * 执行心跳服务
             */
            SdsHeartBeatService sdsHeartBeatService = SdsHeartBeatService.getInstance();
            if (sdsHeartBeatService != null && uploadDataSwitch) {
                sdsHeartBeatService.uploadHeartbeatData();
            }

            /**
             * 这里再计算下一个任务的时间
             * 因为对时间精度要求比较高，所以不能用固定周期的方法
             * 注意：下一个清理任务由当前清理任务执行完后算出来，长期来看，这样的时间准确度更高
             */
            cleanAndUploadExecutor.schedule(this, calDistanceNextExecuteTime(), TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 周期性（每5秒）去服务端pull最新的降级点策略
     */
    private static class CyclePullPointStrategyTask implements Runnable {

        @Override
        public void run() {

            if (!pullPointStrategySwitch) {
                return;
            }
            /**
             * 执行拉取降级点策略的任务
             */
            SdsHeartBeatService sdsHeartBeatService = SdsHeartBeatService.getInstance();
            if (sdsHeartBeatService != null) {
                sdsHeartBeatService.updatePointStrategyFromWebServer();
            }
        }
    }

    /**
     * 计算下一次任务执行的延迟时长
     * 为了保证正确性，{@link CycleClearAndUploadTask}任务应该在每次统计周期中间来执行，
     * 例如{@link BizConstant#CYCLE_BUCKET_NUM} 为10，
     * {@link BizConstant#BUCKET_TIME} 为1秒，那么{@link CycleClearAndUploadTask}应该在每分钟的5秒、15秒、25秒、35秒等时间来执行
     *
     * @return
     */
    private static long calDistanceNextExecuteTime() {
        long cycleTime = CYCLE_BUCKET_NUM * BUCKET_TIME * 1000;
        long now = System.currentTimeMillis();
        return (cycleTime - now % cycleTime) + cycleTime / 2;
    }

    public static void setPullPointStrategySwitch(boolean pullPointStrategySwitch) {
        CycleDataService.pullPointStrategySwitch = pullPointStrategySwitch;
    }

    public static void setUploadDataSwitch(boolean uploadDataSwitch) {
        CycleDataService.uploadDataSwitch = uploadDataSwitch;
    }
}
