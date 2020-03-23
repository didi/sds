package com.didiglobal.sds.admin.util;

import com.didiglobal.sds.client.util.StringUtils;

import java.util.regex.Pattern;

/**
 * 字符工具类
 *
 * @author manzhizhen
 * @date 2019-01-28
 */
public final class StringCheck {

    /**
     * 中文字符的正则
     */
    private static Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");

    /**
     * 检查名字，限制字母、数字和"-"
     *
     * @param name
     * @return
     */
    public static boolean checkStringName(String name) {
        if (StringUtils.isBlank(name)) {
            return true;
        }

        for (char c : name.toCharArray()) {
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '-' || c == '_' ||
                    c == '#') {
                continue;
            }

            return false;
        }

        return true;
    }

    /**
     * 检查名字，限制汉字、字母、数字和"-"
     *
     * @param name
     * @return
     */
    public static boolean checkChineseStringName(String name) {
        if (StringUtils.isBlank(name)) {
            return true;
        }

        for (char c : name.toCharArray()) {
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '-' ||
                    isChinese(c)) {
                continue;
            }

            return false;
        }

        return true;
    }


    /**
     * 是否是汉字
     *
     * @param value
     * @return
     */
    public static boolean isChinese(char value) {
        return pattern.matcher(String.valueOf(value)).find();
    }

}
