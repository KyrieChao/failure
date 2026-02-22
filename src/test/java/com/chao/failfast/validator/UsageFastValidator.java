package com.chao.failfast.validator;

import com.chao.failfast.annotation.FastValidator;
import com.chao.failfast.internal.ResponseCode;
import com.chao.failfast.model.entity.Usage;

/**
 * 也可以单独给一个类添加校验逻辑
 */
public class UsageFastValidator implements FastValidator<Usage> {


    @Override
    public void validate(Usage dto, ValidationContext context) {
        if (dto == null) {
            context.reportError(ResponseCode.of(40000, "请求对象不能为空"));
            return;
        }
        if (!dto.getName().equals("root")) {
            context.reportError(ResponseCode.of(40001, "用户名必须为root"));
            context.stop();
        }
        if (dto.getAge() < 18) {
            context.reportError(ResponseCode.of(40002, "用户年龄必须大于18"));
            context.stop();
        }
    }

    @Override
    public Class<?> getSupportedType() {
        // 显式声明支持的类型（虽然默认是Object.class，但在复杂场景下有助于类型安全）
        return Usage.class;
    }
}
