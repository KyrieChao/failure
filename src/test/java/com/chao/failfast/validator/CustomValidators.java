package com.chao.failfast.validator;

import com.chao.failfast.internal.ResponseCode;
import com.chao.failfast.model.entity.User;
import com.chao.failfast.model.entity.User2;
import com.chao.failfast.model.enums.UserCode;
import org.springframework.stereotype.Component;

@Component
public class CustomValidators extends TypedValidator {
    @Override
    protected void registerValidators() {

        // ====================== User 校验 ======================
        register(User.class, (user, ctx) -> {
            // 用户名校验
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                ctx.reportError(ResponseCode.of(40004, "用户名不能为空"));
            }
            if ("root".equals(user.getUsername())) {
                ctx.reportError(ResponseCode.of(40004, "用户名不能为 root"));
            }
            // 年龄校验
            if (user.getAge() != null && user.getAge() < 18) {
                ctx.reportError(UserCode.Under_Age);
            }

            // 邮箱校验
            if (user.getEmail() != null && !user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                ctx.reportError(ResponseCode.of(40005, "邮箱格式不正确"));
            }
        });

        // ====================== User2 校验 (Added manually to fix tests) ======================
        register(User2.class, (user, ctx) -> {
            // 用户名校验
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                ctx.reportError(ResponseCode.of(40004, "用户名不能为空"));
            }
            // 年龄校验
            if (user.getAge() != null && user.getAge() < 18) {
                ctx.reportError(UserCode.Under_Age);
            }
        });
    }

}
