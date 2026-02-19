package com.chao.failfast.internal.chain;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.ResponseCode;

import java.util.function.Consumer;

/**
 * 枚举校验链
 * 提供枚举值的有效性校验功能
 */
public abstract class EnumChain<C extends EnumChain<C>> extends DateChain<C> {

    protected EnumChain(boolean failFast) {
        super(failFast);
    }

    /**
     * 验证字符串值是否为指定枚举类的有效值
     *
     * @param enumType 枚举类类型
     * @param value    待验证字符串值
     * @param <E>      枚举类型
     * @return 当前链实例
     */
    public <E extends Enum<E>> C enumValue(Class<E> enumType, String value) {
        if (!alive) return self();
        boolean valid = isValidEnum(enumType, value);
        return check(valid);
    }

    /**
     * 验证字符串值是否为指定枚举类的有效值，失败时使用指定错误码
     *
     * @param enumType 枚举类类型
     * @param value    待验证字符串值
     * @param code     错误码
     * @param <E>      枚举类型
     * @return 当前链实例
     */
    public <E extends Enum<E>> C enumValue(Class<E> enumType, String value, ResponseCode code) {
        if (shouldSkip()) return self();
        boolean valid = isValidEnum(enumType, value);
        return check(valid, code);
    }

    /**
     * 验证字符串值是否为指定枚举类的有效值，失败时使用自定义错误构建器
     *
     * @param enumType 枚举类类型
     * @param value    待验证字符串值
     * @param consumer 错误构建器消费者
     * @param <E>      枚举类型
     * @return 当前链实例
     */
    public <E extends Enum<E>> C enumValue(Class<E> enumType, String value, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return self();
        boolean valid = isValidEnum(enumType, value);
        return check(valid, consumer);
    }

    /**
     * 检查值是否为有效的枚举值
     *
     * @param enumType 枚举类型
     * @param value    字符串值
     * @param <E>      枚举泛型
     * @return 如果有效返回true，否则返回false
     */
    private <E extends Enum<E>> boolean isValidEnum(Class<E> enumType, String value) {
        if (value == null) return false;
        try {
            Enum.valueOf(enumType, value);
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}
