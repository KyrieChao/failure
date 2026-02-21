package com.chao.failfast.internal.check;

import java.util.Objects;

/**
 * 对象同一性校验工具类
 */
public final class IdentityChecks {

    private IdentityChecks() {}

    public static boolean same(Object obj1, Object obj2) {
        return obj1 == obj2;
    }

    public static boolean notSame(Object obj1, Object obj2) {
        return obj1 != obj2;
    }

    public static boolean equals(Object obj1, Object obj2) {
        return Objects.equals(obj1, obj2);
    }

    public static boolean notEquals(Object obj1, Object obj2) {
        return !Objects.equals(obj1, obj2);
    }
}
