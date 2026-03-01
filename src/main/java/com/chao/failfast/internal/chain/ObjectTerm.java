package com.chao.failfast.internal.chain;

import com.chao.failfast.constant.FailureConst;
import com.chao.failfast.internal.check.ObjectChecks;
import com.chao.failfast.internal.core.ResponseCode;
import com.chao.failfast.internal.core.ViolationSpec;

import java.util.function.Consumer;

/**
 * 对象校验接口
 */
public interface ObjectTerm<S extends ChainCore<S>> {

    S core();

    // ========== notNull ==========

    default S notNull(Object obj, Consumer<ViolationSpec> spec) {
        return core().check(ObjectChecks.exists(obj), spec);
    }

    default S notNull(Object obj) {
        return notNull(obj, FailureConst.NO_OP);
    }

    default S notNull(Object obj, ResponseCode code) {
        return notNull(obj, s -> s.responseCode(code));
    }

    default S notNull(Object obj, ResponseCode code, String detail) {
        return notNull(obj, s -> s.responseCode(code).detail(detail));
    }

    default S exists(Object obj, Consumer<ViolationSpec> spec) {
        return notNull(obj, spec);
    }

    default S exists(Object obj) {
        return notNull(obj);
    }

    default S exists(Object obj, ResponseCode code) {
        return notNull(obj, code);
    }

    default S exists(Object obj, ResponseCode code, String detail) {
        return notNull(obj, code, detail);
    }

    // ========== isNull ==========

    default S isNull(Object obj, Consumer<ViolationSpec> spec) {
        return core().check(ObjectChecks.isNull(obj), spec);
    }

    default S isNull(Object obj) {
        return isNull(obj, FailureConst.NO_OP);
    }

    default S isNull(Object obj, ResponseCode code) {
        return isNull(obj, s -> s.responseCode(code));
    }

    default S isNull(Object obj, ResponseCode code, String detail) {
        return isNull(obj, s -> s.responseCode(code).detail(detail));
    }

    // ========== instanceOf ==========

    default S instanceOf(Object obj, Class<?> type, Consumer<ViolationSpec> spec) {
        return core().check(ObjectChecks.instanceOf(obj, type), spec);
    }

    default S instanceOf(Object obj, Class<?> type) {
        return instanceOf(obj, type, FailureConst.NO_OP);
    }

    default S instanceOf(Object obj, Class<?> type, ResponseCode code) {
        return instanceOf(obj, type, s -> s.responseCode(code));
    }

    default S instanceOf(Object obj, Class<?> type, ResponseCode code, String detail) {
        return instanceOf(obj, type, s -> s.responseCode(code).detail(detail));
    }

    // ========== notInstanceOf ==========

    default S notInstanceOf(Object obj, Class<?> type, Consumer<ViolationSpec> spec) {
        return core().check(ObjectChecks.notInstanceOf(obj, type), spec);
    }

    default S notInstanceOf(Object obj, Class<?> type) {
        return notInstanceOf(obj, type, FailureConst.NO_OP);
    }

    default S notInstanceOf(Object obj, Class<?> type, ResponseCode code) {
        return notInstanceOf(obj, type, s -> s.responseCode(code));
    }

    default S notInstanceOf(Object obj, Class<?> type, ResponseCode code, String detail) {
        return notInstanceOf(obj, type, s -> s.responseCode(code).detail(detail));
    }

    // ========== allNotNull ==========

    // 主方法：先传 spec，再传 objects（避免和 Consumer<ViolationSpec> 冲突）
    default S allNotNull(Consumer<ViolationSpec> spec, Object... objs) {
        return core().check(ObjectChecks.allNotNull(objs), spec);
    }

    // 无参版本
    default S allNotNull(Object... objs) {
        return allNotNull(FailureConst.NO_OP, objs);
    }

    default S allNotNull(ResponseCode code, Object... objs) {
        return allNotNull(s -> s.responseCode(code), objs);
    }

    /**
     * 默认方法：检查所有传入的对象是否不为null，并在响应中包含指定的响应码和详细信息
     *
     * @param code   响应码对象，用于标识响应状态
     * @param detail 响应的详细信息描述
     * @param objs   可变参数，需要检查是否为null的对象列表
     * @return 返回当前构建器实例S，支持链式调用
     */
    default S allNotNull(ResponseCode code, String detail, Object... objs) {
        return allNotNull(s -> s.responseCode(code).detail(detail), objs);
    }
}