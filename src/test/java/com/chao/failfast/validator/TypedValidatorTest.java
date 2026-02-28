package com.chao.failfast.validator;

import com.chao.failfast.annotation.FastValidator;
import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.core.ResponseCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TypedValidator 类型校验器测试")
class TypedValidatorTest {

    static class TestValidator extends TypedValidator {
        @Override
        protected void registerValidators() {
            register(String.class, (s, ctx) -> {
                if (s.isEmpty()) ctx.reportError(ResponseCode.of(400, "Empty string"));
            });
            register(Integer.class, (i, ctx) -> {
                if (i < 0) ctx.reportError(ResponseCode.of(400, "Negative integer"));
            });
        }
    }
    static class EmptyValidator extends TypedValidator {
    }

    @Test
    @DisplayName("应当根据类型分发校验")
    void shouldDispatchByType() {
        TestValidator validator = new TestValidator();
        FastValidator.ValidationContext ctx = new FastValidator.ValidationContext(false);

        validator.validate("test", ctx);
        assertThat(ctx.isValid()).isTrue();

        validator.validate("", ctx);
        assertThat(ctx.isValid()).isFalse();
        assertThat(ctx.getErrors()).hasSize(1);
        
        // Reset
        ctx = new FastValidator.ValidationContext(false);
        validator.validate(10, ctx);
        assertThat(ctx.isValid()).isTrue();
        
        validator.validate(-1, ctx);
        assertThat(ctx.isValid()).isFalse();
    }

    @Test
    @DisplayName("当对象为 null 时应报错")
    void shouldReportErrorWhenNull() {
        TestValidator validator = new TestValidator();
        FastValidator.ValidationContext ctx = new FastValidator.ValidationContext(false);

        validator.validate(null, ctx);
        assertThat(ctx.isValid()).isFalse();
        Business error = ctx.getErrors().get(0);
        assertThat(error.getResponseCode().getCode()).isEqualTo(50000);
        assertThat(error.getResponseCode().getMessage()).isEqualTo("校验对象不能为空");
    }

    @Test
    @DisplayName("当类型不支持时应报错")
    void shouldReportErrorWhenTypeNotSupported() {
        TestValidator validator = new TestValidator();
        FastValidator.ValidationContext ctx = new FastValidator.ValidationContext(false);

        validator.validate(10.5, ctx); // Double not registered
        assertThat(ctx.isValid()).isFalse();
        Business error = ctx.getErrors().get(0);
        assertThat(error.getResponseCode().getCode()).isEqualTo(40099);
        assertThat(error.getDetail()).contains("不支持的校验类型");
    }
    @Test
    @DisplayName("当未注册类型时，应当不进行校验")
    void shouldNotValidateWhenTypeNotRegistered() {
        EmptyValidator validator = new EmptyValidator();
        FastValidator.ValidationContext ctx = new FastValidator.ValidationContext(false);

        validator.validate("test", ctx);
        assertThat(ctx.isValid()).isFalse();
        assertThat(ctx.getErrors().get(0).getResponseCode().getCode()).isEqualTo(40099);
    }
}
