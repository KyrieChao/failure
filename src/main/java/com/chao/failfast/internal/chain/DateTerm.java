package com.chao.failfast.internal.chain;

import com.chao.failfast.constant.FailureConst;
import com.chao.failfast.internal.check.DateChecks;
import com.chao.failfast.internal.core.ResponseCode;
import com.chao.failfast.internal.core.ViolationSpec;

import java.time.Instant;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.Date;
import java.util.function.Consumer;

/**
 * 日期校验接口
 */
public interface DateTerm<S extends ChainCore<S>> {

    S core();

    // ==================== Date 类型 ====================

    default S after(Date d1, Date d2, Consumer<ViolationSpec> spec) {
        return  core().check(DateChecks.after(d1, d2), spec);
    }

    default S after(Date d1, Date d2) {
        return after(d1, d2, FailureConst.NO_OP);
    }

    default S after(Date d1, Date d2, ResponseCode code) {
        return after(d1, d2, s -> s.responseCode(code));
    }

    default S after(Date d1, Date d2, ResponseCode code, String detail) {
        return after(d1, d2, s -> s.responseCode(code).detail(detail));
    }

    default S before(Date d1, Date d2, Consumer<ViolationSpec> spec) {
        return  core().check(DateChecks.before(d1, d2), spec);
    }

    default S before(Date d1, Date d2) {
        return before(d1, d2, FailureConst.NO_OP);
    }

    default S before(Date d1, Date d2, ResponseCode code) {
        return before(d1, d2, s -> s.responseCode(code));
    }

    default S before(Date d1, Date d2, ResponseCode code, String detail) {
        return before(d1, d2, s -> s.responseCode(code).detail(detail));
    }

    // ----- isPast / isFuture (Date) -----

    default S isPast(Date date, Consumer<ViolationSpec> spec) {
        return  core().check(DateChecks.isPast(date), spec);
    }

    default S isPast(Date date) {
        return isPast(date, FailureConst.NO_OP);
    }

    default S isPast(Date date, ResponseCode code) {
        return isPast(date, s -> s.responseCode(code));
    }

    default S isPast(Date date, ResponseCode code, String detail) {
        return isPast(date, s -> s.responseCode(code).detail(detail));
    }

    default S isFuture(Date date, Consumer<ViolationSpec> spec) {
        return  core().check(DateChecks.isFuture(date), spec);
    }

    default S isFuture(Date date) {
        return isFuture(date, FailureConst.NO_OP);
    }

    default S isFuture(Date date, ResponseCode code) {
        return isFuture(date, s -> s.responseCode(code));
    }

    default S isFuture(Date date, ResponseCode code, String detail) {
        return isFuture(date, s -> s.responseCode(code).detail(detail));
    }

    // ==================== 泛型 Comparable ====================

    default <T extends Comparable<T>> S after(T t1, T t2, Consumer<ViolationSpec> spec) {
        return  core().check(DateChecks.after(t1, t2), spec);
    }

    default <T extends Comparable<T>> S after(T t1, T t2) {
        return after(t1, t2, FailureConst.NO_OP);
    }

    default <T extends Comparable<T>> S after(T t1, T t2, ResponseCode code) {
        return after(t1, t2, s -> s.responseCode(code));
    }

    default <T extends Comparable<T>> S after(T t1, T t2, ResponseCode code, String detail) {
        return after(t1, t2, s -> s.responseCode(code).detail(detail));
    }

    default <T extends Comparable<T>> S before(T t1, T t2, Consumer<ViolationSpec> spec) {
        return  core().check(DateChecks.before(t1, t2), spec);
    }

    default <T extends Comparable<T>> S before(T t1, T t2) {
        return before(t1, t2, FailureConst.NO_OP);
    }

    default <T extends Comparable<T>> S before(T t1, T t2, ResponseCode code) {
        return before(t1, t2, s -> s.responseCode(code));
    }

    default <T extends Comparable<T>> S before(T t1, T t2, ResponseCode code, String detail) {
        return before(t1, t2, s -> s.responseCode(code).detail(detail));
    }

    default <T extends Comparable<T>> S afterOrEqual(T t1, T t2, Consumer<ViolationSpec> spec) {
        return  core().check(DateChecks.afterOrEqual(t1, t2), spec);
    }

    default <T extends Comparable<T>> S afterOrEqual(T t1, T t2) {
        return afterOrEqual(t1, t2, FailureConst.NO_OP);
    }

    default <T extends Comparable<T>> S afterOrEqual(T t1, T t2, ResponseCode code) {
        return afterOrEqual(t1, t2, s -> s.responseCode(code));
    }

    default <T extends Comparable<T>> S afterOrEqual(T t1, T t2, ResponseCode code, String detail) {
        return afterOrEqual(t1, t2, s -> s.responseCode(code).detail(detail));
    }

    default <T extends Comparable<T>> S beforeOrEqual(T t1, T t2, Consumer<ViolationSpec> spec) {
        return  core().check(DateChecks.beforeOrEqual(t1, t2), spec);
    }

    default <T extends Comparable<T>> S beforeOrEqual(T t1, T t2) {
        return beforeOrEqual(t1, t2, FailureConst.NO_OP);
    }

    default <T extends Comparable<T>> S beforeOrEqual(T t1, T t2, ResponseCode code) {
        return beforeOrEqual(t1, t2, s -> s.responseCode(code));
    }

    default <T extends Comparable<T>> S beforeOrEqual(T t1, T t2, ResponseCode code, String detail) {
        return beforeOrEqual(t1, t2, s -> s.responseCode(code).detail(detail));
    }

    default <T extends Comparable<T>> S between(T value, T start, T end, Consumer<ViolationSpec> spec) {
        return  core().check(DateChecks.between(value, start, end), spec);
    }

    default <T extends Comparable<T>> S between(T value, T start, T end) {
        return between(value, start, end, FailureConst.NO_OP);
    }

    default <T extends Comparable<T>> S between(T value, T start, T end, ResponseCode code) {
        return between(value, start, end, s -> s.responseCode(code));
    }

    default <T extends Comparable<T>> S between(T value, T start, T end, ResponseCode code, String detail) {
        return between(value, start, end, s -> s.responseCode(code).detail(detail));
    }

    // ==================== ChronoLocalDate (LocalDate) ====================

    default S isPast(ChronoLocalDate date, Consumer<ViolationSpec> spec) {
        return  core().check(DateChecks.isPast(date), spec);
    }

    default S isPast(ChronoLocalDate date) {
        return isPast(date, FailureConst.NO_OP);
    }

    default S isPast(ChronoLocalDate date, ResponseCode code) {
        return isPast(date, s -> s.responseCode(code));
    }

    default S isPast(ChronoLocalDate date, ResponseCode code, String detail) {
        return isPast(date, s -> s.responseCode(code).detail(detail));
    }

    default S isFuture(ChronoLocalDate date, Consumer<ViolationSpec> spec) {
        return  core().check(DateChecks.isFuture(date), spec);
    }

    default S isFuture(ChronoLocalDate date) {
        return isFuture(date, FailureConst.NO_OP);
    }

    default S isFuture(ChronoLocalDate date, ResponseCode code) {
        return isFuture(date, s -> s.responseCode(code));
    }

    default S isFuture(ChronoLocalDate date, ResponseCode code, String detail) {
        return isFuture(date, s -> s.responseCode(code).detail(detail));
    }

    default S isToday(LocalDate date, Consumer<ViolationSpec> spec) {
        return  core().check(DateChecks.isToday(date), spec);
    }

    default S isToday(LocalDate date) {
        return isToday(date, FailureConst.NO_OP);
    }

    default S isToday(LocalDate date, ResponseCode code) {
        return isToday(date, s -> s.responseCode(code));
    }

    default S isToday(LocalDate date, ResponseCode code, String detail) {
        return isToday(date, s -> s.responseCode(code).detail(detail));
    }

    // ==================== ChronoLocalDateTime (LocalDateTime) ====================

    default S isPast(ChronoLocalDateTime<?> dateTime, Consumer<ViolationSpec> spec) {
        return  core().check(DateChecks.isPast(dateTime), spec);
    }

    default S isPast(ChronoLocalDateTime<?> dateTime) {
        return isPast(dateTime, FailureConst.NO_OP);
    }

    default S isPast(ChronoLocalDateTime<?> dateTime, ResponseCode code) {
        return isPast(dateTime, s -> s.responseCode(code));
    }

    default S isPast(ChronoLocalDateTime<?> dateTime, ResponseCode code, String detail) {
        return isPast(dateTime, s -> s.responseCode(code).detail(detail));
    }

    default S isFuture(ChronoLocalDateTime<?> dateTime, Consumer<ViolationSpec> spec) {
        return  core().check(DateChecks.isFuture(dateTime), spec);
    }

    default S isFuture(ChronoLocalDateTime<?> dateTime) {
        return isFuture(dateTime, FailureConst.NO_OP);
    }

    default S isFuture(ChronoLocalDateTime<?> dateTime, ResponseCode code) {
        return isFuture(dateTime, s -> s.responseCode(code));
    }

    default S isFuture(ChronoLocalDateTime<?> dateTime, ResponseCode code, String detail) {
        return isFuture(dateTime, s -> s.responseCode(code).detail(detail));
    }


    // ==================== Instant ====================

    default S isPast(Instant instant, Consumer<ViolationSpec> spec) {
        return  core().check(DateChecks.isPast(instant), spec);
    }

    default S isPast(Instant instant) {
        return isPast(instant, FailureConst.NO_OP);
    }

    default S isPast(Instant instant, ResponseCode code) {
        return isPast(instant, s -> s.responseCode(code));
    }

    default S isPast(Instant instant, ResponseCode code, String detail) {
        return isPast(instant, s -> s.responseCode(code).detail(detail));
    }

    default S isFuture(Instant instant, Consumer<ViolationSpec> spec) {
        return  core().check(DateChecks.isFuture(instant), spec);
    }

    default S isFuture(Instant instant) {
        return isFuture(instant, FailureConst.NO_OP);
    }

    default S isFuture(Instant instant, ResponseCode code) {
        return isFuture(instant, s -> s.responseCode(code));
    }

    default S isFuture(Instant instant, ResponseCode code, String detail) {
        return isFuture(instant, s -> s.responseCode(code).detail(detail));
    }

    // ==================== ChronoZonedDateTime (ZonedDateTime) ====================

    default S isPast(ChronoZonedDateTime<?> zonedDateTime, Consumer<ViolationSpec> spec) {
        return  core().check(DateChecks.isPast(zonedDateTime), spec);
    }

    default S isPast(ChronoZonedDateTime<?> zonedDateTime) {
        return isPast(zonedDateTime, FailureConst.NO_OP);
    }

    default S isPast(ChronoZonedDateTime<?> zonedDateTime, ResponseCode code) {
        return isPast(zonedDateTime, s -> s.responseCode(code));
    }

    default S isPast(ChronoZonedDateTime<?> zonedDateTime, ResponseCode code, String detail) {
        return isPast(zonedDateTime, s -> s.responseCode(code).detail(detail));
    }

    default S isFuture(ChronoZonedDateTime<?> zonedDateTime, Consumer<ViolationSpec> spec) {
        return  core().check(DateChecks.isFuture(zonedDateTime), spec);
    }

    default S isFuture(ChronoZonedDateTime<?> zonedDateTime) {
        return isFuture(zonedDateTime, FailureConst.NO_OP);
    }

    default S isFuture(ChronoZonedDateTime<?> zonedDateTime, ResponseCode code) {
        return isFuture(zonedDateTime, s -> s.responseCode(code));
    }

    default S isFuture(ChronoZonedDateTime<?> zonedDateTime, ResponseCode code, String detail) {
        return isFuture(zonedDateTime, s -> s.responseCode(code).detail(detail));
    }
}