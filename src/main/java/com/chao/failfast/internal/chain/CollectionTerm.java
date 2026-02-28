package com.chao.failfast.internal.chain;

import com.chao.failfast.internal.check.CollectionChecks;
import com.chao.failfast.internal.core.ResponseCode;
import com.chao.failfast.internal.core.ViolationSpec;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 集合校验接口
 */
public interface CollectionTerm<S extends ChainCore<S>> {

    S core();

    // ========== notEmpty ==========

    default S notEmpty(Collection<?> col, Consumer<ViolationSpec> spec) {
        return core().check(CollectionChecks.notEmpty(col), spec);
    }

    default S notEmpty(Collection<?> col) {
        return notEmpty(col, s -> {
        });
    }

    default S notEmpty(Collection<?> col, ResponseCode code) {
        return notEmpty(col, s -> s.responseCode(code));
    }

    default S notEmpty(Collection<?> col, ResponseCode code, String detail) {
        return notEmpty(col, s -> s.responseCode(code).detail(detail));
    }

    // ========== isEmpty ==========

    default S isEmpty(Collection<?> col, Consumer<ViolationSpec> spec) {
        return core().check(CollectionChecks.isEmpty(col), spec);
    }

    default S isEmpty(Collection<?> col) {
        return isEmpty(col, s -> {
        });
    }

    default S isEmpty(Collection<?> col, ResponseCode code) {
        return isEmpty(col, s -> s.responseCode(code));
    }

    default S isEmpty(Collection<?> col, ResponseCode code, String detail) {
        return isEmpty(col, s -> s.responseCode(code).detail(detail));
    }

    // ========== sizeBetween ==========

    default S sizeBetween(Collection<?> col, int min, int max, Consumer<ViolationSpec> spec) {
        if (!core().isAlive()) return core();
        return core().check(CollectionChecks.sizeBetween(col, min, max), spec);
    }

    default S sizeBetween(Collection<?> col, int min, int max) {
        return sizeBetween(col, min, max, s -> {
        });
    }

    default S sizeBetween(Collection<?> col, int min, int max, ResponseCode code) {
        return sizeBetween(col, min, max, s -> s.responseCode(code));
    }

    default S sizeBetween(Collection<?> col, int min, int max, ResponseCode code, String detail) {
        return sizeBetween(col, min, max, s -> s.responseCode(code).detail(detail));
    }

    // ========== sizeEquals ==========

    default S sizeEquals(Collection<?> col, int expectedSize, Consumer<ViolationSpec> spec) {
        return core().check(CollectionChecks.sizeEquals(col, expectedSize), spec);
    }

    default S sizeEquals(Collection<?> col, int expectedSize) {
        return sizeEquals(col, expectedSize, s -> {
        });
    }

    default S sizeEquals(Collection<?> col, int expectedSize, ResponseCode code) {
        return sizeEquals(col, expectedSize, s -> s.responseCode(code));
    }

    default S sizeEquals(Collection<?> col, int expectedSize, ResponseCode code, String detail) {
        return sizeEquals(col, expectedSize, s -> s.responseCode(code).detail(detail));
    }

    // ========== contains ==========

    default S contains(Collection<?> col, Object element, Consumer<ViolationSpec> spec) {
        return core().check(CollectionChecks.contains(col, element), spec);
    }

    default S contains(Collection<?> col, Object element) {
        return contains(col, element, s -> {
        });
    }

    default S contains(Collection<?> col, Object element, ResponseCode code) {
        return contains(col, element, s -> s.responseCode(code));
    }

    default S contains(Collection<?> col, Object element, ResponseCode code, String detail) {
        return contains(col, element, s -> s.responseCode(code).detail(detail));
    }

    // ========== notContains ==========

    default S notContains(Collection<?> col, Object element, Consumer<ViolationSpec> spec) {
        return core().check(CollectionChecks.notContains(col, element), spec);
    }

    default S notContains(Collection<?> col, Object element) {
        return notContains(col, element, s -> {
        });
    }

    default S notContains(Collection<?> col, Object element, ResponseCode code) {
        return notContains(col, element, s -> s.responseCode(code));
    }

    default S notContains(Collection<?> col, Object element, ResponseCode code, String detail) {
        return notContains(col, element, s -> s.responseCode(code).detail(detail));
    }

    // ========== hasNoNullElements ==========

    default S hasNoNullElements(Collection<?> col, Consumer<ViolationSpec> spec) {
        return core().check(CollectionChecks.hasNoNullElements(col), spec);
    }

    default S hasNoNullElements(Collection<?> col) {
        return hasNoNullElements(col, s -> {
        });
    }

    default S hasNoNullElements(Collection<?> col, ResponseCode code) {
        return hasNoNullElements(col, s -> s.responseCode(code));
    }

    default S hasNoNullElements(Collection<?> col, ResponseCode code, String detail) {
        return hasNoNullElements(col, s -> s.responseCode(code).detail(detail));
    }

    // ========== allMatch ==========

    default <T> S allMatch(Collection<T> col, Predicate<T> predicate, Consumer<ViolationSpec> spec) {
        return core().check(CollectionChecks.allMatch(col, predicate), spec);
    }

    default <T> S allMatch(Collection<T> col, Predicate<T> predicate) {
        return allMatch(col, predicate, s -> {
        });
    }

    default <T> S allMatch(Collection<T> col, Predicate<T> predicate, ResponseCode code) {
        return allMatch(col, predicate, s -> s.responseCode(code));
    }

    default <T> S allMatch(Collection<T> col, Predicate<T> predicate, ResponseCode code, String detail) {
        return allMatch(col, predicate, s -> s.responseCode(code).detail(detail));
    }

    // ========== anyMatch ==========

    default <T> S anyMatch(Collection<T> col, Predicate<T> predicate, Consumer<ViolationSpec> spec) {
        return core().check(CollectionChecks.anyMatch(col, predicate), spec);
    }

    default <T> S anyMatch(Collection<T> col, Predicate<T> predicate) {
        return anyMatch(col, predicate, s -> {
        });
    }

    default <T> S anyMatch(Collection<T> col, Predicate<T> predicate, ResponseCode code) {
        return anyMatch(col, predicate, s -> s.responseCode(code));
    }

    default <T> S anyMatch(Collection<T> col, Predicate<T> predicate, ResponseCode code, String detail) {
        return anyMatch(col, predicate, s -> s.responseCode(code).detail(detail));
    }
}