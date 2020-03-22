/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2016 All Rights Reserved.
 */
package com.didiglobal.sds.client.service;


import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.contant.BizConstant;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.client.util.AssertUtil;
import com.didiglobal.sds.client.util.DateUtils;
import org.slf4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 降级延迟服务类
 *
 * @author manzhizhen
 * @version $Id: SdsDowngradeDelayService.java, v 0.1 2016年1月23日 下午1:31:12
 * Administrator Exp $
 */
final public class SdsDowngradeDelayService {

    private static Logger logger = SdsLoggerFactory.getDefaultLogger();

    private final static SdsDowngradeDelayService downgradeDelayService = new SdsDowngradeDelayService();

    /**
     * key-降级点，value-延迟时间器
     */
    private ConcurrentHashMap<String, DelayTimer> pointDelayMap = new ConcurrentHashMap<>();

    private static ThreadLocal<Map<String, Boolean>> pointRetryInvoke = new ThreadLocal<Map<String, Boolean>>() {
        @Override
        protected Map<String, Boolean> initialValue() {
            return new HashMap<String, Boolean>();
        }
    };

    private SdsDowngradeDelayService() {
    }

    public static SdsDowngradeDelayService getInstance() {
        return downgradeDelayService;
    }

    /**
     * 新增一个降级点延迟
     *
     * @param point
     * @param delayTime
     */
    public void addPointDelay(String point, long delayTime) {
        addPointDelay(point, delayTime, -1);
    }

    /**
     * 新增一个降级点延迟
     *
     * @param point
     * @param delayTime
     * @param retryInterval
     */
    public void addPointDelay(String point, long delayTime, long retryInterval) {
        DelayTimer delayTimer = new DelayTimer(delayTime, retryInterval);
        delayTimer = pointDelayMap.putIfAbsent(point, delayTimer);
        if (delayTimer == null) {
            logger.info("SdsDowngradeDelayService 新增一个降级点延迟：" + point + " - "
                    + delayTimer);

        } else {
            logger.warn("SdsDowngradeDelayService 新增降级点延迟失败，因为降级点延迟已经存在："
                    + point + " - " + delayTimer);

        }
    }

    /**
     * 根据最新的策略来更新降级延长信息
     * 该方法由com.kuaidadi.liangjian.sds.client.service.SdsStrategyService调用
     * <p>
     * 非线程安全
     *
     * @param strategyMap
     */
    public void addOrUpdatePointDelay(
            ConcurrentHashMap<String, SdsStrategy> strategyMap) {
        ConcurrentHashMap<String, DelayTimer> newPointDelayMap = new ConcurrentHashMap<>();
        for (Entry<String, SdsStrategy> entry : strategyMap.entrySet()) {
            SdsStrategy sdsStrategy = entry.getValue();
            String point = entry.getKey();

            /**
             * 如果降级延长时间小于0，则表明没有降级延长等特性
             */
            if (sdsStrategy.getDelayTime() == null || sdsStrategy.getDelayTime() < 0) {
                continue;
            }

            try {
                DelayTimer oldDelayTimer = pointDelayMap.get(point);
                if (oldDelayTimer == null) {
                    newPointDelayMap.put(point, new DelayTimer(sdsStrategy.getDelayTime(),
                            sdsStrategy.getRetryInterval()));

                    // 如果该降级点本来就有降级延迟，则直接更新DelayTimer的各个属性
                } else {
                    oldDelayTimer.update(sdsStrategy.getDelayTime(),
                            sdsStrategy.getRetryInterval());
                    newPointDelayMap.put(point, oldDelayTimer);
                }

            } catch (IllegalArgumentException e) {
                logger.warn(
                        "SdsDowngradeDelayService updatePointDelay 新增降级延迟异常，降级点："
                                + point, e);
            }

        }

        pointDelayMap = newPointDelayMap;
    }

    /**
     * 该降级点是否应该降级延迟
     *
     * @param point
     * @param now
     * @return
     */
    public boolean isDowngradeDelay(String point, long now) {
        DelayTimer delayTimer = pointDelayMap.get(point);
        if (delayTimer == null) {
            return false;
        }

        return delayTimer.isDowngreadeEffect(now);

    }

    /**
     * 降级延迟中重试请求判断
     *
     * @param point
     * @param now
     * @return false：如果该请求不属于重试请求 true：如果该请求属于重试请求
     */
    public boolean retryChoice(String point, long now) {
        DelayTimer delayTimer = pointDelayMap.get(point);
        if (delayTimer == null) {
            return false;
        }

        return delayTimer.canRetry(point, now);
    }

    /**
     * 重设降级延迟时间
     *
     * @param point
     * @param curDate
     */
    public void resetExpireTime(String point, long curDate) {
        DelayTimer delayTimer = pointDelayMap.get(point);
        if (delayTimer != null) {
            if (delayTimer.resetDelayTime(curDate)) {
                delayTimer.resetRetryTime(curDate);
                delayTimer.resetRetryTimes();
            }

        }
    }

    /**
     * 重试成功，停止降级延迟
     *
     * @param point
     * @param invokeTime
     */
    public void stopDowngradeDelay(String point, Date invokeTime) {
        Map<String, Boolean> pointMap = pointRetryInvoke.get();

        Boolean value = pointMap.get(point);
        if (value == null) {
            value = Boolean.valueOf(false);
            pointMap.put(point, value);

            return;
        }

        if (value) {
            DelayTimer delayTimer = pointDelayMap.get(point);
            if (delayTimer != null) {

                delayTimer.stopDelay(invokeTime);

                logger.info("SdsDowngradeDelayService 关闭降级延迟状态:" + point);
            }

            pointMap.put(point, Boolean.valueOf(false));
            logger.info("SdsDowngradeDelayService 重试请求成功：" + point);

        }
    }

    /**
     * 重试失败，继续降级延迟
     *
     * @param point
     * @param invokeTime
     */
    public void continueDowngradeDelay(String point, Date invokeTime) {
        continueDowngradeDelay(point, invokeTime.getTime());
    }

    /**
     * 重试失败，继续降级延迟
     *
     * @param point
     * @param invokeTime
     */
    public void continueDowngradeDelay(String point, long invokeTime) {
        Map<String, Boolean> pointMap = pointRetryInvoke.get();

        Boolean value = pointMap.get(point);
        if (value == null) {
            pointMap.put(point, Boolean.valueOf(false));

            return;
        }

        if (value) {

            DelayTimer delayTimer = pointDelayMap.get(point);
            if (delayTimer != null) {

                delayTimer.resetRetryTime(invokeTime);

                delayTimer.resetRetryTimes();

                logger.info("SdsDowngradeDelayService 重设【重试时间】和【重试次数】：" + point);
            }

            pointMap.put(point, Boolean.valueOf(false));
            logger.info("SdsDowngradeDelayService 重试请求失败：" + point);
        }
    }

    public ConcurrentHashMap<String, DelayTimer> getPointDelayMap() {
        return pointDelayMap;
    }

    static class DelayTimer {

        /**
         * 降级延迟时间
         */
        private AtomicLong delayTime;
        /**
         * 降级延迟期间重试时间周期，如果<=0表示不重试。
         */
        private AtomicLong retryInterval;
        /**
         * 降级延迟期间重试时间增长比例，默认为1.
         */
        // private long retryIntervalRatio;
        /**
         * 重试次数，默认为1次
         */
        // private long retryTimes;

        /**
         * 本次过期时间
         */
        private transient AtomicLong expireTime = new AtomicLong();
        /**
         * 本次重试过期时间
         */
        private transient AtomicLong retryExpireTime = new AtomicLong();
        /**
         * 上一次重试间隔时间
         */
        // private transient AtomicLong lastRetryInterval = new AtomicLong();
        /**
         * 当前降级延迟期间内降级重试时间段内还剩的重试次数
         */
        private transient AtomicLong currRetryTimes = new AtomicLong();

        public DelayTimer(long delayTime, long retryInterval) {
            checkParam(delayTime, retryInterval);

            this.delayTime = new AtomicLong(delayTime);
            this.retryInterval = new AtomicLong(retryInterval);

            init();
        }

        public void update(long delayTime, long retryInterval) {
            checkParam(delayTime, retryInterval);

            this.delayTime = new AtomicLong(delayTime);
            this.retryInterval = new AtomicLong(retryInterval);
        }

        private void checkParam(long delayTime, long retryInterval) {
            AssertUtil.greaterThanOrEqual(delayTime, BizConstant.CYCLE_BUCKET_NUM * 1000, "降级延迟时间不能小于" +
                    BizConstant.CYCLE_BUCKET_NUM + "秒钟！");
            if (retryInterval > 0) {
                AssertUtil.greaterThanOrEqual(retryInterval, 1000, "降级延迟重试时间不能小于1秒钟！");

            }

            if (retryInterval > delayTime) {
                throw new IllegalArgumentException("降级延迟重试时间不能大于降级延迟时间！");
            }
        }

        /**
         * 降级是否有效
         *
         * @param time
         * @return
         */
        public boolean isDowngreadeEffect(long time) {
            return expireTime.get() >= time;
        }

        /**
         * 重设过期时间
         *
         * @param curDate
         * @return
         */
        public boolean resetDelayTime(long curDate) {
            long expireValue = expireTime.get();
            if (expireValue < curDate) {
                long value = curDate + delayTime.get();
                boolean res = expireTime.compareAndSet(expireValue, value);
                if (res) {
                    logger.debug("SdsDowngradeDelayService 重置【expireTime】:"
                            + DateUtils.format(new Date(value),
                            DateUtils.DATE_TIME_FORMAT));
                }

                return res;

            }

            return false;

        }

        /**
         * 重设重试过期时间
         *
         * @param curTime
         */
        public void resetRetryTime(long curTime) {
            if (hasRetryTime()) {
                long value = curTime + retryInterval.get();
                retryExpireTime.set(value);

            }

        }

        /**
         * 重设重试次数
         */
        public void resetRetryTimes() {
            currRetryTimes.set(1);
        }

        /**
         * 是否应该重试
         *
         * @param point
         * @param curDate
         * @return
         */
        public boolean canRetry(String point, long curDate) {
            if (!hasRetryTime()) {
                return false;
            }

            /**
             * 只有当前时间在降级延迟时间内，且超过了降级重试时间段，且降级重试次数还有的情况下，才认定为重试
             */
            if (expireTime.get() >= curDate && curDate >= retryExpireTime.get()) {

                long value = currRetryTimes.get();
                if (value > 0) {
                    if (currRetryTimes.compareAndSet(value, value - 1)) {

                        // 标记重试invoke
                        pointRetryInvoke.get().put(point, Boolean.valueOf(true));

                        return true;
                    }
                }
            }

            return false;
        }

        /**
         * 将延迟状态关闭
         *
         * @param invokeTime 调用时间
         * @return
         */
        public void stopDelay(Date invokeTime) {
            // 将降级延迟的有效期比当前时间小一秒
            expireTime.set(invokeTime.getTime() - 1000);

        }

        /**
         * 是否有重试时间
         *
         * @return
         */
        private boolean hasRetryTime() {
            // 如果重试时间小于等于0，表示不重试
            return retryInterval.get() > 0;

        }

        private void init() {
            long now = new Date().getTime();
            expireTime.set(now);
            retryExpireTime.set(now);
            // currRetryTimes.set(1);
        }

        public long getDelayTime() {
            return delayTime.get();
        }

        public long getRetryInterval() {
            return retryInterval.get();
        }

        public AtomicLong getExpireTime() {
            return expireTime;
        }

        public AtomicLong getRetryExpireTime() {
            return retryExpireTime;
        }

        public AtomicLong getCurrRetryTimes() {
            return currRetryTimes;
        }

        @Override
        public String toString() {
            return "DelayTimer [delayTime=" + delayTime + ", retryInterval="
                    + retryInterval + ", expireTime=" + expireTime
                    + ", retryExpireTime=" + retryExpireTime
                    + ", currRetryTimes=" + currRetryTimes + "]";
        }
    }
}
