package com.needto.common.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间处理工具
 * @author Administrator
 * @date 2018/6/7 0007
 */
public class DateUtils {

    /**
     * Date转Calendar
     *
     * @param date
     * @return
     */
    public static Calendar dataToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * Calendar转Date
     * @param calendar
     * @return
     */
    public static Date calendarToData(Calendar calendar) {
        // 从一个 Calendar 对象中获取 Date 对象
        return calendar.getTime();
    }


    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static Date getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        return calendar.getTime();
    }

    /**
     * 获取未来 第 past 天的日期
     * @param past
     * @return
     */
    public static Date getFetureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        return calendar.getTime();
    }

    public static Date getDate(int days){
        Date date;
        if(days == 0){
            date = new Date();
        }else if(days > 0){
            date = getFetureDate(days);
        }else{
            date = getPastDate(-days);
        }
        return date;
    }

    /**
     * 获取某一天的开始时间
     */
    public static Date getBeginDate(int days){
        Date date = getDate(days);
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取某一天的结束时间
     */
    public static Date getEndDate(int days){
        Date date = getDate(days);
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取周的开始时间
     * @return
     */
    public static Date getWeekBeginDate(int weeks){
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.WEEK_OF_YEAR, weeks);
        int dayWeek = currentDate.get(Calendar.DAY_OF_WEEK);
        if(1 == dayWeek){
            currentDate.add(Calendar.DAY_OF_MONTH, -1);
        }
        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        return calendarToData(currentDate);
    }

    /**
     * 获取周的结束时间
     * @return
     */
    public static Date getWeekEndDate(int weeks){
        weeks += 1;
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.WEEK_OF_YEAR, weeks);
        int dayWeek = currentDate.get(Calendar.DAY_OF_WEEK);
        if(1 == dayWeek){
            currentDate.add(Calendar.DAY_OF_MONTH, -1);
        }
        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        return calendarToData(currentDate);
    }

    /**
     * 获取月的开始时间
     * @param months
     * @return
     */
    public static Date getMonthBeginDate(int months){
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.DATE, 1);
        currentDate.add(Calendar.MONTH, months);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        return calendarToData(currentDate);
    }

    /**
     * 获取月的结束时间
     * @param months
     * @return
     */
    public static Date getMonthEndnDate(int months){
        months += 1;
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.DATE, 1);
        currentDate.add(Calendar.MONTH, months);
        currentDate.add(Calendar.DATE, -1);
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        return calendarToData(currentDate);
    }

    public static void main(String[] args){
        System.out.println("日开始："+ getBeginDate(0).toString());
        System.out.println("日结束："+ getEndDate(0).toString());
        System.out.println("周开始："+ getWeekBeginDate(-1).toString());
        System.out.println("周结束："+ getWeekEndDate(-1).toString());
        System.out.println("月开始："+ getMonthBeginDate(-1).toString());
        System.out.println("月结束："+ getMonthEndnDate(-1).toString());
    }

}
