package com.chao.failfast.model.enums;

import com.chao.failfast.internal.ResponseCode;

public enum UserCode implements ResponseCode {

    SUCCESS(200, "成功", "ok"),
    Not_Exist(40001, "用户不存在", "用户不存在"),
    Username_Blank(40002, "用户名不能为空", "用户名不能为空"),
    Age_Not_Positive(40003, "年龄不能为负数", "年龄不能为负数"),
    Age_Out_Of_Range(40004, "年龄超出范围", "年龄超出范围"),
    Under_Age(40007, "用户未成年", "用户未成年"),
    Phone_Not_Match(40005, "手机号格式不正确", "手机号格式不正确"),
    Email_Not_Match(40006, "邮箱格式不正确", "邮箱格式不正确");
    private final int code;
    private final String message;
    private final String description;

    UserCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
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
        return description;
    }
}
