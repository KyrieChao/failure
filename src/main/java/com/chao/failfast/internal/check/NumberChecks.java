package com.chao.failfast.internal.check;

/**
 * 数值校验工具类
 */
public final class NumberChecks {

    private NumberChecks() {}

    public static boolean positive(Number value) {
        return value != null && value.doubleValue() > 0;
    }

    public static <T extends Number & Comparable<T>> boolean inRange(T value, T min, T max) {
        return value != null && min != null && max != null
                && value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
    }

    public static boolean inRangeNumber(Number v, Number min, Number max) {
        return v != null && min != null && max != null
                && v.doubleValue() >= min.doubleValue()
                && v.doubleValue() <= max.doubleValue();
    }

    public static boolean nonNegative(Number value) {
        return value != null && value.doubleValue() >= 0;
    }
}
