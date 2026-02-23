package com.chao.failfast.internal.check;

/**
 * 布尔校验工具类
 * 提供一系列用于布尔值检查的静态方法
 */
public final class BooleanChecks {

    /**
     * 私有构造方法，防止实例化工具类
     */
    private BooleanChecks() {}

    /**
     * 检查状态条件
     * @param condition 要检查的布尔条件
     * @return 直接返回传入的条件值
     */
    public static boolean state(boolean condition) {
        return condition;
    }

    /**
     * 检查布尔值是否为true
     * @param cond 要检查的布尔值
     * @return 如果传入值为true则返回true，否则返回false
     */
    public static boolean isTrue(boolean cond) {
        return cond;
    }

    /**
     * 检查布尔值是否为false
     * @param cond 要检查的布尔值
     * @return 如果传入值为false则返回true，否则返回false
     */
    public static boolean isFalse(boolean cond) {
        return !cond;
    }
}
