package com.chao.failfast.internal.check;

/**
 * 对象校验工具类
 * 提供一系列用于对象校验的静态方法，包括判断对象是否存在、是否为空、是否为指定类型等
 */
public final class ObjectChecks {

    // 私有构造方法，防止实例化工具类
    private ObjectChecks() {}

    /**
     * 检查对象是否存在（不为null）
     * @param obj 需要检查的对象
     * @return 如果对象不为null返回true，否则返回false
     */
    public static boolean exists(Object obj) {
        return obj != null;
    }

    /**
     * 检查对象是否为null
     * @param obj 需要检查的对象
     * @return 如果对象为null返回true，否则返回false
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * 检查对象是否是指定类型的实例
     * @param obj 需要检查的对象
     * @param type 目标类型
     * @return 如果对象不为null且是指定类型的实例返回true，否则返回false
     */
    public static boolean instanceOf(Object obj, Class<?> type) {
        return type != null && type.isInstance(obj);
    }

    /**
     * 检查对象是否不是指定类型的实例
     * @param obj 需要检查的对象
     * @param type 目标类型
     * @return 如果对象不为null且不是指定类型的实例返回true，否则返回false
     */
    public static boolean notInstanceOf(Object obj, Class<?> type) {
        return type != null && !type.isInstance(obj);
    }

    /**
     * 检查所有对象都不为null
     * @param objs 需要检查的对象可变参数
     * @return 如果所有对象都不为null返回true，如果参数本身为null或任一参数为null返回false
     */
    public static boolean allNotNull(Object... objs) {
        // 首先检查参数数组本身是否为null
        if (objs == null) {
            return false;
        }
        // 遍历检查每个元素是否为null
        for (Object obj : objs) {
            if (obj == null) {
                return false;
            }
        }
        return true;
    }
}
