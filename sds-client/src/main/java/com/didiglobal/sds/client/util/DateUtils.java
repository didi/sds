/**
 *
 */
package com.didiglobal.sds.client.util;

import com.didiglobal.sds.client.contant.BizConstant;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author manzhizhen
 * @version $Id: DateUtils.java, v 0.1 2016年2月21日 上午10:56:04 Administrator Exp $
 */
public class DateUtils {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将日期时间转换成字符格式
     *
     * @param date
     * @param newFormat
     * @return
     */
    public static String format(Date date, String newFormat) {
        if (date == null) {
            return null;
        }
        if (StringUtils.isBlank(newFormat)) {
            newFormat = DATE_TIME_FORMAT;
        }

        SimpleDateFormat format = new SimpleDateFormat(newFormat);

        return format.format(date);
    }

    /**
     * 获取当前时间处于第几个周期时间
     *
     * @param cycleTime 周期时长，单位秒
     * @param time UTC到现在的毫秒数
     * @return 当前时间处于第几个周期时间
     */
    public static long getCycleTimeIndex(long cycleTime, long time) {
        return time / (cycleTime * BizConstant.MILLISECOND_IN_SECOND);
    }

    /**
     * 返回举例当前时间最近的上一个周期的结束时间
     *
     * @param cycleTime 周期时长，单位秒
     * @param time UTC到现在的毫秒数
     * @return 当前时间处于第几个周期时间
     */
    public static long getLastCycleEndTime(long cycleTime, long time) {
        return time - (time % (cycleTime * BizConstant.MILLISECOND_IN_SECOND));
    }

    /**
     * 获取指定时间秒数
     *
     * @param time UTC到现在的毫秒数
     * @return UTC的分钟数
     */
    public static long getSecond(long time) {
        return time / BizConstant.MILLISECOND_IN_SECOND;
    }
}
