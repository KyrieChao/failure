package com.chao.failfast.internal.chain;

import com.chao.failfast.internal.check.IdentityChecks;
import com.chao.failfast.internal.core.ResponseCode;
import com.chao.failfast.internal.core.ViolationSpec;

import java.util.function.Consumer;

/**
 * 对象同一性校验接口
 */
public interface IdentityTerm<S extends ChainCore<S>> {

    S core();

    // ========== same ==========

    default S same(Object obj1, Object obj2, Consumer<ViolationSpec> spec) {
        return core().check(IdentityChecks.same(obj1, obj2), spec);
    }

    default S same(Object obj1, Object obj2) {
        return same(obj1, obj2, ValidationConst.NO_OP);
    }

    default S same(Object obj1, Object obj2, ResponseCode code) {
        return same(obj1, obj2, s -> s.responseCode(code));
    }

    default S same(Object obj1, Object obj2, ResponseCode code, String detail) {
        return same(obj1, obj2, s -> s.responseCode(code).detail(detail));
    }

    // ========== notSame ==========

    default S notSame(Object obj1, Object obj2, Consumer<ViolationSpec> spec) {
        return core().check(IdentityChecks.notSame(obj1, obj2), spec);
    }

    default S notSame(Object obj1, Object obj2) {
        return notSame(obj1, obj2, ValidationConst.NO_OP);
    }

    default S notSame(Object obj1, Object obj2, ResponseCode code) {
        return notSame(obj1, obj2, s -> s.responseCode(code));
    }

    default S notSame(Object obj1, Object obj2, ResponseCode code, String detail) {
        return notSame(obj1, obj2, s -> s.responseCode(code).detail(detail));
    }

    // ========== equals ==========

    default S equals(Object obj1, Object obj2, Consumer<ViolationSpec> spec) {
        return core().check(IdentityChecks.equals(obj1, obj2), spec);
    }

    default S equals(Object obj1, Object obj2) {
        return equals(obj1, obj2, ValidationConst.NO_OP);
    }

    default S equals(Object obj1, Object obj2, ResponseCode code) {
        return equals(obj1, obj2, s -> s.responseCode(code));
    }

    default S equals(Object obj1, Object obj2, ResponseCode code, String detail) {
        return equals(obj1, obj2, s -> s.responseCode(code).detail(detail));
    }

    // ========== notEquals ==========

    default S notEquals(Object obj1, Object obj2, Consumer<ViolationSpec> spec) {
        return core().check(IdentityChecks.notEquals(obj1, obj2), spec);
    }

    default S notEquals(Object obj1, Object obj2) {
        return notEquals(obj1, obj2, ValidationConst.NO_OP);
    }

    default S notEquals(Object obj1, Object obj2, ResponseCode code) {
        return notEquals(obj1, obj2, s -> s.responseCode(code));
    }

    default S notEquals(Object obj1, Object obj2, ResponseCode code, String detail) {
        return notEquals(obj1, obj2, s -> s.responseCode(code).detail(detail));
    }
}