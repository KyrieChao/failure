package com.chao.failfast.internal.chain;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.ResponseCode;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 数组校验链
 * 提供数组相关的校验功能，如非空、大小、包含等
 */
public abstract class ArrayChain<C extends ArrayChain<C>> extends CollectionChain<C> {

    protected ArrayChain(boolean failFast) {
        super(failFast);
    }

    /**
     * 验证数组不能为空（不能为null且包含元素）
     *
     * @param array 待验证数组
     * @param <T>   数组元素类型
     * @return 当前链实例
     */
    public <T> C notEmpty(T[] array) {
        return check(array != null && array.length > 0);
    }

    /**
     * 验证数组不能为空，失败时使用指定错误码
     *
     * @param array 待验证数组
     * @param code  错误码
     * @param <T>   数组元素类型
     * @return 当前链实例
     */
    public <T> C notEmpty(T[] array, ResponseCode code) {
        return check(array != null && array.length > 0, code);
    }

    /**
     * 验证数组不能为空，失败时使用自定义错误构建器
     *
     * @param array    待验证数组
     * @param consumer 错误构建器消费者
     * @param <T>      数组元素类型
     * @return 当前链实例
     */
    public <T> C notEmpty(T[] array, Consumer<Business.Fabricator> consumer) {
        return check(array != null && array.length > 0, consumer);
    }

    /**
     * 验证数组大小在指定范围内
     *
     * @param array 待验证数组
     * @param min   最小大小
     * @param max   最大大小
     * @param <T>   数组元素类型
     * @return 当前链实例
     */
    public <T> C sizeBetween(T[] array, int min, int max) {
        if (!alive) return self();
        int size = (array == null) ? 0 : array.length;
        return check(size >= min && size <= max);
    }

    /**
     * 验证数组大小在指定范围内，失败时使用指定错误码
     *
     * @param array 待验证数组
     * @param min   最小大小
     * @param max   最大大小
     * @param code  错误码
     * @param <T>   数组元素类型
     * @return 当前链实例
     */
    public <T> C sizeBetween(T[] array, int min, int max, ResponseCode code) {
        if (shouldSkip()) return self();
        int size = (array == null) ? 0 : array.length;
        return check(size >= min && size <= max, code);
    }

    /**
     * 验证数组大小在指定范围内，失败时使用自定义错误构建器
     *
     * @param array    待验证数组
     * @param min      最小大小
     * @param max      最大大小
     * @param consumer 错误构建器消费者
     * @param <T>      数组元素类型
     * @return 当前链实例
     */
    public <T> C sizeBetween(T[] array, int min, int max, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return self();
        int size = (array == null) ? 0 : array.length;
        return check(size >= min && size <= max, consumer);
    }

    /**
     * 验证数组大小等于预期值
     *
     * @param array        待验证数组
     * @param expectedSize 预期大小
     * @param <T>          数组元素类型
     * @return 当前链实例
     */
    public <T> C sizeEquals(T[] array, int expectedSize) {
        return check(array != null && array.length == expectedSize);
    }

    /**
     * 验证数组大小等于预期值，失败时使用指定错误码
     *
     * @param array        待验证数组
     * @param expectedSize 预期大小
     * @param code         错误码
     * @param <T>          数组元素类型
     * @return 当前链实例
     */
    public <T> C sizeEquals(T[] array, int expectedSize, ResponseCode code) {
        return check(array != null && array.length == expectedSize, code);
    }

    /**
     * 验证数组大小等于预期值，失败时使用自定义错误构建器
     *
     * @param array        待验证数组
     * @param expectedSize 预期大小
     * @param consumer     错误构建器消费者
     * @param <T>          数组元素类型
     * @return 当前链实例
     */
    public <T> C sizeEquals(T[] array, int expectedSize, Consumer<Business.Fabricator> consumer) {
        return check(array != null && array.length == expectedSize, consumer);
    }

    /**
     * 验证数组包含指定元素
     *
     * @param array 待验证数组
     * @param o     指定元素
     * @param <T>   数组元素类型
     * @return 当前链实例
     */
    public <T> C contains(T[] array, T o) {
        if (!alive) return self();
        boolean found = containsElement(array, o);
        return check(found);
    }

    /**
     * 验证数组包含指定元素，失败时使用指定错误码
     *
     * @param array 待验证数组
     * @param o     指定元素
     * @param code  错误码
     * @param <T>   数组元素类型
     * @return 当前链实例
     */
    public <T> C contains(T[] array, T o, ResponseCode code) {
        if (shouldSkip()) return self();
        boolean found = containsElement(array, o);
        return check(found, code);
    }

    /**
     * 验证数组包含指定元素，失败时使用自定义错误构建器
     *
     * @param array    待验证数组
     * @param o        指定元素
     * @param consumer 错误构建器消费者
     * @param <T>      数组元素类型
     * @return 当前链实例
     */
    public <T> C contains(T[] array, T o, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return self();
        boolean found = containsElement(array, o);
        return check(found, consumer);
    }

    /**
     * 验证数组不包含指定元素
     *
     * @param array 待验证数组
     * @param o     指定元素
     * @param <T>   数组元素类型
     * @return 当前链实例
     */
    public <T> C notContains(T[] array, T o) {
        if (!alive) return self();
        boolean found = containsElement(array, o);
        return check(!found);
    }

    /**
     * 验证数组不包含指定元素，失败时使用指定错误码
     *
     * @param array 待验证数组
     * @param o     指定元素
     * @param code  错误码
     * @param <T>   数组元素类型
     * @return 当前链实例
     */
    public <T> C notContains(T[] array, T o, ResponseCode code) {
        if (shouldSkip()) return self();
        boolean found = containsElement(array, o);
        return check(!found, code);
    }

    /**
     * 验证数组不包含指定元素，失败时使用自定义错误构建器
     *
     * @param array    待验证数组
     * @param o        指定元素
     * @param consumer 错误构建器消费者
     * @param <T>      数组元素类型
     * @return 当前链实例
     */
    public <T> C notContains(T[] array, T o, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return self();
        boolean found = containsElement(array, o);
        return check(!found, consumer);
    }

    /**
     * 检查数组是否包含指定元素
     *
     * @param array 数组
     * @param o     元素
     * @param <T>   元素类型
     * @return 如果包含返回true，否则返回false
     */
    private <T> boolean containsElement(T[] array, T o) {
        boolean found = false;
        if (array != null) {
            for (T item : array) {
                if (Objects.equals(item, o)) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }
}
