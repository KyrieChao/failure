package com.chao.failfast.internal.chain;

import com.chao.failfast.constant.FailureConst;
import com.chao.failfast.internal.check.BooleanChecks;
import com.chao.failfast.internal.core.ResponseCode;
import com.chao.failfast.internal.core.ViolationSpec;

import java.util.function.Consumer;

/**
 * 布尔校验接口
 */
public interface BooleanTerm<S extends ChainCore<S>> {

    S core();

    // ========== state ==========

    default S state(boolean condition, Consumer<ViolationSpec> spec) {
        return core().check(BooleanChecks.state(condition), spec);
    }

    default S state(boolean condition) {
        return state(condition, FailureConst.NO_OP);
    }

    default S state(boolean condition, ResponseCode code) {
        return state(condition, s -> s.responseCode(code));
    }

    default S state(boolean condition, ResponseCode code, String detail) {
        return state(condition, s -> s.responseCode(code).detail(detail));
    }

    // ========== isTrue ==========

    default S isTrue(boolean condition, Consumer<ViolationSpec> spec) {
        return core().check(BooleanChecks.isTrue(condition), spec);
    }

    default S isTrue(boolean condition) {
        return isTrue(condition, FailureConst.NO_OP);
    }

    default S isTrue(boolean condition, ResponseCode code) {
        return isTrue(condition, s -> s.responseCode(code));
    }

    default S isTrue(boolean condition, ResponseCode code, String detail) {
        return isTrue(condition, s -> s.responseCode(code).detail(detail));
    }

    // ========== isFalse ==========

    default S isFalse(boolean condition, Consumer<ViolationSpec> spec) {
        return core().check(BooleanChecks.isFalse(condition), spec);
    }

    default S isFalse(boolean condition) {
        return isFalse(condition, FailureConst.NO_OP);
    }

    default S isFalse(boolean condition, ResponseCode code) {
        return isFalse(condition, s -> s.responseCode(code));
    }

    default S isFalse(boolean condition, ResponseCode code, String detail) {
        return isFalse(condition, s -> s.responseCode(code).detail(detail));
    }
}