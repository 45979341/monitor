package com.zhuzhou.framework.utils.stomp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: chenzeting
 * Date:     2018/9/17
 * Description:
 */
public class DateUtils {

    public static final String ymdstr = "yyyy-MM-dd HH:mm:ss";

    public static final SimpleDateFormat sdf = new SimpleDateFormat(ymdstr);

    public static final SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat yymd = new SimpleDateFormat("yyyyMMdd");

    public static final SimpleDateFormat ym = new SimpleDateFormat("yyyyMM");

    public static final SimpleDateFormat all = new SimpleDateFormat("yyyyMMddHHmmss");

    public static final SimpleDateFormat ymdhms = new SimpleDateFormat("yyyyMMdd HHmmss");

    public static Date parse (SimpleDateFormat sdf, String date) {
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String format (SimpleDateFormat sdf, Date date) {
        return sdf.format(date);
    }

    /**
     * 格式化日期 yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String formatDate (Date date) {
        if (date == null) {
            return "";
        }
        return sdf.format(date);
    }

    /**
     * yyyy-MM-dd
     * @return
     */
    public static String formatDate () {
        return ymd.format(new Date());
    }

    /**
     * 判断与今天是否是同一天
     * @return
     */
    public static boolean isSameDate (Date timeIdentify) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (timeIdentify != null) {
            String time = sdf.format(timeIdentify);
            String curDate = sdf.format(new Date());
            if (curDate.equals(time)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 日期加减秒
     * @param date
     * @return
     */
    public static Date opSecond (Date date, int second) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        //日期加60秒天
        rightNow.add(Calendar.SECOND,second);
        Date resultDate = rightNow.getTime();
        return resultDate;
    }

    /**
     * 两个日期相差>diff,return true
     * @param start
     * @param end
     * @param diff 设置的秒数
     * @return
     */
    public static boolean diffDate (Date start, Date end, int diff) {
        long startTime = start.getTime();
        long endTime = end.getTime();
        return Math.abs((endTime - startTime)/1000) >= diff;
    }

    /**
     * 两个日期相差<diff,return true
     * @param start
     * @param end
     * @param diff 设置的秒数
     * @return
     */
    public static boolean diffDate2 (Date start, Date end, int diff) {
        long startTime = start.getTime();
        long endTime = end.getTime();
        return Math.abs((endTime - startTime)/1000) <= diff;
    }
}
