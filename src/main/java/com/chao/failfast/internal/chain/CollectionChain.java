package com.chao.failfast.internal.chain;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.ResponseCode;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * 集合校验链
 * 提供集合相关的校验功能，如非空、大小、包含等
 */
public abstract class CollectionChain<C extends CollectionChain<C>> extends StringChain<C> {

    protected CollectionChain(boolean failFast) {
        super(failFast);
    }

    /**
     * 验证集合不能为空（不能为null且包含元素）
     *
     * @param col 待验证集合
     * @return 当前链实例
     */
    public C notEmpty(Collection<?> col) {
        return check(col != null && !col.isEmpty());
    }

    /**
     * 验证集合不能为空，失败时使用指定错误码
     *
     * @param col  待验证集合
     * @param code 错误码
     * @return 当前链实例
     */
    public C notEmpty(Collection<?> col, ResponseCode code) {
        return check(col != null && !col.isEmpty(), code);
    }

    /**
     * 验证集合不能为空，失败时使用自定义错误构建器
     *
     * @param col      待验证集合
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C notEmpty(Collection<?> col, Consumer<Business.Fabricator> consumer) {
        return check(col != null && !col.isEmpty(), consumer);
    }

    /**
     * 验证集合大小在指定范围内
     *
     * @param col 待验证集合
     * @param min 最小大小
     * @param max 最大大小
     * @return 当前链实例
     */
    public C sizeBetween(Collection<?> col, int min, int max) {
        if (!alive) return self();
        int size = (col == null) ? 0 : col.size();
        return check(size >= min && size <= max);
    }

    /**
     * 验证集合大小在指定范围内，失败时使用指定错误码
     *
     * @param col  待验证集合
     * @param min  最小大小
     * @param max  最大大小
     * @param code 错误码
     * @return 当前链实例
     */
    public C sizeBetween(Collection<?> col, int min, int max, ResponseCode code) {
        if (shouldSkip()) return self();
        int size = (col == null) ? 0 : col.size();
        return check(size >= min && size <= max, code);
    }

    /**
     * 验证集合大小在指定范围内，失败时使用自定义错误构建器
     *
     * @param col      待验证集合
     * @param min      最小大小
     * @param max      最大大小
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C sizeBetween(Collection<?> col, int min, int max, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return self();
        int size = (col == null) ? 0 : col.size();
        return check(size >= min && size <= max, consumer);
    }

    /**
     * 验证集合大小等于预期值
     *
     * @param col          待验证集合
     * @param expectedSize 预期大小
     * @return 当前链实例
     */
    public C sizeEquals(Collection<?> col, int expectedSize) {
        return check(col != null && col.size() == expectedSize);
    }

    /**
     * 验证集合大小等于预期值，失败时使用指定错误码
     *
     * @param col          待验证集合
     * @param expectedSize 预期大小
     * @param code         错误码
     * @return 当前链实例
     */
    public C sizeEquals(Collection<?> col, int expectedSize, ResponseCode code) {
        return check(col != null && col.size() == expectedSize, code);
    }

    /**
     * 验证集合大小等于预期值，失败时使用自定义错误构建器
     *
     * @param col          待验证集合
     * @param expectedSize 预期大小
     * @param consumer     错误构建器消费者
     * @return 当前链实例
     */
    public C sizeEquals(Collection<?> col, int expectedSize, Consumer<Business.Fabricator> consumer) {
        return check(col != null && col.size() == expectedSize, consumer);
    }

    /**
     * 验证集合包含指定元素
     *
     * @param col 待验证集合
     * @param o   指定元素
     * @return 当前链实例
     */
    public C contains(Collection<?> col, Object o) {
        return check(col != null && col.contains(o));
    }

    /**
     * 验证集合包含指定元素，失败时使用指定错误码
     *
     * @param col  待验证集合
     * @param o    指定元素
     * @param code 错误码
     * @return 当前链实例
     */
    public C contains(Collection<?> col, Object o, ResponseCode code) {
        return check(col != null && col.contains(o), code);
    }

    /**
     * 验证集合包含指定元素，失败时使用自定义错误构建器
     *
     * @param col      待验证集合
     * @param o        指定元素
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C contains(Collection<?> col, Object o, Consumer<Business.Fabricator> consumer) {
        return check(col != null && col.contains(o), consumer);
    }

    /**
     * 验证集合不包含指定元素
     *
     * @param col 待验证集合
     * @param o   指定元素
     * @return 当前链实例
     */
    public C notContains(Collection<?> col, Object o) {
        return check(col == null || !col.contains(o));
    }

    /**
     * 验证集合不包含指定元素，失败时使用指定错误码
     *
     * @param col  待验证集合
     * @param o    指定元素
     * @param code 错误码
     * @return 当前链实例
     */
    public C notContains(Collection<?> col, Object o, ResponseCode code) {
        if (shouldSkip()) return self();
        boolean invalid = col != null && col.contains(o);
        return check(!invalid, code);
    }

    /**
     * 验证集合不包含指定元素，失败时使用自定义错误构建器
     *
     * @param col      待验证集合
     * @param o        指定元素
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C notContains(Collection<?> col, Object o, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return self();
        boolean invalid = col != null && col.contains(o);
        return check(!invalid, consumer);
    }
}
