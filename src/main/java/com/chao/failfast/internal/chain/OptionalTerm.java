package com.chao.failfast.internal.chain;

import com.chao.failfast.constant.FailureConst;
import com.chao.failfast.internal.check.OptionalChecks;
import com.chao.failfast.internal.core.ResponseCode;
import com.chao.failfast.internal.core.ViolationSpec;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Optional校验接口
 */
public interface OptionalTerm<S extends ChainCore<S>> {

    S core();

    // ========== isPresent ==========

    default S isPresent(Optional<?> opt, Consumer<ViolationSpec> spec) {
        return core().check(OptionalChecks.isPresent(opt), spec);
    }

    default S isPresent(Optional<?> opt) {
        return isPresent(opt, FailureConst.NO_OP);
    }

    default S isPresent(Optional<?> opt, ResponseCode code) {
        return isPresent(opt, s -> s.responseCode(code));
    }

    default S isPresent(Optional<?> opt, ResponseCode code, String detail) {
        return isPresent(opt, s -> s.responseCode(code).detail(detail));
    }

    // ========== isEmpty ==========

    default S isEmpty(Optional<?> opt, Consumer<ViolationSpec> spec) {
        return core().check(OptionalChecks.isEmpty(opt), spec);
    }

    default S isEmpty(Optional<?> opt) {
        return isEmpty(opt, FailureConst.NO_OP);
    }

    default S isEmpty(Optional<?> opt, ResponseCode code) {
        return isEmpty(opt, s -> s.responseCode(code));
    }

    default S isEmpty(Optional<?> opt, ResponseCode code, String detail) {
        return isEmpty(opt, s -> s.responseCode(code).detail(detail));
    }
}