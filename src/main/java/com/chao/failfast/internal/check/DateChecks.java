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
 */
public final class DateChecks {

    private DateChecks() {}

    // Original Date methods
    public static boolean after(Date date1, Date date2) {
        return date1 != null && date2 != null && date1.after(date2);
    }

    public static boolean before(Date date1, Date date2) {
        return date1 != null && date2 != null && date1.before(date2);
    }

    // Generic Comparable methods for Time API (and others)
    public static <T extends Comparable<T>> boolean after(T t1, T t2) {
        return t1 != null && t2 != null && t1.compareTo(t2) > 0;
    }

    public static <T extends Comparable<T>> boolean afterOrEqual(T t1, T t2) {
        return t1 != null && t2 != null && t1.compareTo(t2) >= 0;
    }

    public static <T extends Comparable<T>> boolean before(T t1, T t2) {
        return t1 != null && t2 != null && t1.compareTo(t2) < 0;
    }

    public static <T extends Comparable<T>> boolean beforeOrEqual(T t1, T t2) {
        return t1 != null && t2 != null && t1.compareTo(t2) <= 0;
    }

    public static <T extends Comparable<T>> boolean between(T value, T start, T end) {
        return value != null && start != null && end != null
                && value.compareTo(start) >= 0 && value.compareTo(end) <= 0;
    }

    // isPast / isFuture implementations
    public static boolean isPast(Date date) {
        return date != null && date.before(new Date());
    }

    public static boolean isFuture(Date date) {
        return date != null && date.after(new Date());
    }

    public static boolean isPast(ChronoLocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }

    public static boolean isFuture(ChronoLocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }

    public static boolean isPast(ChronoLocalDateTime<?> dateTime) {
        return dateTime != null && dateTime.isBefore(LocalDateTime.now());
    }

    public static boolean isFuture(ChronoLocalDateTime<?> dateTime) {
        return dateTime != null && dateTime.isAfter(LocalDateTime.now());
    }

    public static boolean isPast(Instant instant) {
        return instant != null && instant.isBefore(Instant.now());
    }

    public static boolean isFuture(Instant instant) {
        return instant != null && instant.isAfter(Instant.now());
    }

    public static boolean isPast(ChronoZonedDateTime<?> zonedDateTime) {
        return zonedDateTime != null && zonedDateTime.isBefore(ZonedDateTime.now());
    }

    public static boolean isFuture(ChronoZonedDateTime<?> zonedDateTime) {
        return zonedDateTime != null && zonedDateTime.isAfter(ZonedDateTime.now());
    }

    public static boolean isToday(LocalDate date) {
        return date != null && date.isEqual(LocalDate.now());
    }
}
