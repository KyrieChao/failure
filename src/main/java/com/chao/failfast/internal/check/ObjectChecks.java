package com.chao.failfast.internal.check;

/**
 * 对象校验工具类
 */
public final class ObjectChecks {

    private ObjectChecks() {}

    public static boolean exists(Object obj) {
        return obj != null;
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }
}
