package com.chao.failfast.internal.check;

/**
 * 数值校验工具类
 */
public final class NumberChecks {

    private NumberChecks() {
    }

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

    public static <T extends Number & Comparable<T>> boolean greaterThan(T value, T threshold) {
        return value != null && threshold != null && value.compareTo(threshold) > 0;
    }

    public static <T extends Number & Comparable<T>> boolean greaterOrEqual(T value, T threshold) {
        return value != null && threshold != null && value.compareTo(threshold) >= 0;
    }

    public static <T extends Number & Comparable<T>> boolean lessThan(T value, T threshold) {
        return value != null && threshold != null && value.compareTo(threshold) < 0;
    }

    public static <T extends Number & Comparable<T>> boolean lessOrEqual(T value, T threshold) {
        return value != null && threshold != null && value.compareTo(threshold) <= 0;
    }

    public static boolean notZero(Number value) {
        return value != null && value.doubleValue() != 0.0;
    }

    public static boolean isZero(Number value) {
        return value != null && value.doubleValue() == 0.0;
    }

    public static boolean negative(Number value) {
        return value != null && value.doubleValue() < 0;
    }

    public static boolean multipleOf(Number value, Number divisor) {
        if (value == null || divisor == null || divisor.doubleValue() == 0) {
            return false;
        }
        return value.doubleValue() % divisor.doubleValue() == 0;
    }

    public static boolean decimalScale(java.math.BigDecimal value, int scale) {
        return value != null && value.scale() == scale;
    }
}
