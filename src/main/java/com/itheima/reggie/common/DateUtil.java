package com.itheima.reggie.common;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.tomcat.util.http.ConcurrentDateFormat;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static final String CN_DATA = "yyyy年MM月dd日";
    private static final String NORM_DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_PATTERN = "yyyyMMdd";
    public static final String DATE_SHORT_PATTERN = "yyyy-MM";
    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_TIME = "HH:mm:ss";
//    public static final ConcurrentDateFormat DATETIME_FORMAT = ConcurrentDateFormat.of("yyyy-MM-dd HH:mm:ss");
//    public static final ConcurrentDateFormat DATE_FORMAT = ConcurrentDateFormat.of("yyyy-MM-dd");
//    public static final ConcurrentDateFormat TIME_FORMAT = ConcurrentDateFormat.of("HH:mm:ss");

    public DateUtil() {
    }

    public static Date setYears(Date date, int amount) {
        return set(date, 1, amount);
    }

    public static Date setMonths(Date date, int amount) {
        return set(date, 2, amount);
    }

    public static Date setWeeks(Date date, int amount) {
        return set(date, 3, amount);
    }

    public static Date setDays(Date date, int amount) {
        return set(date, 5, amount);
    }

    public static Date setHours(Date date, int amount) {
        return set(date, 11, amount);
    }

    public static Date setMinutes(Date date, int amount) {
        return set(date, 12, amount);
    }

    public static Date setSeconds(Date date, int amount) {
        return set(date, 13, amount);
    }

    public static Date setMilliseconds(Date date, int amount) {
        return set(date, 14, amount);
    }

    private static Date set(Date date, int calendarField, int amount) {
        Assert.notNull(date, "The date must not be null");
        Calendar c = Calendar.getInstance();
        c.setLenient(false);
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

//    public static String formatDateTime(Date date) {
//        return DATETIME_FORMAT.format(date);
//    }
//
//    public static String formatDate(Date date) {
//        return DATE_FORMAT.format(date);
//    }
//
//    public static String formatTime(Date date) {
//        return TIME_FORMAT.format(date);
//    }

    public static String formatDate(Date date, String pattern) {
        String formatDate = null;
        if (StringUtils.isNotBlank(pattern)) {
            formatDate = DateFormatUtils.format(date, pattern);
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }

        return formatDate;
    }

//    public static String format(Date date, String pattern) {
//        return ConcurrentDateFormat.of(pattern).format(date);
//    }
//
//    public static Date parse(String dateStr, String pattern) {
//        ConcurrentDateFormat format = ConcurrentDateFormat.of(pattern);
//
//        try {
//            return format.parse(dateStr);
//        } catch (ParseException var4) {
//            throw Exceptions.unchecked(var4);
//        }
//    }

//    public static Date parse(String dateStr, ConcurrentDateFormat format) {
//        try {
//            return format.parse(dateStr);
//        } catch (ParseException var3) {
//            throw Exceptions.unchecked(var3);
//        }
//    }

    public static String getTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String getMsTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
    }

    public static String getAllTime() {
        return formatDate(new Date(), "yyyyMMddHHmmss");
    }

    public static String getTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getDay(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }

    public static final Date stringToDate(String date) {
        if (date == null) {
            return null;
        } else {
            String separator = String.valueOf(date.charAt(4));
            String pattern = "yyyyMMdd";
            if (!separator.matches("\\d*")) {
                pattern = "yyyy" + separator + "MM" + separator + "dd";
                if (date.length() < 10) {
                    pattern = "yyyy" + separator + "M" + separator + "d";
                }
            } else if (date.length() < 8) {
                pattern = "yyyyMd";
            }

            pattern = pattern + " HH:mm:ss.SSS";
            pattern = pattern.substring(0, Math.min(pattern.length(), date.length()));

            try {
                return (new SimpleDateFormat(pattern)).parse(date);
            } catch (ParseException var4) {
                return null;
            }
        }
    }

//    public static final String format(Date date) {
//        return format(date, "yyyy-MM-dd");
//    }

    public static String getSysYear() {
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(1));
        return year;
    }

    public static Integer getSysMonth() {
        Calendar date = Calendar.getInstance();
        Integer month = date.get(2);
        return month;
    }

    public static Integer getSysDay() {
        Calendar date = Calendar.getInstance();
        Integer month = date.get(5);
        return month;
    }

    public static String getDefaultFormatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static LocalDate toDate(Instant instant) {
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date localDateToDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        } else {
            ZoneId zone = ZoneId.systemDefault();
            Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
            return Date.from(instant);
        }
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        } else {
            ZoneId zone = ZoneId.systemDefault();
            Instant instant = localDateTime.atZone(zone).toInstant();
            return Date.from(instant);
        }
    }

    public static long localDateToTimestamp(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static String getCnDate(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return "";
        } else {
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
            LocalDate date = LocalDate.parse(dateStr);
            return sdf.format(date);
        }
    }

    public static Date getDateAfter(Date date, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(5, now.get(5) + day);
        return now.getTime();
    }

    public static Date getDateBefore(Date date, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(5, now.get(5) - day);
        return now.getTime();
    }

    public static String getCnDateInfo(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return "";
        } else {
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDate date = LocalDate.parse(dateStr, timeFormatter);
            return sdf.format(date);
        }
    }

//    public static String getCnDateInfoByDate(LocalDate date) {
//        if (ToolUtil.isEmpty(date)) {
//            return "";
//        } else {
//            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
//            return sdf.format(date);
//        }
//    }

    public static LocalDate date2LocalDate(Date date) {
        return null == date ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        return null == date ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static boolean isToday(String day) {
        Date now = new Date();
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        String nowDay = sim.format(now);
        return day.equals(nowDay);
    }
}
