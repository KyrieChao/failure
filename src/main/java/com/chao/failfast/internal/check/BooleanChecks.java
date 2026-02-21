package com.chao.failfast.internal.check;

/**
 * 布尔校验工具类
 */
public final class BooleanChecks {

    private BooleanChecks() {}

    public static boolean state(boolean condition) {
        return condition;
    }

    public static boolean isTrue(boolean cond) {
        return cond;
    }

    public static boolean isFalse(boolean cond) {
        return !cond;
    }
}
