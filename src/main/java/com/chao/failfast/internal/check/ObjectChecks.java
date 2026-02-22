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

    public static boolean instanceOf(Object obj, Class<?> type) {
        return type != null && type.isInstance(obj);
    }

    public static boolean notInstanceOf(Object obj, Class<?> type) {
        return type != null && !type.isInstance(obj);
    }

    public static boolean allNotNull(Object... objs) {
        if (objs == null) {
            return false;
        }
        for (Object obj : objs) {
            if (obj == null) {
                return false;
            }
        }
        return true;
    }
}
