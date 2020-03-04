package com.didiglobal.sds.admin.util;

import java.util.BitSet;

/**
 * Created by tianyulei on 2019/7/10
 **/
public class BloomFileter {
    private final int[] seeds;
    private final int size;
    private final BitSet notebook;
    private final MisjudgmentRate rate;

    /**
     * 默认中等程序的误判率：MisjudgmentRate.MIDDLE
     *
     * @param dataCount 预期处理的数据规模，如预期用于处理1百万数据的查重，这里则填写1000000
     */
    public BloomFileter(int dataCount) {
        this(MisjudgmentRate.HIGH, dataCount);
    }

    /**
     * @param rate      一个枚举类型的误判率
     * @param dataCount 预期处理的数据规模，如预期用于处理1百万数据的查重，这里则填写1000000
     */
    public BloomFileter(MisjudgmentRate rate, int dataCount) {
        long bitSize = rate.seeds.length * dataCount;
        if (bitSize < 0 || bitSize > Integer.MAX_VALUE) {
            throw new RuntimeException("位数太大溢出了，请降低误判率或者降低数据大小");
        }
        this.rate = rate;
        seeds = rate.seeds;
        size = (int) bitSize;
        notebook = new BitSet(size);
    }

    public void add(String data) {

        for (int i = 0; i < seeds.length; i++) {
            int index = hash(data, seeds[i]);
            setTrue(index);
        }
    }

    public boolean check(String data) {
        for (int i = 0; i < seeds.length; i++) {
            int index = hash(data, seeds[i]);
            if (!notebook.get(index)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 新增成功返回true,否则返回false
     *
     * @param data
     * @return
     */
    public boolean addIfNotExist(String data) {
        if (!check(data)) {
            add(data);
            return true;
        }
        return false;
    }

    public void setTrue(int index) {
        notebook.set(index, true);
    }

    private int hash(String data, int seeds) {
        char[] value = data.toCharArray();
        int hash = 0;
        if (value.length > 0) {

            for (int i = 0; i < value.length; i++) {
                hash = i * hash + value[i];
            }
        }

        hash = hash * seeds % size;
        // 防止溢出变成负数
        return Math.abs(hash);
    }


    /**
     * 分配的位数越多，误判率越低但是越占内存
     * <p>
     * 4个位误判率大概是0.14689159766308
     * <p>
     * 8个位误判率大概是0.02157714146322
     * <p>
     * 16个位误判率大概是0.00046557303372
     * <p>
     * 32个位误判率大概是0.00000021167340
     *
     * @author tianyulei
     */
    public enum MisjudgmentRate {
        // 这里要选取质数，能很好的降低错误率
        /**
         * 每个字符串分配4个位
         */
        VERY_SMALL(new int[]{2, 3, 5, 7}),
        /**
         * 每个字符串分配8个位
         */
        SMALL(new int[]{2, 3, 5, 7, 11, 13, 17, 19}), //
        /**
         * 每个字符串分配16个位
         */
        MIDDLE(new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53}), //
        /**
         * 每个字符串分配32个位
         */
        HIGH(new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97,
                101, 103, 107, 109, 113, 127, 131});

        private int[] seeds;

        MisjudgmentRate(int[] seeds) {
            this.seeds = seeds;
        }

        public int[] getSeeds() {
            return seeds;
        }
    }

}
