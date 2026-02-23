package com.chao.failfast.internal.check;

import java.util.Objects;

/**
 * 对象同一性校验工具类
 * 提供用于检查对象同一性和相等性的静态方法
 */
public final class IdentityChecks {

    /**
     * 私有构造方法，防止实例化工具类
     */
    private IdentityChecks() {}

    /**
     * 检查两个对象是否为同一个对象（同一性）
     * @param obj1 第一个对象
     * @param obj2 第二个对象
     * @return 如果两个对象引用相同则返回true，否则返回false
     */
    public static boolean same(Object obj1, Object obj2) {
        return obj1 == obj2;
    }

    /**
     * 检查两个对象是否不是同一个对象（同一性）
     * @param obj1 第一个对象
     * @param obj2 第二个对象
     * @return 如果两个对象引用不同则返回true，否则返回false
     */
    public static boolean notSame(Object obj1, Object obj2) {
        return obj1 != obj2;
    }

    /**
     * 检查两个对象是否相等（相等性）
     * 使用Objects.equals方法，可以正确处理null值
     * @param obj1 第一个对象
     * @param obj2 第二个对象
     * @return 如果两个对象相等则返回true，否则返回false
     */
    public static boolean equals(Object obj1, Object obj2) {
        return Objects.equals(obj1, obj2);
    }

    /**
     * 检查两个对象是否不相等（相等性）
     * 使用Objects.equals方法，可以正确处理null值
     * @param obj1 第一个对象
     * @param obj2 第二个对象
     * @return 如果两个对象不相等则返回true，否则返回false
     */
    public static boolean notEquals(Object obj1, Object obj2) {
        return !Objects.equals(obj1, obj2);
    }
}
