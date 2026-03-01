package com.chao.failfast.internal.chain;

import com.chao.failfast.constant.FailureConst;
import com.chao.failfast.internal.check.MapChecks;
import com.chao.failfast.internal.core.ResponseCode;
import com.chao.failfast.internal.core.ViolationSpec;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Map校验接口
 */
public interface MapTerm<S extends ChainCore<S>> {

    S core();

    // ========== notEmpty ==========

    default S notEmpty(Map<?, ?> map, Consumer<ViolationSpec> spec) {
        return core().check(MapChecks.notEmpty(map), spec);
    }

    default S notEmpty(Map<?, ?> map) {
        return notEmpty(map, FailureConst.NO_OP);
    }

    default S notEmpty(Map<?, ?> map, ResponseCode code) {
        return notEmpty(map, s -> s.responseCode(code));
    }

    default S notEmpty(Map<?, ?> map, ResponseCode code, String detail) {
        return notEmpty(map, s -> s.responseCode(code).detail(detail));
    }

    // ========== isEmpty ==========

    default S isEmpty(Map<?, ?> map, Consumer<ViolationSpec> spec) {
        return core().check(MapChecks.isEmpty(map), spec);
    }

    default S isEmpty(Map<?, ?> map) {
        return isEmpty(map, FailureConst.NO_OP);
    }

    default S isEmpty(Map<?, ?> map, ResponseCode code) {
        return isEmpty(map, s -> s.responseCode(code));
    }

    default S isEmpty(Map<?, ?> map, ResponseCode code, String detail) {
        return isEmpty(map, s -> s.responseCode(code).detail(detail));
    }

    // ========== containsKey ==========

    default S containsKey(Map<?, ?> map, Object key, Consumer<ViolationSpec> spec) {
        return core().check(MapChecks.containsKey(map, key), spec);
    }

    default S containsKey(Map<?, ?> map, Object key) {
        return containsKey(map, key, FailureConst.NO_OP);
    }

    default S containsKey(Map<?, ?> map, Object key, ResponseCode code) {
        return containsKey(map, key, s -> s.responseCode(code));
    }

    default S containsKey(Map<?, ?> map, Object key, ResponseCode code, String detail) {
        return containsKey(map, key, s -> s.responseCode(code).detail(detail));
    }

    // ========== notContainsKey ==========

    default S notContainsKey(Map<?, ?> map, Object key, Consumer<ViolationSpec> spec) {
        return core().check(MapChecks.notContainsKey(map, key), spec);
    }

    default S notContainsKey(Map<?, ?> map, Object key) {
        return notContainsKey(map, key, FailureConst.NO_OP);
    }

    default S notContainsKey(Map<?, ?> map, Object key, ResponseCode code) {
        return notContainsKey(map, key, s -> s.responseCode(code));
    }

    default S notContainsKey(Map<?, ?> map, Object key, ResponseCode code, String detail) {
        return notContainsKey(map, key, s -> s.responseCode(code).detail(detail));
    }

    // ========== containsValue ==========

    default S containsValue(Map<?, ?> map, Object value, Consumer<ViolationSpec> spec) {
        return core().check(MapChecks.containsValue(map, value), spec);
    }

    default S containsValue(Map<?, ?> map, Object value) {
        return containsValue(map, value, FailureConst.NO_OP);
    }

    default S containsValue(Map<?, ?> map, Object value, ResponseCode code) {
        return containsValue(map, value, s -> s.responseCode(code));
    }

    default S containsValue(Map<?, ?> map, Object value, ResponseCode code, String detail) {
        return containsValue(map, value, s -> s.responseCode(code).detail(detail));
    }

    // ========== sizeBetween ==========

    default S sizeBetween(Map<?, ?> map, int min, int max, Consumer<ViolationSpec> spec) {
        return core().check(MapChecks.sizeBetween(map, min, max), spec);
    }

    default S sizeBetween(Map<?, ?> map, int min, int max) {
        return sizeBetween(map, min, max, FailureConst.NO_OP);
    }

    default S sizeBetween(Map<?, ?> map, int min, int max, ResponseCode code) {
        return sizeBetween(map, min, max, s -> s.responseCode(code));
    }

    default S sizeBetween(Map<?, ?> map, int min, int max, ResponseCode code, String detail) {
        return sizeBetween(map, min, max, s -> s.responseCode(code).detail(detail));
    }

    // ========== sizeEquals ==========

    default S sizeEquals(Map<?, ?> map, int size, Consumer<ViolationSpec> spec) {
        return core().check(MapChecks.sizeEquals(map, size), spec);
    }

    default S sizeEquals(Map<?, ?> map, int size) {
        return sizeEquals(map, size, FailureConst.NO_OP);
    }

    default S sizeEquals(Map<?, ?> map, int size, ResponseCode code) {
        return sizeEquals(map, size, s -> s.responseCode(code));
    }

    default S sizeEquals(Map<?, ?> map, int size, ResponseCode code, String detail) {
        return sizeEquals(map, size, s -> s.responseCode(code).detail(detail));
    }
}