package com.chao.failfast.internal.check;

/**
 * 枚举校验工具类
 */
public final class EnumChecks {

    private EnumChecks() {}

    public static <E extends Enum<E>> boolean enumValue(Class<E> enumType, String value) {
        if (enumType == null || value == null) {
            return false;
        }
        try {
            Enum.valueOf(enumType, value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static <E extends Enum<E>> boolean enumConstant(E value, Class<E> type) {
        return type != null && type.isInstance(value);
    }
}
