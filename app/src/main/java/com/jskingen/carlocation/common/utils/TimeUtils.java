package com.jskingen.carlocation.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by ChneY on 2017/4/10.
 */

public class TimeUtils {
    /**
     * 后台保存格式
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String currentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = new Date(System.currentTimeMillis());
        return format.format(d1);
    }

    public static String currentTime(long t) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = new Date(t);
        return format.format(d1);
    }

    public static String getHHmmss(long l) {
        SimpleDateFormat fmat = new SimpleDateFormat("HH:mm:ss");
        fmat.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
        return fmat.format(l);
    }

    /**
     * 时间转时间戳
     */
    public static long getTimestamp(String time) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(time);
        return date.getTime();
    }

    public static long getTimestamp(String time, String formatString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        Date date = format.parse(time);
        return date.getTime();
    }

    /**
     * 通过 Date 获取
     * @param date
     * @return
     */
    public static String getYMD(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String getYMD() {
        Date date =new Date();
        return getYMD(date);
    }
}
