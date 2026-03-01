package com.chao.failfast.internal.chain;

import com.chao.failfast.constant.FailureConst;
import com.chao.failfast.internal.check.NumberChecks;
import com.chao.failfast.internal.core.ResponseCode;
import com.chao.failfast.internal.core.ViolationSpec;

import java.math.BigDecimal;
import java.util.function.Consumer;

/**
 * 数值校验接口
 */
public interface NumberTerm<S extends ChainCore<S>> {

    S core();

    // ========== positive ==========

    default S positive(Number value, Consumer<ViolationSpec> spec) {
        return core().check(NumberChecks.positive(value), spec);
    }

    default S positive(Number value) {
        return positive(value, FailureConst.NO_OP);
    }

    default S positive(Number value, ResponseCode code) {
        return positive(value, s -> s.responseCode(code));
    }

    default S positive(Number value, ResponseCode code, String detail) {
        return positive(value, s -> s.responseCode(code).detail(detail));
    }

    // ========== inRange ==========

    default <T extends Number & Comparable<T>> S inRange(T value, T min, T max, Consumer<ViolationSpec> spec) {
        if (!core().isAlive()) return core();
        return core().check(NumberChecks.inRange(value, min, max), spec);
    }

    default <T extends Number & Comparable<T>> S inRange(T value, T min, T max) {
        return inRange(value, min, max, FailureConst.NO_OP);
    }

    default <T extends Number & Comparable<T>> S inRange(T value, T min, T max, ResponseCode code) {
        return inRange(value, min, max, s -> s.responseCode(code));
    }

    default <T extends Number & Comparable<T>> S inRange(T value, T min, T max, ResponseCode code, String detail) {
        return inRange(value, min, max, s -> s.responseCode(code).detail(detail));
    }

    // ========== nonNegative ==========

    default S nonNegative(Number value, Consumer<ViolationSpec> spec) {
        return core().check(NumberChecks.nonNegative(value), spec);
    }

    default S nonNegative(Number value) {
        return nonNegative(value, FailureConst.NO_OP);
    }

    default S nonNegative(Number value, ResponseCode code) {
        return nonNegative(value, s -> s.responseCode(code));
    }

    default S nonNegative(Number value, ResponseCode code, String detail) {
        return nonNegative(value, s -> s.responseCode(code).detail(detail));
    }

    // ========== greaterThan ==========

    default <T extends Number & Comparable<T>> S greaterThan(T value, T threshold, Consumer<ViolationSpec> spec) {
        return core().check(NumberChecks.greaterThan(value, threshold), spec);
    }

    default <T extends Number & Comparable<T>> S greaterThan(T value, T threshold) {
        return greaterThan(value, threshold, FailureConst.NO_OP);
    }

    default <T extends Number & Comparable<T>> S greaterThan(T value, T threshold, ResponseCode code) {
        return greaterThan(value, threshold, s -> s.responseCode(code));
    }

    default <T extends Number & Comparable<T>> S greaterThan(T value, T threshold, ResponseCode code, String detail) {
        return greaterThan(value, threshold, s -> s.responseCode(code).detail(detail));
    }

    // ========== greaterOrEqual ==========

    default <T extends Number & Comparable<T>> S greaterOrEqual(T value, T threshold, Consumer<ViolationSpec> spec) {
        return core().check(NumberChecks.greaterOrEqual(value, threshold), spec);
    }

    default <T extends Number & Comparable<T>> S greaterOrEqual(T value, T threshold) {
        return greaterOrEqual(value, threshold, FailureConst.NO_OP);
    }

    default <T extends Number & Comparable<T>> S greaterOrEqual(T value, T threshold, ResponseCode code) {
        return greaterOrEqual(value, threshold, s -> s.responseCode(code));
    }

    default <T extends Number & Comparable<T>> S greaterOrEqual(T value, T threshold, ResponseCode code, String detail) {
        return greaterOrEqual(value, threshold, s -> s.responseCode(code).detail(detail));
    }

    // ========== lessThan ==========

    default <T extends Number & Comparable<T>> S lessThan(T value, T threshold, Consumer<ViolationSpec> spec) {
        return core().check(NumberChecks.lessThan(value, threshold), spec);
    }

    default <T extends Number & Comparable<T>> S lessThan(T value, T threshold) {
        return lessThan(value, threshold, FailureConst.NO_OP);
    }

    default <T extends Number & Comparable<T>> S lessThan(T value, T threshold, ResponseCode code) {
        return lessThan(value, threshold, s -> s.responseCode(code));
    }

    default <T extends Number & Comparable<T>> S lessThan(T value, T threshold, ResponseCode code, String detail) {
        return lessThan(value, threshold, s -> s.responseCode(code).detail(detail));
    }

    // ========== lessOrEqual ==========

    default <T extends Number & Comparable<T>> S lessOrEqual(T value, T threshold, Consumer<ViolationSpec> spec) {
        return core().check(NumberChecks.lessOrEqual(value, threshold), spec);
    }

    default <T extends Number & Comparable<T>> S lessOrEqual(T value, T threshold) {
        return lessOrEqual(value, threshold, FailureConst.NO_OP);
    }

    default <T extends Number & Comparable<T>> S lessOrEqual(T value, T threshold, ResponseCode code) {
        return lessOrEqual(value, threshold, s -> s.responseCode(code));
    }

    default <T extends Number & Comparable<T>> S lessOrEqual(T value, T threshold, ResponseCode code, String detail) {
        return lessOrEqual(value, threshold, s -> s.responseCode(code).detail(detail));
    }

    // ========== notZero ==========

    default S notZero(Number value, Consumer<ViolationSpec> spec) {
        return core().check(NumberChecks.notZero(value), spec);
    }

    default S notZero(Number value) {
        return notZero(value, FailureConst.NO_OP);
    }

    default S notZero(Number value, ResponseCode code) {
        return notZero(value, s -> s.responseCode(code));
    }

    default S notZero(Number value, ResponseCode code, String detail) {
        return notZero(value, s -> s.responseCode(code).detail(detail));
    }

    // ========== isZero ==========

    default S isZero(Number value, Consumer<ViolationSpec> spec) {
        return core().check(NumberChecks.isZero(value), spec);
    }

    default S isZero(Number value) {
        return isZero(value, FailureConst.NO_OP);
    }

    default S isZero(Number value, ResponseCode code) {
        return isZero(value, s -> s.responseCode(code));
    }

    default S isZero(Number value, ResponseCode code, String detail) {
        return isZero(value, s -> s.responseCode(code).detail(detail));
    }

    // ========== negative ==========

    default S negative(Number value, Consumer<ViolationSpec> spec) {
        return core().check(NumberChecks.negative(value), spec);
    }

    default S negative(Number value) {
        return negative(value, FailureConst.NO_OP);
    }

    default S negative(Number value, ResponseCode code) {
        return negative(value, s -> s.responseCode(code));
    }

    default S negative(Number value, ResponseCode code, String detail) {
        return negative(value, s -> s.responseCode(code).detail(detail));
    }

    // ========== multipleOf ==========

    default S multipleOf(Number value, Number divisor, Consumer<ViolationSpec> spec) {
        return core().check(NumberChecks.multipleOf(value, divisor), spec);
    }

    default S multipleOf(Number value, Number divisor) {
        return multipleOf(value, divisor, FailureConst.NO_OP);
    }

    default S multipleOf(Number value, Number divisor, ResponseCode code) {
        return multipleOf(value, divisor, s -> s.responseCode(code));
    }

    default S multipleOf(Number value, Number divisor, ResponseCode code, String detail) {
        return multipleOf(value, divisor, s -> s.responseCode(code).detail(detail));
    }

    // ========== decimalScale ==========

    default S decimalScale(BigDecimal value, int scale, Consumer<ViolationSpec> spec) {
        return core().check(NumberChecks.decimalScale(value, scale), spec);
    }

    default S decimalScale(BigDecimal value, int scale) {
        return decimalScale(value, scale, FailureConst.NO_OP);
    }

    default S decimalScale(BigDecimal value, int scale, ResponseCode code) {
        return decimalScale(value, scale, s -> s.responseCode(code));
    }

    default S decimalScale(BigDecimal value, int scale, ResponseCode code, String detail) {
        return decimalScale(value, scale, s -> s.responseCode(code).detail(detail));
    }
    // 在 NumberValidation 接口中添加：

// ========== positiveNumber (alias) ==========

    default S positiveNumber(Number value, Consumer<ViolationSpec> spec) {
        return positive(value, spec);
    }

    default S positiveNumber(Number value) {
        return positive(value);
    }

    default S positiveNumber(Number value, ResponseCode code) {
        return positive(value, code);
    }

    default S positiveNumber(Number value, ResponseCode code, String detail) {
        return positive(value, code, detail);
    }

// ========== inRangeNumber ==========

    default S inRangeNumber(Number v, Number min, Number max, Consumer<ViolationSpec> spec) {
        if (!core().isAlive()) return core();
        return core().check(NumberChecks.inRangeNumber(v, min, max), spec);
    }

    default S inRangeNumber(Number v, Number min, Number max) {
        return inRangeNumber(v, min, max, FailureConst.NO_OP);
    }

    default S inRangeNumber(Number v, Number min, Number max, ResponseCode code) {
        return inRangeNumber(v, min, max, s -> s.responseCode(code));
    }

    default S inRangeNumber(Number v, Number min, Number max, ResponseCode code, String detail) {
        return inRangeNumber(v, min, max, s -> s.responseCode(code).detail(detail));
    }
}