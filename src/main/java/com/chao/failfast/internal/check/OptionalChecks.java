package com.chao.failfast.internal.check;

import java.util.Optional;

/**
 * Optional校验工具类
 * 提供对Optional对象的便捷校验方法
 */
public final class OptionalChecks {
    // 私有构造方法，防止实例化工具类
    private OptionalChecks() {
    }

    /**
     * 检查Optional对象是否存在值
     *
     * @param opt 要检查的Optional对象
     * @return 如果Optional对象不为null且包含值则返回true，否则返回false
     */
    public static boolean isPresent(Optional<?> opt) {
        return opt != null && opt.isPresent();
    }

    /**
     * 检查Optional对象是否为空
     *
     * @param opt 要检查的Optional对象
     * @return 如果Optional对象为null或不包含值则返回true，否则返回false
     */
    public static boolean isEmpty(Optional<?> opt) {
        return opt == null || !opt.isPresent();
    }
}
