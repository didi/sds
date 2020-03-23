/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2016 All Rights Reserved.
 */
package com.didiglobal.sds.client.util;

/**
 * 参数断言
 * 
 * @author  manzhizhen
 * @version $Id: AssertUtil.java, v 0.1 2016年1月27日 下午5:33:26 Administrator Exp $
 */
public final class AssertUtil {

    public static void notBlack(String str, String assertDesc) {
        if (StringUtils.isBlank(str)) {
            throw new IllegalArgumentException(assertDesc);
        }
    }

    public static void notNull(Object obj, String assertDesc) {
        if (obj == null) {
            throw new IllegalArgumentException(assertDesc);
        }
    }

    public static void greaterThan(long value, long base, String assertDesc) {
        if (value <= base) {
            throw new IllegalArgumentException(assertDesc);
        }
    }

    public static void greaterThanOrEqual(long value, long base, String assertDesc) {
        if (value < base) {
            throw new IllegalArgumentException(assertDesc);
        }
    }

}
