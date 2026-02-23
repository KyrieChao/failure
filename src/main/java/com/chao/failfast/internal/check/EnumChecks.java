package com.chao.failfast.internal.check;

/**
 * 枚举校验工具类
 * 提供用于验证枚举值和枚举常量的静态方法
 */
public final class EnumChecks {

    /**
     * 私有构造函数，防止实例化工具类
     */
    private EnumChecks() {}

    /**
     * 检查给定的字符串值是否是指定枚举类型的有效值
     *
     * @param <E> 枚举类型
     * @param enumType 要检查的枚举类
     * @param value 要检查的字符串值
     * @return 如果字符串值是枚举的有效值则返回true，否则返回false
     */
    public static <E extends Enum<E>> boolean enumValue(Class<E> enumType, String value) {
        // 检查枚举类型和值是否为null
        if (enumType == null || value == null) {
            return false;
        }
        try {
            // 尝试将字符串值转换为枚举常量
            Enum.valueOf(enumType, value);
            return true;
        } catch (IllegalArgumentException e) {
            // 如果转换失败，则返回false
            return false;
        }
    }

    /**
     * 检查给定的对象是否是指定枚举类型的实例
     *
     * @param <E> 枚举类型
     * @param value 要检查的对象
     * @param type 要检查的枚举类
     * @return 如果对象是指定枚举类型的实例则返回true，否则返回false
     */
    public static <E extends Enum<E>> boolean enumConstant(E value, Class<E> type) {
        // 检查类型是否不为null且对象是该类型的实例
        return type != null && type.isInstance(value);
    }
}
