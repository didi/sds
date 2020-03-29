package com.didiglobal.sds.client.counter;


import com.didiglobal.sds.client.bean.VisitWrapperValue;
import com.didiglobal.sds.client.service.CycleDataService;

/**
 * 包含访问量、并发量、异常量、异常率、超时等特性的毫秒计数器
 * <p>
 * Created by manzhizhen on 2017/1/1.
 */
public class PowerfulCycleTimeCounter {

    /**
     * 秒访问量统计数据
     */
    private AbstractCycleData visitData = CycleDataService.createCycleData();

    /**
     * 并发量统计数据
     */
    private ConcurrentData concurrentData = CycleDataService.createConcurrentCycleData();

    /**
     * 秒异常量统计数据
     */
    private AbstractCycleData exceptionData = CycleDataService.createCycleData();

    /**
     * 超时计数器
     */
    private AbstractCycleData timeoutData = CycleDataService.createCycleData();

    /**
     * 令牌桶
     */
    private TokenBucketData tokenBucketData = CycleDataService.createTokenBucketCycleData();

    /**
     * 降级计数器
     */
    private AbstractCycleData downgradeData = CycleDataService.createCycleData();

    /**
     * 访问量计数器+1
     *
     * @param time UTC到当前时间的毫秒数
     * @return 访问量当前值
     */
    public VisitWrapperValue visitAddAndGet(long time) {
        return commonAddAndGet(time, visitData);
    }

    /**
     * 获取访问量指定时间上一秒的桶值
     *
     * @param time UTC到当前时间的毫秒数
     * @return
     */
    public long getLastSecondVisitBucketValue(long time) {
        return visitData.getBucketValue(time - 1000);
    }

    /**
     * 获取上上一周期访问调用量的统计值
     *
     * @return 上一周期访问调用量的统计值
     */
    public long getLastCycleVisitValue(long time) {
        return visitData.getLastWholeCycleValue(time);
    }

    /**
     * 并发调用量的当前毫秒的计数器加1，并且如果下一毫秒计数器不为0，则置为0
     *
     * @return 并发量
     */
    public boolean concurrentAcquire(long time) {
        return concurrentData.concurrentAcquire(time);
    }

    /**
     * 并发调用量的当前毫秒的计数器减1，并且如果下一毫秒计数器不为0，则置为0
     *
     * @return 并发量
     */
    public void concurrentRelease() {
        concurrentData.concurrentRelease();
    }

    /**
     * 获取上上一周期并发量的统计值
     *
     * @param time
     * @return 上一周期访问并发量的统计值
     */
    public int getLastCycleConcurrentValue(long time) {
        return concurrentData.getLastConcurrentMaxCountAndClean(time);
    }

    /**
     * 异常调用量的当前毫秒的计数器加1，并且如果下一毫秒计数器不为0，则置为0
     *
     * @param time UTC到当前时间的毫秒数
     * @return 异常次数
     */
    public long exceptionAddAndGet(long time) {
        return commonAddAndGet(time, exceptionData).getSlidingCycleValue();
    }

    /**
     * 获取指定时间异常调用量的值
     *
     * @param time UTC到当前时间的毫秒数
     * @return 异常次数
     */
    public long getExceptionValue(long time) {
        return exceptionData.getCurSlidingCycleValue(time);
    }

    /**
     * 获取上一周期异常量的统计值
     *
     * @return 上一周期访问异常量的统计值
     */
    public long getLastCycleExceptionValue(long time) {
        return exceptionData.getLastWholeCycleValue(time);
    }

    /**
     * 超时调用量的当前毫秒的计数器加1，并且如果下一毫秒计数器不为0，则置为0
     *
     * @param time UTC到当前时间的毫秒数
     * @return 超时次数
     */
    public long timeoutAddAndGet(long time) {
        return commonAddAndGet(time, timeoutData).getSlidingCycleValue();
    }

    /**
     * 获取指定时间超时调用量的值
     *
     * @param time UTC到当前时间的毫秒数
     * @return 超时次数
     */
    public long getTimeoutValue(long time) {
        return timeoutData.getCurSlidingCycleValue(time);
    }

    /**
     * 获取指定时间上上一周期的超时数量
     *
     * @param time
     * @return
     */
    public long getLastCycleTimeoutValue(long time) {
        return timeoutData.getLastWholeCycleValue(time);
    }

    /**
     * 尝试消耗一个令牌
     * @param time
     * @return
     */
    public long tokenBucketInvokeAddAndGet(long time) {
        return tokenBucketData.takeOneToken(time);
    }

    /**
     * 降级量的当前毫秒的计数器加1，并且如果下一毫秒计数器不为0，则置为0
     *
     * @param time UTC到当前时间的毫秒数
     * @return 降级次数
     */
    public long downgradeAddAndGet(long time) {
        return downgradeData.incrementAndGet(time).getSlidingCycleValue();
    }

    /**
     * 获取当前降级量的值
     *
     * @param time UTC到当前时间的毫秒数
     * @return 降级次数
     */
    public long getDowngradeValue(long time) {
        return downgradeData.getCurSlidingCycleValue(time);
    }

    /**
     * 获取上一周期降级量的统计值
     *
     * @return
     */
    public long getLastCycleDowngradeValue(long time) {
        return downgradeData.getLastWholeCycleValue(time);
    }

    /**
     * 更新并发阈值
     *
     * @param concurrentThreshold
     */
    public void updateConcurrentThreshold(int concurrentThreshold) {
        concurrentData.updateConcurrentThreshold(concurrentThreshold);
    }

    /**
     * 计数器加1的通用操作，包含对下一个毫秒计数器清零等
     *
     * @param time      UTC到当前时间的毫秒数
     * @param cycleData 周期统计数据
     * @return 计数器+1后的次数
     */
    private VisitWrapperValue commonAddAndGet(long time, AbstractCycleData cycleData) {
        return cycleData.incrementAndGet(time);
    }
}
