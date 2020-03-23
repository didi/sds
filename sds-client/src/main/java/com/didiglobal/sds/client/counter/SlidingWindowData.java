package com.didiglobal.sds.client.counter;

import com.didiglobal.sds.client.bean.VisitWrapperValue;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import com.didiglobal.sds.client.util.DateUtils;
import com.didiglobal.sds.client.contant.BizConstant;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicLongArray;

/**
 * 采用滑动窗口来进行数据滚动统计，让统计数据更准确更自然
 * 滑动窗口内部由固定的桶来组成，每个桶代表每秒的统计数据
 * <p>
 * 完整周期（WholeCycle）: 包含桶indxe0-9或者10-19或者20-29这10个桶的周期就是完整的周期。上传统计和降级的数据都来自一个完整周期。
 * 滑动周期（SlidingCycle）：从当前时间算出的当前桶index，然后往前推9个桶，这10个桶组成的周期就是滑动周期。判断要不要降级是根据当前的滑动周期的数据来的。
 * <p>
 * Created by manzhizhen on 18/2/9.
 */
public class SlidingWindowData extends AbstractCycleData {

    /**
     * 滑动窗口周期数
     */
    protected Integer cycleNum;

    /**
     * 滑动窗口包含的桶的数量
     */
    protected Integer cycleBucketNum;

    /**
     * 一个桶的时间宽度，单位秒
     */
    protected Integer bucketTimeSecond;

    /**
     * 周期内总的桶数
     */
    protected Integer bucketSize;

    /**
     * 用来存储{@link BizConstant#CYCLE_NUM} 个完整周期的数据
     */
    protected AtomicLongArray bucketArray;

    private static Logger logger = SdsLoggerFactory.getDefaultLogger();

    public SlidingWindowData(Integer cycleNum, Integer cycleBucketNum, Integer bucketTimeSecond) {
        this.cycleNum = cycleNum;
        this.cycleBucketNum = cycleBucketNum;
        this.bucketTimeSecond = bucketTimeSecond;

        this.bucketSize = cycleNum * cycleBucketNum;
        this.bucketArray = new AtomicLongArray(bucketSize);
    }

    public SlidingWindowData() {
        this(BizConstant.CYCLE_NUM, BizConstant.CYCLE_BUCKET_NUM, 1);
    }

    @Override
    public VisitWrapperValue incrementAndGet(long time) {
        int bucketIndex = getBucketIndexByTime(time);

        // 需要将该周期的所有秒数据统计出来再返回
        long curSecondValue = bucketArray.incrementAndGet(bucketIndex);
        long slidingCycleValue = curSecondValue;
        for (int i = bucketIndex - cycleBucketNum + 1; i < bucketIndex; i++) {
            slidingCycleValue += bucketArray.get(switchIndex(i));
        }

        return new VisitWrapperValue(curSecondValue, slidingCycleValue);
    }

    @Override
    public void setBucketValue(long time, Long value) {
        int bucketIndex = getBucketIndexByTime(time);
        bucketArray.set(bucketIndex, value);
    }

    @Override
    public long getBucketValue(long time) {
        int bucketIndex = getBucketIndexByTime(time);

        return bucketArray.get(bucketIndex);
    }

    @Override
    public long getCurSlidingCycleValue(long time) {
        return getSlidingCycleBucketTotalValue(getBucketIndexByTime(time));
    }

    @Override
    public long getLastWholeCycleValue(long time) {
        int index = getBucketIndexByTime(time);
        return getLastWholeCycleBucketTotalValue(index);
    }

    @Override
    public void clearNextCycleValue(long time) {
        cleanBucketArrayValue(time);
    }

    /**
     * 获取当前桶（秒）所在的数组索引
     *
     * @param time UTC时间毫秒数
     * @return
     */
    private int getBucketIndexByTime(long time) {
        return (int) ((DateUtils.getSecond(time) / bucketTimeSecond) % bucketSize);
    }

    /**
     * 由于需要把桶（秒）的数组打造成环形数组，所以这里需要对index做特殊处理
     *
     * @param index
     * @return
     */
    private int switchIndex(int index) {
        if (index >= 0 && index < bucketSize) {
            return index;

        } else if (index < 0) {
            return bucketSize + index;

        } else {
            return index - bucketSize;
        }
    }

    /**
     * 计算桶索引所在【滑动周期】内所有bucket中值的总和
     *
     * @param bucketIndex
     * @return
     */
    private long getSlidingCycleBucketTotalValue(int bucketIndex) {
        long total = bucketArray.get(bucketIndex);

        for (int i = bucketIndex - cycleBucketNum + 1; i < bucketIndex; i++) {
            total += bucketArray.get(switchIndex(i));
        }

        return total;
    }

    /**
     * 计算桶索引所在的上一【完整周期】内所有bucket中值的总和
     *
     * @param bucketIndex
     * @return
     */
    private long getLastWholeCycleBucketTotalValue(int bucketIndex) {
        long total = 0;

        int cycleNum = switchIndex(bucketIndex - cycleBucketNum) / cycleBucketNum;
        int startBucketIndex = cycleNum * cycleBucketNum;
        int endBucketIndex = startBucketIndex + cycleBucketNum;

        for (int i = startBucketIndex; i < endBucketIndex; i++) {
            total += bucketArray.get(i);

        }

        return total;
    }

    /**
     * 提前将下面用到的桶数据请零
     *
     * @param time
     * @return
     */
    private void cleanBucketArrayValue(long time) {
        int curBucketIndex = getBucketIndexByTime(time);

        /**
         * 看curBucketIndex处于哪个统计周期内，并计算清楚的下一个统计周期的索引,统计分为
         * {@link CYCLE_NUM}个周期, 对应于桶的index的范围是0 ~ CYCLE_BUCKET_NUM-1, CYCLE_BUCKET_NUM ~ 2*CYCLE_BUCKET_NUM-1,
         * 2*CYCLE_BUCKET_NUM ~ 3*CYCLE_BUCKET_NUM-1
         */
        int cycleNum = switchIndex(curBucketIndex + cycleBucketNum) / cycleBucketNum;
        int startBucketIndex = cycleNum * cycleBucketNum;
        int endBucketIndex = startBucketIndex + cycleBucketNum;

        for (int i = startBucketIndex; i < endBucketIndex; i++) {
            bucketArray.set(i, 0);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SlidingWindowData{");
        sb.append("cycleNum=").append(cycleNum);
        sb.append(", cycleBucketNum=").append(cycleBucketNum);
        sb.append(", bucketTimeSecond=").append(bucketTimeSecond);
        sb.append(", bucketSize=").append(bucketSize);
        sb.append(", bucketArray=").append(bucketArray);
        sb.append('}');
        return sb.toString();
    }
}
