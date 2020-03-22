package com.didiglobal.sds.client.service;

import com.didiglobal.sds.client.bean.SdsStrategy;
import com.didiglobal.sds.client.bean.VisitWrapperValue;
import com.didiglobal.sds.client.counter.PowerfulCycleTimeCounter;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link PowerfulCycleTimeCounter}计数器服务类
 * <p>
 * Created by manzhizhen on 2016/6/11.
 */
public class SdsPowerfulCounterService {

    private static Logger logger = SdsLoggerFactory.getDefaultLogger();

    /**
     * 计数器Map，key-降级点名称，value-访问计数器对象
     */
    private final static ConcurrentHashMap<String, PowerfulCycleTimeCounter> pointCounterMap =
            new ConcurrentHashMap<>();

    private final static SdsPowerfulCounterService counterService = new SdsPowerfulCounterService();

    /**
     * 单例
     */
    public static SdsPowerfulCounterService getInstance() {
        return counterService;
    }

    public VisitWrapperValue visitInvokeAddAndGet(String point, long time) {
        PowerfulCycleTimeCounter counter = getOrCreatePoint(point);

        return counter.visitAddAndGet(time);
    }

    public long getLastSecondVisitBucketValue(String point, long time) {
        PowerfulCycleTimeCounter counter = getOrCreatePoint(point);
        return counter.getLastSecondVisitBucketValue(time);
    }

    public boolean concurrentAcquire(String point, long time) {
        PowerfulCycleTimeCounter counter = getOrCreatePoint(point);

        return counter.concurrentAcquire(time);
    }

    public void concurrentRelease(String point) {
        PowerfulCycleTimeCounter counter = getOrCreatePoint(point);

        counter.concurrentRelease();
    }

    public long exceptionInvokeAddAndGet(String point, long time) {
        PowerfulCycleTimeCounter counter = getOrCreatePoint(point);

        return counter.exceptionAddAndGet(time);
    }

    public long getExceptionInvoke(String point, long time) {
        PowerfulCycleTimeCounter counter = getOrCreatePoint(point);

        return counter.getExceptionValue(time);
    }

    public long timeoutInvokeAddAndGet(String point, long time) {
        PowerfulCycleTimeCounter counter = getOrCreatePoint(point);

        return counter.timeoutAddAndGet(time);
    }

    public long getTimeoutInvoke(String point, long time) {
        PowerfulCycleTimeCounter counter = getOrCreatePoint(point);

        return counter.getTimeoutValue(time);
    }

    public long tokenBucketInvokeAddAndGet(String point, long time) {

        PowerfulCycleTimeCounter counter = getOrCreatePoint(point);

        return counter.tokenBucketInvokeAddAndGet(time);
    }

    public long downgradeAddAndGet(String point, long time) {
        PowerfulCycleTimeCounter counter = getOrCreatePoint(point);

        return counter.downgradeAddAndGet(time);
    }

    public long getCurCycleDowngrade(String point, long time) {
        PowerfulCycleTimeCounter counter = getOrCreatePoint(point);

        return counter.getDowngradeValue(time);
    }

    /**
     * 用于主动更新各降级点的并发阈值和耗时阈值
     */
    public void updateConcurrent() {
        for (Map.Entry<String, SdsStrategy> entry : SdsStrategyService.getInstance().getStrategyMap().entrySet()) {
            if (entry.getValue().getConcurrentThreshold() == null) {
                continue;
            }

            PowerfulCycleTimeCounter powerfulCycleTimeCounter = getOrCreatePoint(entry.getKey());

            if (entry.getValue().getConcurrentThreshold() >= 0) {
                powerfulCycleTimeCounter.updateConcurrentThreshold(entry.getValue().getConcurrentThreshold());

            } else {
                // 如果是小于0的值，例如-1，那就是表明不开启并发限流功能
                powerfulCycleTimeCounter.updateConcurrentThreshold(Integer.MAX_VALUE);
            }
        }
    }

    public PowerfulCycleTimeCounter getOrCreatePoint(String point) {
        PowerfulCycleTimeCounter counter = pointCounterMap.get(point);
        if (null == counter) {
            counter = addPoint(point);
        }

        return counter;
    }

    /**
     * 新增降级点
     *
     * @param point 降级点名称
     */
    private PowerfulCycleTimeCounter addPoint(String point) {
        if (pointCounterMap.putIfAbsent(point, new PowerfulCycleTimeCounter()) == null) {
            logger.info("SdsPowerfulCounterService 新增降级点:" + point);

        }

        return pointCounterMap.get(point);
    }

    public ConcurrentHashMap<String, PowerfulCycleTimeCounter> getPointCounterMap() {
        return pointCounterMap;
    }
}
