package com.chao.failfast.internal.chain;

import com.chao.failfast.internal.check.ArrayChecks;
import com.chao.failfast.internal.core.ResponseCode;
import com.chao.failfast.internal.core.ViolationSpec;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 数组校验接口
 */
public interface ArrayTerm<S extends ChainCore<S>> {

    S core();

    // ========== notEmpty ==========

    default <T> S notEmpty(T[] array, Consumer<ViolationSpec> spec) {
        return core().check(ArrayChecks.notEmpty(array), spec);
    }

    default <T> S notEmpty(T[] array) {
        return notEmpty(array, ValidationConst.NO_OP);
    }

    default <T> S notEmpty(T[] array, ResponseCode code) {
        return notEmpty(array, s -> s.responseCode(code));
    }

    default <T> S notEmpty(T[] array, ResponseCode code, String detail) {
        return notEmpty(array, s -> s.responseCode(code).detail(detail));
    }

    // ========== isEmpty ==========

    default <T> S isEmpty(T[] array, Consumer<ViolationSpec> spec) {
        return core().check(ArrayChecks.isEmpty(array), spec);
    }

    default <T> S isEmpty(T[] array) {
        return isEmpty(array, ValidationConst.NO_OP);
    }

    default <T> S isEmpty(T[] array, ResponseCode code) {
        return isEmpty(array, s -> s.responseCode(code));
    }

    default <T> S isEmpty(T[] array, ResponseCode code, String detail) {
        return isEmpty(array, s -> s.responseCode(code).detail(detail));
    }

    // ========== sizeBetween ==========

    default <T> S sizeBetween(T[] array, int min, int max, Consumer<ViolationSpec> spec) {
        if (!core().isAlive()) return core();
        return core().check(ArrayChecks.sizeBetween(array, min, max), spec);
    }

    default <T> S sizeBetween(T[] array, int min, int max) {
        return sizeBetween(array, min, max, ValidationConst.NO_OP);
    }

    default <T> S sizeBetween(T[] array, int min, int max, ResponseCode code) {
        return sizeBetween(array, min, max, s -> s.responseCode(code));
    }

    default <T> S sizeBetween(T[] array, int min, int max, ResponseCode code, String detail) {
        return sizeBetween(array, min, max, s -> s.responseCode(code).detail(detail));
    }

    // ========== sizeEquals ==========

    default <T> S sizeEquals(T[] array, int expectedSize, Consumer<ViolationSpec> spec) {
        return core().check(ArrayChecks.sizeEquals(array, expectedSize), spec);
    }

    default <T> S sizeEquals(T[] array, int expectedSize) {
        return sizeEquals(array, expectedSize, ValidationConst.NO_OP);
    }

    default <T> S sizeEquals(T[] array, int expectedSize, ResponseCode code) {
        return sizeEquals(array, expectedSize, s -> s.responseCode(code));
    }

    default <T> S sizeEquals(T[] array, int expectedSize, ResponseCode code, String detail) {
        return sizeEquals(array, expectedSize, s -> s.responseCode(code).detail(detail));
    }

    // ========== contains ==========

    default <T> S contains(T[] array, T element, Consumer<ViolationSpec> spec) {
        if (!core().isAlive()) return core();
        return core().check(ArrayChecks.contains(array, element), spec);
    }

    default <T> S contains(T[] array, T element) {
        return contains(array, element, ValidationConst.NO_OP);
    }

    default <T> S contains(T[] array, T element, ResponseCode code) {
        return contains(array, element, s -> s.responseCode(code));
    }

    default <T> S contains(T[] array, T element, ResponseCode code, String detail) {
        return contains(array, element, s -> s.responseCode(code).detail(detail));
    }

    // ========== notContains ==========

    default <T> S notContains(T[] array, T element, Consumer<ViolationSpec> spec) {
        if (!core().isAlive()) return core();
        return core().check(ArrayChecks.notContains(array, element), spec);
    }

    default <T> S notContains(T[] array, T element) {
        return notContains(array, element, ValidationConst.NO_OP);
    }

    default <T> S notContains(T[] array, T element, ResponseCode code) {
        return notContains(array, element, s -> s.responseCode(code));
    }

    default <T> S notContains(T[] array, T element, ResponseCode code, String detail) {
        return notContains(array, element, s -> s.responseCode(code).detail(detail));
    }

    // ========== hasNoNullElements ==========

    default <T> S hasNoNullElements(T[] array, Consumer<ViolationSpec> spec) {
        return core().check(ArrayChecks.hasNoNullElements(array), spec);
    }

    default <T> S hasNoNullElements(T[] array) {
        return hasNoNullElements(array, ValidationConst.NO_OP);
    }

    default <T> S hasNoNullElements(T[] array, ResponseCode code) {
        return hasNoNullElements(array, s -> s.responseCode(code));
    }

    default <T> S hasNoNullElements(T[] array, ResponseCode code, String detail) {
        return hasNoNullElements(array, s -> s.responseCode(code).detail(detail));
    }

    // ========== allMatch ==========

    default <T> S allMatch(T[] array, Predicate<T> predicate, Consumer<ViolationSpec> spec) {
        return core().check(ArrayChecks.allMatch(array, predicate), spec);
    }

    default <T> S allMatch(T[] array, Predicate<T> predicate) {
        return allMatch(array, predicate, ValidationConst.NO_OP);
    }

    default <T> S allMatch(T[] array, Predicate<T> predicate, ResponseCode code) {
        return allMatch(array, predicate, s -> s.responseCode(code));
    }

    default <T> S allMatch(T[] array, Predicate<T> predicate, ResponseCode code, String detail) {
        return allMatch(array, predicate, s -> s.responseCode(code).detail(detail));
    }

    // ========== anyMatch ==========

    default <T> S anyMatch(T[] array, Predicate<T> predicate, Consumer<ViolationSpec> spec) {
        return core().check(ArrayChecks.anyMatch(array, predicate), spec);
    }

    default <T> S anyMatch(T[] array, Predicate<T> predicate) {
        return anyMatch(array, predicate, ValidationConst.NO_OP);
    }

    default <T> S anyMatch(T[] array, Predicate<T> predicate, ResponseCode code) {
        return anyMatch(array, predicate, s -> s.responseCode(code));
    }

    default <T> S anyMatch(T[] array, Predicate<T> predicate, ResponseCode code, String detail) {
        return anyMatch(array, predicate, s -> s.responseCode(code).detail(detail));
    }
}