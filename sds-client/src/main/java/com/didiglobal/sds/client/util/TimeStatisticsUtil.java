package com.didiglobal.sds.client.util;

import java.util.concurrent.TimeUnit;

/**
 * 时间统计工具类
 * <p>
 * Created by manzhizhen on 2016/6/21.
 */
public class TimeStatisticsUtil {
    private static ThreadLocal<Long> startTimeThreadLocal = new ThreadLocal<>();

    /**
     * 记录开始时间
     */
    public static void startTime() {
        startTime(System.nanoTime());
    }

    /**
     * 记录开始时间
     */
    public static void startTime(long time) {
        startTimeThreadLocal.set(time);
    }

    /**
     * 获取消耗的时间，单位毫秒
     * 注意：如果使用错误，比如没有调用startTime方法，则返回-1
     */
    public static long getConsumeTime() {
        Long startTime = startTimeThreadLocal.get();
        if (startTime != null) {
            return (System.nanoTime() - startTime) / 1000000;
        }

        return -1;
    }


    public static void main(String[] args) {

        long t1 = System.currentTimeMillis();
        long t2 = System.nanoTime();

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
        }

        System.out.println(System.currentTimeMillis() - t1);
        System.out.println(System.nanoTime() - t2);
    }
}

