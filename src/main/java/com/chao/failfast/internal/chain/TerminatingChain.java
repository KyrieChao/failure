package com.chao.failfast.internal.chain;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.MultiBusiness;
import com.chao.failfast.internal.ResponseCode;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 终结操作链
 * 提供链式调用的终结操作，如抛出异常、执行回调等
 */
public abstract class TerminatingChain<C extends TerminatingChain<C>> extends IdentityChain<C> {

    protected TerminatingChain(boolean failFast) {
        super(failFast);
    }

    /**
     * 如果验证失败，立即抛出指定异常
     *
     * @param code 异常响应码
     * @return 当前链实例
     */
    public C failNow(ResponseCode code) {
        if (!alive) throw Business.of(code);
        return self();
    }

    /**
     * 如果验证失败，立即抛出指定异常（带自定义消息）
     *
     * @param code 异常响应码
     * @param msg  自定义消息
     * @return 当前链实例
     */
    public C failNow(ResponseCode code, String msg) {
        if (!alive) throw Business.of(code, msg);
        return self();
    }

    /**
     * 如果验证失败，立即抛出指定异常（带格式化消息）
     *
     * @param code      异常响应码
     * @param msgFormat 消息模板
     * @param args      格式化参数
     * @return 当前链实例
     */
    public C failNow(ResponseCode code, String msgFormat, Object... args) {
        if (!alive) throw Business.of(code, String.format(msgFormat, args));
        return self();
    }

    /**
     * 如果验证失败，立即抛出自定义构建的异常
     *
     * @param consumer 异常构建器消费者
     * @return 当前链实例
     */
    public C failNow(Consumer<Business.Fabricator> consumer) {
        if (!alive) {
            Business.Fabricator fabricator = Business.compose();
            consumer.accept(fabricator);
            throw fabricator.materialize();
        }
        return self();
    }

    /**
     * 如果验证失败，执行指定回调
     *
     * @param action 回调函数
     * @return 当前链实例
     */
    public C onFail(Runnable action) {
        if (!alive) action.run();
        return self();
    }

    /**
     * 如果验证失败，获取备用值
     *
     * @param supplier 备用值提供者
     * @param <T>      值类型
     * @return 包含备用值的Optional（失败时），或Empty（成功时）
     */
    public <T> Optional<T> onFailGet(Supplier<T> supplier) {
        return !alive
                ? Optional.ofNullable(supplier.get())
                : Optional.empty();
    }

    /**
     * 如果验证失败，抛出自定义异常
     *
     * @param exceptionSupplier 异常提供者
     * @return 当前链实例
     */
    public C failNow(Supplier<Business> exceptionSupplier) {
        if (!alive) throw exceptionSupplier.get();
        return self();
    }

    /**
     * 终结验证，如果有错误则抛出异常
     * 单个错误抛出 Business，多个错误抛出 MultiBusiness
     */
    public void failAll() {
        if (!isValid()) {
            if (errors.size() == 1) throw errors.get(0);
            throw new MultiBusiness(errors);
        }
    }
}
