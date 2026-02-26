package com.chao.failfast.internal.check;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.Date;

/**
 * 日期校验工具类
 * 提供各种日期和时间的比较、判断方法
 */
public final class DateChecks {


    /**
     * 私有构造方法，防止实例化工具类
     */
    private DateChecks() {}

    // Original Date methods

    /**
     * 判断日期date1是否在date2之后
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @return 如果date1不为null且date2不为null且date1在date2之后，返回true；否则返回false
     */
    public static boolean after(Date date1, Date date2) {
        return date1 != null && date2 != null && date1.after(date2);
    }
    /**
     * 判断日期date1是否在date2之前
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @return 如果date1不为null且date2不为null且date1在date2之前，返回true；否则返回false
     */
    public static boolean before(Date date1, Date date2) {
        return date1 != null && date2 != null && date1.before(date2);
    }

    // Generic Comparable methods for Time API (and others)
    /**
     * 判断泛型对象t1是否在t2之后
     * @param t1 第一个可比较对象
     * @param t2 第二个可比较对象
     * @return 如果t1和t2都不为null且t1大于t2，返回true；否则返回false
     */
    public static <T extends Comparable<T>> boolean after(T t1, T t2) {
        return t1 != null && t2 != null && t1.compareTo(t2) > 0;
    }

    /**
     * 判断泛型对象t1是否在t2之后或相等
     * @param t1 第一个可比较对象
     * @param t2 第二个可比较对象
     * @return 如果t1和t2都不为null且t1大于或等于t2，返回true；否则返回false
     */
    public static <T extends Comparable<T>> boolean afterOrEqual(T t1, T t2) {
        return t1 != null && t2 != null && t1.compareTo(t2) >= 0;
    }

    /**
     * 判断泛型对象t1是否在t2之前
     * @param t1 第一个可比较对象
     * @param t2 第二个可比较对象
     * @return 如果t1和t2都不为null且t1小于t2，返回true；否则返回false
     */
    public static <T extends Comparable<T>> boolean before(T t1, T t2) {
        return t1 != null && t2 != null && t1.compareTo(t2) < 0;
    }

    /**
     * 判断泛型对象t1是否在t2之前或相等
     * @param t1 第一个可比较对象
     * @param t2 第二个可比较对象
     * @return 如果t1和t2都不为null且t1小于或等于t2，返回true；否则返回false
     */
    public static <T extends Comparable<T>> boolean beforeOrEqual(T t1, T t2) {
        return t1 != null && t2 != null && t1.compareTo(t2) <= 0;
    }

    /**
     * 判断泛型对象value是否在start和end之间（包含边界）
     * @param value 要判断的值
     * @param start 起始值
     * @param end 结束值
     * @return 如果value、start和end都不为null且value在start和end之间（包含边界），返回true；否则返回false
     */
    public static <T extends Comparable<T>> boolean between(T value, T start, T end) {
        return value != null && start != null && end != null
                && value.compareTo(start) >= 0 && value.compareTo(end) <= 0;
    }

    // isPast / isFuture implementations
    /**
     * 判断Date类型的日期是否为过去时间
     * @param date 要判断的日期
     * @return 如果date不为null且在当前时间之前，返回true；否则返回false
     */
    public static boolean isPast(Date date) {
        return date != null && date.before(new Date());
    }

    /**
     * 判断Date类型的日期是否为未来时间
     * @param date 要判断的日期
     * @return 如果date不为null且在当前时间之后，返回true；否则返回false
     */
    public static boolean isFuture(Date date) {
        return date != null && date.after(new Date());
    }

    /**
     * 判断ChronoLocalDate类型的日期是否为过去时间
     * @param date 要判断的日期
     * @return 如果date不为null且在当前日期之前，返回true；否则返回false
     */
    public static boolean isPast(ChronoLocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }

    /**
     * 判断ChronoLocalDate类型的日期是否为未来时间
     * @param date 要判断的日期
     * @return 如果date不为null且在当前日期之后，返回true；否则返回false
     */
    public static boolean isFuture(ChronoLocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }

    /**
     * 判断ChronoLocalDateTime类型的时间是否为过去时间
     * @param dateTime 要判断的日期时间
     * @return 如果dateTime不为null且在当前日期时间之前，返回true；否则返回false
     */
    public static boolean isPast(ChronoLocalDateTime<?> dateTime) {
        return dateTime != null && dateTime.isBefore(LocalDateTime.now());
    }

    /**
     * 判断ChronoLocalDateTime类型的时间是否为未来时间
     * @param dateTime 要判断的日期时间
     * @return 如果dateTime不为null且在当前日期时间之后，返回true；否则返回false
     */
    public static boolean isFuture(ChronoLocalDateTime<?> dateTime) {
        return dateTime != null && dateTime.isAfter(LocalDateTime.now());
    }

    /**
     * 判断Instant类型的时间是否为过去时间
     * @param instant 要判断的时间点
     * @return 如果instant不为null且在当前时间点之前，返回true；否则返回false
     */
    public static boolean isPast(Instant instant) {
        return instant != null && instant.isBefore(Instant.now());
    }

    /**
     * 判断Instant类型的时间是否为未来时间
     * @param instant 要判断的时间点
     * @return 如果instant不为null且在当前时间点之后，返回true；否则返回false
     */
    public static boolean isFuture(Instant instant) {
        return instant != null && instant.isAfter(Instant.now());
    }

    /**
     * 判断ChronoZonedDateTime类型的时间是否为过去时间
     * @param zonedDateTime 要判断的时区日期时间
     * @return 如果zonedDateTime不为null且在当前时区日期时间之前，返回true；否则返回false
     */
    public static boolean isPast(ChronoZonedDateTime<?> zonedDateTime) {
        return zonedDateTime != null && zonedDateTime.isBefore(ZonedDateTime.now());
    }

    /**
     * 判断ChronoZonedDateTime类型的时间是否为未来时间
     * @param zonedDateTime 要判断的时区日期时间
     * @return 如果zonedDateTime不为null且在当前时区日期时间之后，返回true；否则返回false
     */
    public static boolean isFuture(ChronoZonedDateTime<?> zonedDateTime) {
        return zonedDateTime != null && zonedDateTime.isAfter(ZonedDateTime.now());
    }

    /**
     * 判断LocalDate类型的日期是否为今天
     * @param date 要判断的日期
     * @return 如果date不为null且与当前日期相同，返回true；否则返回false
     */
    public static boolean isToday(LocalDate date) {
        return date != null && date.isEqual(LocalDate.now());
    }
}
