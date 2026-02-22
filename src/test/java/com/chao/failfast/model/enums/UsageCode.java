package com.chao.failfast.model.enums;

import com.chao.failfast.internal.ResponseCode;

public enum UsageCode implements ResponseCode {
    SUCCESS(200, "成功"),
    ERROR(50000, "通用错误"),
    ROOT_CAUSE(50001, "根本原因"),
    PARAM_ERROR(40001, "参数错误"),
    SYSTEM_ERROR(50002, "系统错误"),
    PARAM_REQUIRED(40002, "参数缺失");

    private final int code;
    private final String message;

    UsageCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getDescription() {
        return message;
    }
}
