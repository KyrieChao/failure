package com.chao.failfast.internal.chain;

import com.chao.failfast.internal.check.EnumChecks;
import com.chao.failfast.internal.core.ResponseCode;
import com.chao.failfast.internal.core.ViolationSpec;

import java.util.function.Consumer;

/**
 * 枚举校验接口
 */
public interface EnumTerm<S extends ChainCore<S>> {

    S core();

    // ========== enumValue ==========

    default <E extends Enum<E>> S enumValue(Class<E> enumType, String value, Consumer<ViolationSpec> spec) {
        if (!core().isAlive()) return core();
        return core().check(EnumChecks.enumValue(enumType, value), spec);
    }

    default <E extends Enum<E>> S enumValue(Class<E> enumType, String value) {
        return enumValue(enumType, value, s -> {
        });
    }

    default <E extends Enum<E>> S enumValue(Class<E> enumType, String value, ResponseCode code) {
        return enumValue(enumType, value, s -> s.responseCode(code));
    }

    default <E extends Enum<E>> S enumValue(Class<E> enumType, String value, ResponseCode code, String detail) {
        return enumValue(enumType, value, s -> s.responseCode(code).detail(detail));
    }

    // ========== enumConstant ==========

    default <E extends Enum<E>> S enumConstant(E value, Class<E> type, Consumer<ViolationSpec> spec) {
        return core().check(EnumChecks.enumConstant(value, type), spec);
    }

    default <E extends Enum<E>> S enumConstant(E value, Class<E> type) {
        return enumConstant(value, type, s -> {
        });
    }

    default <E extends Enum<E>> S enumConstant(E value, Class<E> type, ResponseCode code) {
        return enumConstant(value, type, s -> s.responseCode(code));
    }

    default <E extends Enum<E>> S enumConstant(E value, Class<E> type, ResponseCode code, String detail) {
        return enumConstant(value, type, s -> s.responseCode(code).detail(detail));
    }
}