package com.chao.failfast.validator;

import com.chao.failfast.annotation.FastValidator;
import com.chao.failfast.internal.ResponseCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FastValidatorTest 类型校验器测试")
class FastValidatorTest {
    static class FastValidatorDemo implements FastValidator<String> {

        @Override
        public void validate(String s, ValidationContext context) {
            if (s == null) {
                context.reportError(ResponseCode.of(40000, "请求对象不能为空"));
                return;
            }
            if (s.isEmpty()) {
                context.reportError(ResponseCode.of(40001, "请求对象不能为空"), "s不能为空");
            }
            if (s.length() > 10) {
                context.reportError(ResponseCode.of(40002, "请求对象长度不能超过10"), "s长度不能超过10");
            }
        }

        @Override
        public Class<?> getSupportedType() {
            return String.class;
        }
    }

    @Test
    @DisplayName("通过校验")
    void shouldPassValidation() {
        FastValidatorDemo demo = new FastValidatorDemo();
        FastValidator.ValidationContext ctx = new FastValidator.ValidationContext(false);

        demo.validate("demo", ctx);
        assertThat(ctx.isValid()).isTrue();
    }

    @Test
    @DisplayName("当对象为 null 时应报错")
    void shouldReportErrorWhenObjectIsNull() {
        FastValidatorDemo demo = new FastValidatorDemo();
        FastValidator.ValidationContext ctx = new FastValidator.ValidationContext(false);

        demo.validate(null, ctx);
        assertThat(ctx.isValid()).isFalse();
        assertThat(ctx.getErrors()).hasSize(1);
    }

    @Test
    @DisplayName("当对象长度 大于10 时应报错")
    void shouldReportErrorWhenObjectLengthGreaterThan10() {
        FastValidatorDemo demo = new FastValidatorDemo();
        FastValidator.ValidationContext ctx = new FastValidator.ValidationContext(false);

        demo.validate("demo1234567890", ctx);
        assertThat(ctx.isValid()).isFalse();
        assertThat(ctx.getErrors()).hasSize(1);
    }

    @Test
    @DisplayName("当对象值为空 时应报错")
    void shouldReportErrorWhenObjectIsEmpty() {
        FastValidatorDemo demo = new FastValidatorDemo();
        FastValidator.ValidationContext ctx = new FastValidator.ValidationContext(false);

        demo.validate("", ctx);
        assertThat(ctx.isValid()).isFalse();
        assertThat(ctx.getErrors()).hasSize(1);
    }

    @Test
    @DisplayName("都不符合校验 用getFirstError")
    void shouldReportErrorWhenObjectIsEmptyAndLengthGreaterThan10() {
        FastValidatorDemo demo = new FastValidatorDemo();
        FastValidator.ValidationContext ctx = new FastValidator.ValidationContext(false);
        demo.validate(null, ctx);
        demo.validate("", ctx);
        demo.validate("demo1234567890", ctx);
        assertThat(ctx.getFirstError().getResponseCode().getCode()).isEqualTo(40000);
    }
}
