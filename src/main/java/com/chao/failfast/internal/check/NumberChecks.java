package com.chao.failfast.internal.check;

/**
 * 数值校验工具类
 * 提供各种数值校验的静态方法，用于检查数值是否满足特定条件
 */
public final class NumberChecks {

    /**
     * 私有构造方法，防止实例化工具类
     */
    private NumberChecks() {
    }

    /**
     * 检查数值是否为正数
     * @param value 要检查的数值
     * @return 如果数值不为null且大于0则返回true，否则返回false
     */
    public static boolean positive(Number value) {
        return value != null && value.doubleValue() > 0;
    }

    /**
     * 检查数值是否在指定范围内（使用泛型和Comparable接口）
     * @param value 要检查的数值
     * @param min 最小值（包含）
     * @param max 最大值（包含）
     * @return 如果数值在[min, max]范围内则返回true，否则返回false
     */
    public static <T extends Number & Comparable<T>> boolean inRange(T value, T min, T max) {
        return value != null && min != null && max != null
                && value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
    }

    /**
     * 检查数值是否在指定范围内（使用Number类的doubleValue方法）
     * @param v 要检查的数值
     * @param min 最小值（包含）
     * @param max 最大值（包含）
     * @return 如果数值在[min, max]范围内则返回true，否则返回false
     */
    public static boolean inRangeNumber(Number v, Number min, Number max) {
        return v != null && min != null && max != null
                && v.doubleValue() >= min.doubleValue()
                && v.doubleValue() <= max.doubleValue();
    }

    /**
     * 检查数值是否为非负数
     * @param value 要检查的数值
     * @return 如果数值不为null且大于等于0则返回true，否则返回false
     */
    public static boolean nonNegative(Number value) {
        return value != null && value.doubleValue() >= 0;
    }

    /**
     * 检查数值是否大于指定阈值
     * @param value 要检查的数值
     * @param threshold 阈值
     * @return 如果数值大于阈值则返回true，否则返回false
     */
    public static <T extends Number & Comparable<T>> boolean greaterThan(T value, T threshold) {
        return value != null && threshold != null && value.compareTo(threshold) > 0;
    }

    /**
     * 检查数值是否大于等于指定阈值
     * @param value 要检查的数值
     * @param threshold 阈值
     * @return 如果数值大于等于阈值则返回true，否则返回false
     */
    public static <T extends Number & Comparable<T>> boolean greaterOrEqual(T value, T threshold) {
        return value != null && threshold != null && value.compareTo(threshold) >= 0;
    }

    /**
     * 检查数值是否小于指定阈值
     * @param value 要检查的数值
     * @param threshold 阈值
     * @return 如果数值小于阈值则返回true，否则返回false
     */
    public static <T extends Number & Comparable<T>> boolean lessThan(T value, T threshold) {
        return value != null && threshold != null && value.compareTo(threshold) < 0;
    }

    /**
     * 检查数值是否小于等于指定阈值
     * @param value 要检查的数值
     * @param threshold 阈值
     * @return 如果数值小于等于阈值则返回true，否则返回false
     */
    public static <T extends Number & Comparable<T>> boolean lessOrEqual(T value, T threshold) {
        return value != null && threshold != null && value.compareTo(threshold) <= 0;
    }

    /**
     * 检查数值是否不为零
     * @param value 要检查的数值
     * @return 如果数值不为null且不等于0则返回true，否则返回false
     */
    public static boolean notZero(Number value) {
        return value != null && value.doubleValue() != 0.0;
    }

    /**
     * 检查数值是否为零
     * @param value 要检查的数值
     * @return 如果数值不为null且等于0则返回true，否则返回false
     */
    public static boolean isZero(Number value) {
        return value != null && value.doubleValue() == 0.0;
    }

    /**
     * 检查数值是否为负数
     * @param value 要检查的数值
     * @return 如果数值不为null且小于0则返回true，否则返回false
     */
    public static boolean negative(Number value) {
        return value != null && value.doubleValue() < 0;
    }

    /**
     * 检查数值是否是另一个数的整数倍
     * @param value 要检查的数值
     * @param divisor 除数
     * @return 如果value是divisor的整数倍则返回true，否则返回false
     */
    public static boolean multipleOf(Number value, Number divisor) {
        if (value == null || divisor == null || divisor.doubleValue() == 0) {
            return false;
        }
        return value.doubleValue() % divisor.doubleValue() == 0;
    }

    /**
     * 检查BigDecimal数值的小数位数是否符合指定要求
     * @param value 要检查的BigDecimal数值
     * @param scale 期望的小数位数
     * @return 如果数值的小数位数等于指定scale则返回true，否则返回false
     */
    public static boolean decimalScale(java.math.BigDecimal value, int scale) {
        return value != null && value.scale() == scale;
    }
}
