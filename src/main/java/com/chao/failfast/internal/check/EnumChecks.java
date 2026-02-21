package com.chao.failfast.internal.check;

/**
 * 枚举校验工具类
 */
public final class EnumChecks {

    private EnumChecks() {}

    public static <E extends Enum<E>> boolean isValidEnum(Class<E> enumType, String value) {
        if (value == null) return false;
        try {
            Enum.valueOf(enumType, value);
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}
