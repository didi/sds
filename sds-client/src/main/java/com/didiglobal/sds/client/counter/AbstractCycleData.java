package com.didiglobal.sds.client.counter;

import com.didiglobal.sds.client.bean.VisitWrapperValue;

/**
 * 抽象周期数据类
 * <p>
 * 完整周期（WholeCycle）: 包含桶indxe0-9或者10-19或者20-29这10个桶的周期就是完整的周期。上传统计和降级的数据都来自一个完整周期。
 * 滑动周期（SlidingCycle）：从当前时间算出的当前桶index，然后往前推9个桶，这10个桶组成的周期就是滑动周期。判断要不要降级是根据当前的滑动周期的数据来的。
 * <p>
 * Created by manzhizhen on 17/7/23.
 */
public abstract class AbstractCycleData {

    /**
     * 时间对应的桶值新增1
     *
     * @param time
     * @return
     */
    abstract public VisitWrapperValue incrementAndGet(long time);

    /**
     * 设置对应时间的桶的值
     *
     * @param time
     * @param value
     * @return
     */
    abstract public void setBucketValue(long time, Long value);

    /**
     * 获取时间对应的桶值
     *
     * @param time
     * @return
     */
    abstract public long getBucketValue(long time);

    /**
     * 获取当前时间所处【滑动周期】的统计值
     *
     * @param time 当前毫秒数
     * @return
     */
    abstract public long getCurSlidingCycleValue(long time);

    /**
     * 获取当前时间所处周期的上个【完整周期】所有桶的总值
     *
     * @param time 当前毫秒数
     * @return
     */
    abstract public long getLastWholeCycleValue(long time);

    /**
     * 清理当前时间所处周期的下个完整周期的数据
     *
     * @param time 当前毫秒数
     * @return
     */
    abstract public void clearNextCycleValue(long time);
}
