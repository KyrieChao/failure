package com.chao.failfast.internal.chain;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.ResponseCode;

import java.util.function.Consumer;

/**
 * 数值校验链
 * 提供数值范围、正负数等校验功能
 */
public abstract class NumberChain<C extends NumberChain<C>> extends ArrayChain<C> {

    protected NumberChain(boolean failFast) {
        super(failFast);
    }

    /**
     * 验证数值必须为正数（大于0）
     *
     * @param value 待验证数值
     * @return 当前链实例
     */
    public C positive(Number value) {
        return check(value != null && value.doubleValue() > 0);
    }

    /**
     * 验证数值必须为正数，失败时使用指定错误码
     *
     * @param value 待验证数值
     * @param code  错误码
     * @return 当前链实例
     */
    public C positive(Number value, ResponseCode code) {
        return check(value != null && value.doubleValue() > 0, code);
    }

    /**
     * 验证数值必须为正数，失败时使用自定义错误构建器
     *
     * @param value    待验证数值
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C positive(Number value, Consumer<Business.Fabricator> consumer) {
        return check(value != null && value.doubleValue() > 0, consumer);
    }

    /**
     * 验证数值必须为正数（大于0），同 positive
     *
     * @param value 待验证数值
     * @return 当前链实例
     */
    public C positiveNumber(Number value) {
        return positive(value);
    }

    /**
     * 验证数值必须为正数，失败时使用指定错误码
     *
     * @param value 待验证数值
     * @param code  错误码
     * @return 当前链实例
     */
    public C positiveNumber(Number value, ResponseCode code) {
        return positive(value, code);
    }

    /**
     * 验证数值必须为正数，失败时使用自定义错误构建器
     *
     * @param value    待验证数值
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C positiveNumber(Number value, Consumer<Business.Fabricator> consumer) {
        return positive(value, consumer);
    }

    /**
     * 验证数值在指定范围内（闭区间 [min, max]）
     *
     * @param value 待验证数值
     * @param min   最小值
     * @param max   最大值
     * @param <T>   数值类型
     * @return 当前链实例
     */
    public <T extends Number & Comparable<T>> C inRange(T value, T min, T max) {
        if (!alive) return self();
        boolean ok = value != null && min != null && max != null
                && value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
        return check(ok);
    }

    /**
     * 验证数值在指定范围内，失败时使用指定错误码
     *
     * @param value 待验证数值
     * @param min   最小值
     * @param max   最大值
     * @param code  错误码
     * @param <T>   数值类型
     * @return 当前链实例
     */
    public <T extends Number & Comparable<T>> C inRange(T value, T min, T max, ResponseCode code) {
        if (shouldSkip()) return self();
        boolean ok = value != null && min != null && max != null && value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
        return check(ok, code);
    }

    /**
     * 验证数值在指定范围内，失败时使用自定义错误构建器
     *
     * @param value    待验证数值
     * @param min      最小值
     * @param max      最大值
     * @param consumer 错误构建器消费者
     * @param <T>      数值类型
     * @return 当前链实例
     */
    public <T extends Number & Comparable<T>> C inRange(T value, T min, T max, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return self();
        boolean ok = value != null && min != null && max != null && value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
        return check(ok, consumer);
    }

    /**
     * 验证任意Number类型数值在指定范围内
     *
     * @param v   待验证数值
     * @param min 最小值
     * @param max 最大值
     * @return 当前链实例
     */
    public C inRangeNumber(Number v, Number min, Number max) {
        if (!alive) return self();
        boolean ok = range(v, min, max);
        return check(ok);
    }

    /**
     * 验证任意Number类型数值在指定范围内，失败时使用指定错误码
     *
     * @param v    待验证数值
     * @param min  最小值
     * @param max  最大值
     * @param code 错误码
     * @return 当前链实例
     */
    public C inRangeNumber(Number v, Number min, Number max, ResponseCode code) {
        if (shouldSkip()) return self();
        boolean ok = range(v, min, max);
        return check(ok, code);
    }

    /**
     * 验证任意Number类型数值在指定范围内，失败时使用自定义错误构建器
     *
     * @param v        待验证数值
     * @param min      最小值
     * @param max      最大值
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C inRangeNumber(Number v, Number min, Number max, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return self();
        boolean ok = range(v, min, max);
        return check(ok, consumer);
    }

    /**
     * 检查数值是否在范围内
     *
     * @param v   数值
     * @param min 最小值
     * @param max 最大值
     * @return 如果在范围内返回true，否则返回false
     */
    private boolean range(Number v, Number min, Number max) {
        return v != null && min != null && max != null
                && v.doubleValue() >= min.doubleValue()
                && v.doubleValue() <= max.doubleValue();
    }

    /**
     * 验证数值必须为非负数（大于等于0）
     *
     * @param value 待验证数值
     * @param <T>   数值类型
     * @return 当前链实例
     */
    public <T extends Number & Comparable<T>> C nonNegative(T value) {
        return check(value != null && value.doubleValue() >= 0);
    }

    /**
     * 验证数值必须为非负数，失败时使用指定错误码
     *
     * @param value 待验证数值
     * @param code  错误码
     * @param <T>   数值类型
     * @return 当前链实例
     */
    public <T extends Number & Comparable<T>> C nonNegative(T value, ResponseCode code) {
        return check(value != null && value.doubleValue() >= 0, code);
    }

    /**
     * 验证数值必须为非负数，失败时使用自定义错误构建器
     *
     * @param value    待验证数值
     * @param consumer 错误构建器消费者
     * @param <T>      数值类型
     * @return 当前链实例
     */
    public <T extends Number & Comparable<T>> C nonNegative(T value, Consumer<Business.Fabricator> consumer) {
        return check(value != null && value.doubleValue() >= 0, consumer);
    }
}
