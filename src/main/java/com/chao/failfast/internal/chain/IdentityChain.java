package com.chao.failfast.internal.chain;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.Ex;
import com.chao.failfast.internal.ResponseCode;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 对象同一性校验链
 * 提供对象引用比较(==)和等值比较(equals)功能
 */
public abstract class IdentityChain<C extends IdentityChain<C>> extends EnumChain<C> {

    protected IdentityChain(boolean failFast) {
        super(failFast);
    }

    /**
     * 验证两个对象引用是否指向同一对象（==）
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 当前链实例
     */
    public C same(Object obj1, Object obj2) {
        return check(obj1 == obj2);
    }

    /**
     * 验证两个对象引用是否指向同一对象，失败时使用指定错误码
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @param code 错误码
     * @return 当前链实例
     */
    public C same(Object obj1, Object obj2, ResponseCode code) {
        return check(obj1 == obj2, code);
    }

    /**
     * 验证两个对象引用是否指向同一对象，失败时使用自定义错误构建器
     *
     * @param obj1     对象1
     * @param obj2     对象2
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C same(Object obj1, Object obj2, Consumer<Business.Fabricator> consumer) {
        return check(obj1 == obj2, consumer);
    }

    /**
     * 验证两个对象引用是否指向不同对象（!=）
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 当前链实例
     */
    public C notSame(Object obj1, Object obj2) {
        return check(obj1 != obj2);
    }

    /**
     * 验证两个对象引用是否指向不同对象，失败时使用指定错误码
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @param code 错误码
     * @return 当前链实例
     */
    public C notSame(Object obj1, Object obj2, ResponseCode code) {
        return check(obj1 != obj2, code);
    }

    /**
     * 验证两个对象引用是否指向不同对象，失败时使用自定义错误构建器
     *
     * @param obj1     对象1
     * @param obj2     对象2
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C notSame(Object obj1, Object obj2, Consumer<Business.Fabricator> consumer) {
        return check(obj1 != obj2, consumer);
    }

    /**
     * 验证两个对象是否相等（equals）
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 当前链实例
     */
    public C equals(Object obj1, Object obj2) {
        return check(Objects.equals(obj1, obj2));
    }

    /**
     * 验证两个对象是否相等，失败时使用指定错误码
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @param code 错误码
     * @return 当前链实例
     */
    public C equals(Object obj1, Object obj2, ResponseCode code) {
        return check(Objects.equals(obj1, obj2), code);
    }

    /**
     * 验证两个对象是否相等，失败时使用自定义错误构建器
     *
     * @param obj1     对象1
     * @param obj2     对象2
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C equals(Object obj1, Object obj2, Consumer<Business.Fabricator> consumer) {
        return check(Objects.equals(obj1, obj2), consumer);
    }

    /**
     * 验证两个对象是否不相等（!equals）
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 当前链实例
     */
    public C notEquals(Object obj1, Object obj2) {
        return check(!Objects.equals(obj1, obj2));
    }

    /**
     * 验证两个对象是否不相等，失败时使用指定错误码
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @param code 错误码
     * @return 当前链实例
     */
    public C notEquals(Object obj1, Object obj2, ResponseCode code) {
        return check(!Objects.equals(obj1, obj2), code);
    }

    /**
     * 验证两个对象是否不相等，失败时使用自定义错误构建器
     *
     * @param obj1     对象1
     * @param obj2     对象2
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C notEquals(Object obj1, Object obj2, Consumer<Business.Fabricator> consumer) {
        return check(!Objects.equals(obj1, obj2), consumer);
    }
}
