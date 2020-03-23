package com.didiglobal.sds.client.bean;

/**
 * 访问量的包装值
 * 包含当前桶值和当前的滑动周期值
 *
 * Created by manzhizhen on 18/2/24.
 */
public class VisitWrapperValue {
    /**
     * 当前桶值
     */
    private long bucketValue;

    /**
     * 滑动周期值
     */
    private long slidingCycleValue;

    public VisitWrapperValue(long bucketValue, long slidingCycleValue) {
        this.bucketValue = bucketValue;
        this.slidingCycleValue = slidingCycleValue;
    }

    public long getBucketValue() {
        return bucketValue;
    }

    public long getSlidingCycleValue() {
        return slidingCycleValue;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VisitWrapperValue{");
        sb.append("bucketValue=").append(bucketValue);
        sb.append(", slidingCycleValue=").append(slidingCycleValue);
        sb.append('}');
        return sb.toString();
    }
}
