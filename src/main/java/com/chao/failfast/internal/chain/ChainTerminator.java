package com.chao.failfast.internal.chain;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.MultiBusiness;
import com.chao.failfast.internal.core.ResponseCode;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 终结操作接口
 */
public interface ChainTerminator<S extends ChainCore<S>> {

    S core();


    /**
     * 默认的失败处理方法，当验证失败时抛出相应的业务异常
     * 如果验证失败但没有具体原因，则抛出通用的验证失败异常
     * 如果验证失败且有具体原因，则抛出第一个具体原因对应的异常
     */
    default void fail() {
        if (!core().isValid()) {
            if (core().getCauses().isEmpty()) {
                throw Business.of(
                        ResponseCode.of(500, "Validation failed", "链式验证缺少具体错误配置")
                );
            }
            throw core().getCauses().get(0);
        }
    }

    /**
     * 默认方法，用于处理验证失败的情况
     * 如果验证无效，则根据错误原因的数量抛出不同的异常
     */
    default void failAll() {
        if (!core().isValid()) {
            if (core().getCauses().isEmpty()) {
                throw Business.of(ResponseCode.of(500, "Validation failed", "链式验证缺少具体错误配置"));
            }
            if (core().getCauses().size() == 1) throw core().getCauses().get(0);
            throw new MultiBusiness(core().getCauses());
        }
    }


    default S failNow(ResponseCode code) {
        if (core().isAlive()) return core();
        throw Business.of(code);
    }

    default S failNow(ResponseCode code, String detail) {
        if (core().isAlive()) return core();
        throw Business.of(code, detail);
    }

    /**
     * 格式化消息版本
     */
    default S failNow(ResponseCode code, String msgFormat, Object... args) {
        if (core().isAlive()) return core();
        throw Business.of(code, String.format(msgFormat, args));
    }

    default S failNow(Consumer<Business.Fabricator> fabricator) {
        if (core().isAlive()) return core();
        Business.Fabricator f = Business.compose();
        fabricator.accept(f);
        throw f.materialize();
    }

    /**
     * Supplier 版本 - 延迟创建异常
     */
    default S failNow(Supplier<Business> supplier) {
        if (core().isAlive()) return core();
        throw supplier.get();
    }


    default S onFail(Runnable action) {
        if (!core().isAlive()) action.run();
        return core();
    }

    default <T> Optional<T> onFailGet(Supplier<T> supplier) {
        return !core().isAlive() ? Optional.ofNullable(supplier.get()) : Optional.empty();
    }


    default void verify() {
        // No-op: Errors are reported to context immediately
    }
}