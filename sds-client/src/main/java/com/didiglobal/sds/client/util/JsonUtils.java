package com.didiglobal.sds.client.util;

/**
 * json相关的工具类
 *
 * @auther manzhizhen
 * @date 2019/5/1
 */
public class JsonUtils {

    /**
     * 判断该json串是否是数组类型
     *
     * @param jsonStr
     * @return
     */
    public static boolean isJsonArray(String jsonStr) {

        if(StringUtils.isBlank(jsonStr)) {
            return false;
        }

        return jsonStr.trim().startsWith("[");
    }

}
