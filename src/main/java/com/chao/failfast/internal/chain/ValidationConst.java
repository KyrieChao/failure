package com.chao.failfast.internal.chain;

import com.chao.failfast.internal.core.ResponseCode;
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
    /**
     * 默认验证失败响应码
     */
    public static final ResponseCode DEFAULT_VALIDATION_CODE = ResponseCode.of(
            500, "Validation failed", "链式验证缺少具体错误配置"
    );

    private ValidationConst() {
    }
}