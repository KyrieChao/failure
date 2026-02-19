package com.chao.failfast.internal.chain;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.Ex;
import com.chao.failfast.internal.ResponseCode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 验证链基础抽象类
 * 提供基础的状态管理、错误收集和链式调用支持
 *
 * @param <C> 子类类型（用于链式调用返回自身）
 */
public abstract class AbstractChain<C extends AbstractChain<C>> {

    /**
     * 是否启用快速失败模式
     * true: 遇到第一个错误立即停止后续验证
     * false: 收集所有错误，不中断验证
     */
    protected final boolean failFast;

    /**
     * 验证链是否仍然活跃（未中断）
     * 在快速失败模式下，一旦发生错误，alive 将变为 false
     */
    protected boolean alive = true;

    /**
     * 收集的验证错误列表
     */
    protected final List<Business> errors = new ArrayList<>();

    /**
     * 构造函数
     *
     * @param failFast 是否启用快速失败模式
     */
    protected AbstractChain(boolean failFast) {
        this.failFast = failFast;
    }

    /**
     * 检查是否应该跳过当前验证
     *
     * @return 如果是快速失败模式且已发生错误，则返回 true
     */
    protected boolean shouldSkip() {
        return (!alive && failFast);
    }

    /**
     * 返回当前链实例（用于泛型链式调用）
     *
     * @return 当前实例
     */
    @SuppressWarnings("unchecked")
    protected C self() {
        return (C) this;
    }

    /**
     * 执行基础验证（无错误码）
     * 仅更新 alive 状态，不记录错误信息
     *
     * @param condition 验证条件
     * @return 当前链实例
     */
    protected C check(boolean condition) {
        if (!alive) return self();
        this.alive = condition;
        return self();
    }

    /**
     * 执行验证（带错误码）
     *
     * @param condition 验证条件
     * @param code      验证失败时的错误码
     * @return 当前链实例
     */
    protected C check(boolean condition, ResponseCode code) {
        if (shouldSkip()) return self();
        if (!condition) {
            addError(code);
            if (failFast) alive = false;
        }
        return self();
    }

    /**
     * 执行验证（带自定义错误构建器）
     *
     * @param condition 验证条件
     * @param consumer  错误构建器消费者
     * @return 当前链实例
     */
    protected C check(boolean condition, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return self();
        if (!condition) {
            addError(consumer);
            if (failFast) {
                alive = false;
            }
        }
        return self();
    }

    /**
     * 添加错误信息
     *
     * @param code 错误码
     */
    protected void addError(ResponseCode code) {
        errors.add(Business.of(code));
    }

    /**
     * 添加自定义错误信息
     *
     * @param consumer 错误构建器消费者
     */
    protected void addError(Consumer<Business.Fabricator> consumer) {
        Business.Fabricator fabricator = Business.compose();
        consumer.accept(fabricator);
        errors.add(fabricator.materialize());
    }

    /**
     * 获取所有收集到的错误
     *
     * @return 错误列表副本
     */
    public List<Business> getErrors() {
        return new ArrayList<>(errors);
    }

    /**
     * 检查当前链是否验证通过
     *
     * @return true 表示没有错误且链仍活跃（或未发生错误）
     */
    public boolean isValid() {
        return errors.isEmpty() && alive;
    }
}
