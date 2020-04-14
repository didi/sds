/**
 *
 */
package com.didiglobal.sds.client.util;

/**
 *
 * @author manzhizhen
 * @version $Id: StringUtils.java, v 0.1 2016年2月20日 下午9:57:10 Administrator Exp $
 */
public class StringUtils {

    public static boolean isBlank(String value) {
        return value == null || "".equals(value.trim());
    }

    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

}
