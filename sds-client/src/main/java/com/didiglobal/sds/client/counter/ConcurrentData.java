package com.didiglobal.sds.client.counter;

import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.client.contant.BizConstant;
import org.slf4j.Logger;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 用于并发量统计的数据
 * <p>
 * Created by manzhizhen on 2016/6/25.
 */
public class ConcurrentData {

    /**
     * 并发量限制，将并发严格限制在阈值之内
     */
    private volatile Semaphore concurrentLimit;

    /**
     * 并发量阈值，其实也是初始化上面信号量的许可数
     */
    private volatile int concurrentThreshold;

    /**
     * 为了保证acquire和release使用的是同一个concurrentLimit
     */
    private ThreadLocal<SemaphoreResult> concurrentLimitThreadLocal = new ThreadLocal<SemaphoreResult>() {
        @Override
        protected SemaphoreResult initialValue() {
            return new SemaphoreResult();
        }
    };

    /**
     * 为了较精准的统计并发最大值，需要用到锁
     */
    private ReentrantLock lock = new ReentrantLock();

    /**
     * 我们只需要统计周期内的并发最大值，所以一个周期不需要10个桶，一个桶就行了
     */
    private AbstractCycleData cycleMaxConcurrentData = new SlidingWindowData(BizConstant.CYCLE_NUM, 1, 10);

    private Logger logger = SdsLoggerFactory.getDefaultLogger();

    public ConcurrentData() {
        this(Integer.MAX_VALUE);
    }

    public ConcurrentData(int concurrentThreshold) {
        this.concurrentThreshold = concurrentThreshold <= 0 ? Integer.MAX_VALUE : concurrentThreshold;

        concurrentLimit = new Semaphore(this.concurrentThreshold);
    }

    /**
     * 并发访问+1
     *
     * @param time 暂时用不着
     * @return 当前并发量
     */
    public boolean concurrentAcquire(long time) {

        boolean result = concurrentLimit.tryAcquire();
        concurrentLimitThreadLocal.get().setAll(concurrentLimit, result);

        int curValue = concurrentThreshold - concurrentLimit.availablePermits();

        /**
         * 统计历史最大并发量的值
         */
        if (curValue > cycleMaxConcurrentData.getCurSlidingCycleValue(time)) {
            try {
                if (lock.tryLock(3, TimeUnit.MILLISECONDS)) {
                    try {
                        if (curValue > cycleMaxConcurrentData.getCurSlidingCycleValue(time)) {
                            cycleMaxConcurrentData.setBucketValue(time, (long) curValue);
                        }
                    } finally {
                        lock.unlock();
                    }
                } else {
                    logger.warn("ConcurrentData#concurrentAcquire 获取锁失败，最大并发值可能统计不准.");
                }
            } catch (Exception e) {
            }
        }

        return result;
    }

    /**
     * 并发访问-1
     * 注意，Release信号量时得确保和Acquire使用的是同一个{@link #}
     *
     * @return 当前并发量
     */
    public void concurrentRelease() {
        // 小于0表示不做限制
        if (concurrentThreshold < 0) {
            return;
        }

        /**
         * 确保和concurrentAcquire使用的是同一个concurrentLimit
         */
        if (concurrentLimitThreadLocal.get().isAcquireResult()) {
            Semaphore semaphore = concurrentLimitThreadLocal.get().getSemaphore();
            if (semaphore != null) {
                semaphore.release();
            }
        }
    }

    /**
     * 获取当前并发量的值
     *
     * @return
     */
    public int getConcurrentValue() {
        if (concurrentThreshold < 0) {
            return -1;
        }

        return concurrentThreshold - concurrentLimit.availablePermits();
    }

    /**
     * 更新并发阈值
     *
     * @param concurrentThreshold
     */
    public void updateConcurrentThreshold(int concurrentThreshold) {
        this.concurrentThreshold = concurrentThreshold;
        concurrentLimit = new Semaphore(concurrentThreshold < 0 ? 0 : concurrentThreshold);
    }

    /**
     * 获取历史最大并发数并将并发值清零
     *
     * @param time
     * @return
     */
    public int getLastConcurrentMaxCountAndClean(long time) {
        return (int) cycleMaxConcurrentData.getLastWholeCycleValue(time);
    }

    public AbstractCycleData getCycleMaxConcurrentData() {
        return cycleMaxConcurrentData;
    }

    private class SemaphoreResult {

        private Semaphore semaphore;
        private boolean acquireResult;

        public void setAll(Semaphore semaphore, boolean acquireResult) {
            this.semaphore = semaphore;
            this.acquireResult = acquireResult;
        }

        public Semaphore getSemaphore() {
            return semaphore;
        }

        public void setSemaphore(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        public boolean isAcquireResult() {
            return acquireResult;
        }

        public void setAcquireResult(boolean acquireResult) {
            this.acquireResult = acquireResult;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("SemaphoreResult{");
            sb.append("semaphore=").append(semaphore);
            sb.append(", acquireResult=").append(acquireResult);
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ConcurrentData{");
        sb.append("concurrentLimit=").append(concurrentLimit);
        sb.append(", concurrentThreshold=").append(concurrentThreshold);
        sb.append(", concurrentLimitThreadLocal=").append(concurrentLimitThreadLocal);
        sb.append(", lock=").append(lock);
        sb.append(", cycleMaxConcurrentData=").append(cycleMaxConcurrentData);
        sb.append('}');
        return sb.toString();
    }
}
