package com.jnu.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author ：Killer
 * @date ：Created in 20-10-21 下午5:46
 * @description：${description}
 * @modified By：
 * @version: version
 */
public class DateUtil {

    /**
     * 日期格式 年 月 日 如2009-02-26
     */
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    private DateUtil(){

    }

    public static Date getNow(){
        return new Date();
    }
    /**
     * 获取 指定日期的00:00:00
     *
     * @param day 0表示当天,1表示往后一天,-1表示前一天,如此类推
     * @return
     */
    public static Date getDayStartTime(Date date, int day) {
        if (date == null) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        calendar.set(Calendar.MILLISECOND,0);
        calendar.add(Calendar.DAY_OF_MONTH,day);
        return calendar.getTime();
    }

    /**
     * 获取 指定日期的23:59:59
     *
     * @param day 0表示当天,1表示往后一天,-1表示前一天,如此类推
     * @return
     */
    public static Date getDayEndTime(Date date,int day) {
        if (date == null) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                23, 59, 59);
        calendar.add(Calendar.DAY_OF_MONTH,day);
        return calendar.getTime();
    }

    /**
     * 按指定的格式，把Date转换成String 如date为null,返回null
     *
     * @param date
     *            Date参数
     * @param format
     *            日期格式
     * @return String
     */
    public static String format(Date date, String format) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 基于指定日期增加天数
     * @param date
     * @param num 整数往后推，负数往前移
     * @return
     */
    public static Date addDay(Date date,int num) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, num);
        return cal.getTime();
    }
}
