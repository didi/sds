package com.didiglobal.sds.client;

import com.didiglobal.sds.client.bean.CheckData;
import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.bean.VisitWrapperValue;
import com.didiglobal.sds.client.config.SdsDowngradeActionNotify;
import com.didiglobal.sds.client.enums.DowngradeActionType;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.client.service.*;
import com.didiglobal.sds.client.strategy.DefaultStrategyExecutorBuilder;
import com.didiglobal.sds.client.strategy.StrategyExecutorBuilder;
import com.didiglobal.sds.client.strategy.executor.AbstractStrategyExecutor;
import com.didiglobal.sds.client.util.AssertUtil;
import com.didiglobal.sds.client.util.TimeStatisticsUtil;
import org.slf4j.Logger;

import java.util.Date;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Sds服务降级客户端
 * <p>
 * 适用于全新的全能降级点
 * Created by manzhizhen on 2016/6/11.
 */
public class CommonSdsClient extends AbstractSdsClient {

    private Logger logger = SdsLoggerFactory.getDefaultLogger();

    private ThreadLocal<Long> downgradeStartTime = new ThreadLocal<Long>();

    /**
     * 判断策略链条
     */
    private volatile AbstractStrategyExecutor strategyExecutorChain;

    CommonSdsClient(String appGroupName, String appName, String serverAddrList) {
        super(appGroupName, appName, serverAddrList);

        // 初始化策略链条
        initStrategyChain();

        // 初始化心跳线程
        initHeartBeat(appGroupName, appName, serverAddrList);
    }

    /**
     * 降级入口
     *
     * @param point 降级点
     * @return 是否需要降级，true-需要降级，false-不需要降级
     */
    @Override
    public boolean shouldDowngrade(String point) {
        AssertUtil.notBlack(point, "降级点不能为空！");

        try {
            long now = System.currentTimeMillis();

            /**
             * 记录开始时间
             */
            TimeStatisticsUtil.startTime();

            /**
             * 传递now到{@link this#downgradeFinally(String)}方法
             */
            downgradeStartTime.set(now);

            SdsPowerfulCounterService sdsPowerfulCounterService = SdsPowerfulCounterService.getInstance();

            VisitWrapperValue visitWrapperValue = sdsPowerfulCounterService.visitInvokeAddAndGet(point, now);
            // TODO 这里需要优化，看能否不用每次都去取
            long lastSecondVisitCount = sdsPowerfulCounterService.getLastSecondVisitBucketValue(point, now);
            boolean concurrentAcquire = sdsPowerfulCounterService.concurrentAcquire(point, now);
            long exceptionCount = sdsPowerfulCounterService.getExceptionInvoke(point, now);
            long timeoutCount = sdsPowerfulCounterService.getTimeoutInvoke(point, now);
            long takeTokenBucketNum = sdsPowerfulCounterService.tokenBucketInvokeAddAndGet(point, now);
            long downgradeCount = sdsPowerfulCounterService.getCurCycleDowngrade(point, now);

            return judge(point, visitWrapperValue.getSlidingCycleValue(), visitWrapperValue.getBucketValue(),
                    lastSecondVisitCount, concurrentAcquire, exceptionCount, timeoutCount, takeTokenBucketNum,
                    downgradeCount, now);

        } catch (Exception e) {
            logger.warn("CommonSdsClient#downgradeEntrance " + point + " 异常", e);
            return false;
        }
    }

    /**
     * 降级出口
     *
     * @param point 降级点
     */
    @Override
    public void downgradeFinally(String point) {
        try {
            // （非降级）业务处理耗时
            long consumeTime = TimeStatisticsUtil.getConsumeTime();

            // 并发数减1
            SdsPowerfulCounterService.getInstance().concurrentRelease(point);

            SdsStrategy strategy = SdsStrategyService.getInstance().getStrategy(point);
            if (strategy == null) {
                return;
            }

            Long timeoutThreshold = strategy.getTimeoutThreshold();
            /**
             * 如果耗时超过设定的超时时间{@link SdsStrategy#timeout}，则超时计数器+1
             */
            if (timeoutThreshold != null && timeoutThreshold > 0 && consumeTime > timeoutThreshold) {

                Long downgradeStartTimeValue = downgradeStartTime.get();
                if (downgradeStartTimeValue == null) {
                    logger.warn("CommonSdsClient downgradeExit: 调用 downgradeFinally 之前请先调用 shouldDowngrade ！");
                    return;
                }

                SdsPowerfulCounterService.getInstance().timeoutInvokeAddAndGet(point, downgradeStartTimeValue);
            }

        } finally {
            downgradeStartTime.set(null);
        }
    }

    /**
     * 根据SdsDowngradePointFactory配置的异常列表，来判断是否增加异常量访问计数器
     * <p>
     * 注意：用户可以通过 {@see SdsPointStrategyConfig#setDowngradeExceptions(String, java.util.List)}
     * 和  {@see SdsPointStrategyConfig#setDowngradeExceptExceptions(String, java.util.List)}
     * 来设置失败异常列表和排除异常列表。
     *
     * @param point     降级点
     * @param exception 如果exception为null，则直接进行异常量计数器+1
     * @return 当前异常数
     */
    @Override
    public void exceptionSign(String point, Throwable exception) {
        AssertUtil.notBlack(point, "降级点不能为空！");

        try {
            Long startTime = downgradeStartTime.get();

            // 先判断该异常是否属于失败异常
            if (exception != null && SdsDowngradeExceptionService.getInstance().isDowngradeException(point,
                    exception)) {
                SdsDowngradeDelayService.getInstance().continueDowngradeDelay(point, System.currentTimeMillis());

                // 异常计数器加1
                SdsPowerfulCounterService.getInstance().exceptionInvokeAddAndGet(point, startTime);

            }

        } catch (Exception e) {
            logger.warn("CommonSdsClient downgradeCaptureExceptionAndExit " + point + " 异常", e);
        }
    }

    /**
     * 降级判断，是否需要降级
     *
     * @param point                降级点
     * @param visitCount           （当前滑动周期内）访问量调用数
     * @param curSecondVisitCount  当前秒的访问量
     * @param lastSecondVisitCount 前1秒的访问量
     * @param concurrentAcquire    并发限制结果
     * @param exceptionCount       （当前滑动周期内）异常数量
     * @param timeoutCount         （当前滑动周期内）超时量
     * @param takeTokenBucketNum   当前秒的令牌桶计数器
     * @param downgradeCount       （当前滑动周期内）降级量
     * @param time                 UTC时间毫秒数
     * @return 是否需要降级，true-是，false-否
     */
    private boolean judge(String point, long visitCount, long curSecondVisitCount, long lastSecondVisitCount,
                          boolean concurrentAcquire, long exceptionCount, long timeoutCount, long takeTokenBucketNum,
                          long downgradeCount, long time) {

        // 获取配置的策略：规则和触犯规则后的动作
        SdsStrategy strategy = SdsStrategyService.getInstance().getStrategy(point);
        if (null == strategy) {
            return false;
        }


        DowngradeActionType downgradeActionType = null;
        if (!SdsDowngradeDelayService.getInstance().isDowngradeDelay(point, time)) {

            /**
             * 构建执行策略检查的参数
             */
            CheckData checkData = new CheckData();
            checkData.setPoint(point);
            checkData.setTime(time);
            checkData.setVisitCount(visitCount);
            checkData.setCurSecondVisitCount(curSecondVisitCount);
            checkData.setLastSecondVisitCount(lastSecondVisitCount);
            checkData.setConcurrentAcquire(concurrentAcquire);
            checkData.setExceptionCount(exceptionCount);
            checkData.setTimeoutCount(timeoutCount);
            checkData.setTakeTokenBucketNum(takeTokenBucketNum);
            checkData.setDowngradeCount(downgradeCount);

            /**
             * 执行策略链，如果通过了策略中所有的规则，则不需要降级
             */
            if ((downgradeActionType = strategyExecutorChain.execute(checkData, strategy)) == null) {
                return false;
            }

            /**
             * 访问量第一次超过阈值时，需要重设延迟日期
             * 注意：这里会有一个小问题，当降级延迟结束时，如果此时的调用量已经超过阈值，将不会立马出发新一次的降级延迟，不
             * 过即使没有出发新的降级延迟，接下来的请求由于超过阈值，也会处于降级状态中，所以影响不大
             */
            //            if (reqCount == threshhold + 1) {
            //                logger.debug("SdsClient 降级点重置延迟日期:" + point);
            // 如果访问量超过阈值，则需要去看该降级点是否有降级延迟，如果有配置降级延迟，则降级延迟开始
            SdsDowngradeDelayService.getInstance().resetExpireTime(point, time);

        } else {
            if (SdsDowngradeDelayService.getInstance().retryChoice(point, time)) {
                logger.info("CommonSdsClient 本次请求是降级延迟的重试请求：" + point);
                return false;
            }
        }

        boolean needDowngrade;
        /**
         * 小于等于0表示不需要降级
         */
        if (strategy.getDowngradeRate() <= 0) {
            needDowngrade = false;

            /**
             * 大于等于100说明百分百降级
             */
        } else if (strategy.getDowngradeRate() >= 100) {
            needDowngrade = true;

        } else {
            needDowngrade = ThreadLocalRandom.current().nextInt(100) < strategy.getDowngradeRate();
        }

        if (needDowngrade) {
            // 降级计数器+1
            addDowngradeCount(point, time, downgradeActionType);
        }

        return needDowngrade;
    }

    /**
     * 降级次数+1
     *
     * @param point               降级点
     * @param time                UTC到当前毫秒数
     * @param downgradeActionType 被降级的类型
     */
    private void addDowngradeCount(String point, long time, DowngradeActionType downgradeActionType) {
        SdsPowerfulCounterService.getInstance().downgradeAddAndGet(point, time);

        /**
         * 通知降级后的自定义处理逻辑
         */
        SdsDowngradeActionNotify.notify(point, downgradeActionType, new Date(time));
    }

    /**
     * 初始化策略
     */
    private void initStrategyChain() {
        // 默认使用用户自定义的
        String customStrategyExecutorBuilderName = "";
        try {
            ServiceLoader<StrategyExecutorBuilder> strategyExecutorBuilders = ServiceLoader.load(StrategyExecutorBuilder.class);
            Iterator<StrategyExecutorBuilder> strategyExecutorBuilderIterator = strategyExecutorBuilders.iterator();
            // 注意这里用的是 if, 即只会找到第一个
            if (strategyExecutorBuilderIterator.hasNext()) {
                StrategyExecutorBuilder next = strategyExecutorBuilderIterator.next();
                customStrategyExecutorBuilderName = next.getClass().getName();
                strategyExecutorChain = next.build();
            }
        } catch (Exception e) {
            // 如果构建策略链出错了, 打印 warn 日志提醒, 在下面重新构建策略链
            logger.warn("CommonSdsClient initStrategyChain occur error, it will use default strategy executor chain", e);
        }
        // 构建默认策略链
        if (strategyExecutorChain == null) {
            logger.info("CommonSdsClient use default strategy executor chain");
            strategyExecutorChain = new DefaultStrategyExecutorBuilder().build();
        } else {
            logger.info("CommonSdsClient use custom strategy executor chain: {}", customStrategyExecutorBuilderName);
        }
    }

    /**
     * 初始化心跳功能
     *
     * @param appGroupName
     * @param appName
     * @param serverAddrList
     */
    private void initHeartBeat(String appGroupName, String appName, String serverAddrList) {
        SdsHeartBeatService.createOnlyOne(appGroupName, appName, serverAddrList);
    }
}
