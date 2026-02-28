package com.chao.failfast.internal.chain;

import com.chao.failfast.internal.core.ViolationSpec;

import java.util.function.Consumer;

/**
 * 验证框架常量
 */
final class ValidationConst {

    /**
     * 无操作配置（用于默认方法）
     */
    public static final Consumer<ViolationSpec> NO_OP = s -> {
    };

    private ValidationConst() {
    }
}