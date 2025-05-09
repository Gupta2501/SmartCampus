package com.teach.javafxclient.util;

import java.time.LocalDate;

public class LocalDateUtil {
    public static boolean isIntervalContained(String date1, String date2, String date3, String date4) {
        if (date1==null||date1.isEmpty()||date2==null||date2.isEmpty()){
            return true;
        }
        // 验证日期的升序性
        if (date1.compareTo(date2) > 0 || date3.compareTo(date4) > 0) {
            return false;
        }

        // 解析日期字符串为 LocalDate 对象
        LocalDate startDate = LocalDate.parse(date1);
        LocalDate endDate = LocalDate.parse(date2);
        LocalDate intervalStart = LocalDate.parse(date3);
        LocalDate intervalEnd = LocalDate.parse(date4);

        // 判断区间是否包含在后两个日期内
        if (intervalStart.isBefore(startDate) && intervalEnd.isAfter(endDate)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isLaterDate(String date1, String date2) {
        // 解析日期字符串为 LocalDate 对象
        LocalDate firstDate = LocalDate.parse(date1);
        LocalDate secondDate = LocalDate.parse(date2);

        // 判断后一个日期是否比前一个日期大
        return secondDate.isAfter(firstDate);
    }

    public static boolean isBeforeToday(String dateString) {
        // 解析日期字符串
        LocalDate date = LocalDate.parse(dateString);

        // 获取今天的日期
        LocalDate today = LocalDate.now();

        // 判断给定日期是否在今天之前
        return date.isBefore(today);
    }

    public static boolean isTodayInRange(String startDateString, String endDateString) {
        // 解析起始日期字符串
        LocalDate startDate = LocalDate.parse(startDateString);

        // 解析结束日期字符串
        LocalDate endDate = LocalDate.parse(endDateString);

        // 获取今天的日期
        LocalDate today = LocalDate.now();

        // 判断今天是否在日期范围内
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }
}
