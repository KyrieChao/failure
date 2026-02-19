package com.chao.failfast.internal.chain;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.ResponseCode;

import java.util.function.Consumer;

/**
 * 布尔状态校验链
 * 提供布尔值、状态条件等校验功能
 */
public abstract class BooleanChain<C extends BooleanChain<C>> extends ObjectChain<C> {

    protected BooleanChain(boolean failFast) {
        super(failFast);
    }

    /**
     * 验证布尔状态是否为 true
     *
     * @param condition 待验证条件
     * @return 当前链实例
     */
    public C state(boolean condition) {
        return check(condition);
    }

    /**
     * 验证布尔状态是否为 true，失败时使用指定错误码
     *
     * @param condition 待验证条件
     * @param code      错误码
     * @return 当前链实例
     */
    public C state(boolean condition, ResponseCode code) {
        return check(condition, code);
    }

    /**
     * 验证布尔状态是否为 true，失败时使用自定义错误构建器
     *
     * @param condition 待验证条件
     * @param consumer  错误构建器消费者
     * @return 当前链实例
     */
    public C state(boolean condition, Consumer<Business.Fabricator> consumer) {
        return check(condition, consumer);
    }

    /**
     * 验证布尔值是否为 true
     *
     * @param cond 待验证布尔值
     * @return 当前链实例
     */
    public C isTrue(boolean cond) {
        return state(cond);
    }

    /**
     * 验证布尔值是否为 true，失败时使用指定错误码
     *
     * @param cond 待验证布尔值
     * @param code 错误码
     * @return 当前链实例
     */
    public C isTrue(boolean cond, ResponseCode code) {
        return state(cond, code);
    }

    /**
     * 验证布尔值是否为 true，失败时使用自定义错误构建器
     *
     * @param cond     待验证布尔值
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C isTrue(boolean cond, Consumer<Business.Fabricator> consumer) {
        return state(cond, consumer);
    }

    /**
     * 验证布尔值是否为 false
     *
     * @param cond 待验证布尔值
     * @return 当前链实例
     */
    public C isFalse(boolean cond) {
        return state(!cond);
    }

    /**
     * 验证布尔值是否为 false，失败时使用指定错误码
     *
     * @param cond 待验证布尔值
     * @param code 错误码
     * @return 当前链实例
     */
    public C isFalse(boolean cond, ResponseCode code) {
        return state(!cond, code);
    }

    /**
     * 验证布尔值是否为 false，失败时使用自定义错误构建器
     *
     * @param cond     待验证布尔值
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C isFalse(boolean cond, Consumer<Business.Fabricator> consumer) {
        return state(!cond, consumer);
    }
}
