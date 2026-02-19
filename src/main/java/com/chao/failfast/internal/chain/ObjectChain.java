package com.chao.failfast.internal.chain;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.ResponseCode;

import java.util.function.Consumer;

/**
 * 对象存在性校验链
 * 提供基础的对象非空/空值校验功能
 */
public abstract class ObjectChain<C extends ObjectChain<C>> extends AbstractChain<C> {

    protected ObjectChain(boolean failFast) {
        super(failFast);
    }

    /**
     * 验证对象是否存在（非 null）
     *
     * @param obj 待验证对象
     * @return 当前链实例
     */
    public C exists(Object obj) {
        return check(obj != null);
    }

    /**
     * 验证对象是否存在（非 null），失败时使用指定错误码
     *
     * @param obj  待验证对象
     * @param code 错误码
     * @return 当前链实例
     */
    public C exists(Object obj, ResponseCode code) {
        return check(obj != null, code);
    }

    /**
     * 验证对象是否存在（非 null），失败时使用自定义错误构建器
     *
     * @param obj      待验证对象
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C exists(Object obj, Consumer<Business.Fabricator> consumer) {
        return check(obj != null, consumer);
    }

    /**
     * 验证对象不为 null（同 exists）
     *
     * @param obj 待验证对象
     * @return 当前链实例
     */
    public C notNull(Object obj) {
        return exists(obj);
    }

    /**
     * 验证对象不为 null，失败时使用指定错误码
     *
     * @param obj  待验证对象
     * @param code 错误码
     * @return 当前链实例
     */
    public C notNull(Object obj, ResponseCode code) {
        return exists(obj, code);
    }

    /**
     * 验证对象不为 null，失败时使用自定义错误构建器
     *
     * @param obj      待验证对象
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C notNull(Object obj, Consumer<Business.Fabricator> consumer) {
        return exists(obj, consumer);
    }

    /**
     * 验证对象必须为 null
     *
     * @param obj 待验证对象
     * @return 当前链实例
     */
    public C isNull(Object obj) {
        return check(obj == null);
    }

    /**
     * 验证对象必须为 null，失败时使用指定错误码
     *
     * @param obj  待验证对象
     * @param code 错误码
     * @return 当前链实例
     */
    public C isNull(Object obj, ResponseCode code) {
        return check(obj == null, code);
    }

    /**
     * 验证对象必须为 null，失败时使用自定义错误构建器
     *
     * @param obj      待验证对象
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C isNull(Object obj, Consumer<Business.Fabricator> consumer) {
        return check(obj == null, consumer);
    }
}
