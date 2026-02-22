package com.chao.failfast.internal.check;

import java.util.Optional;

/**
 * Optional校验工具类
 */
public final class OptionalChecks {
    private OptionalChecks() {}

    public static boolean isPresent(Optional<?> opt) {
        return opt != null && opt.isPresent();
    }

    public static boolean isEmpty(Optional<?> opt) {
        return opt == null || !opt.isPresent();
    }
}
