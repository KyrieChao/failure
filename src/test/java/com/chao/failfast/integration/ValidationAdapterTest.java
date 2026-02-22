package com.chao.failfast.integration;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.MultiBusiness;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("ValidationAdapter 集成测试")
class ValidationAdapterTest {

    private ValidationAdapter validationAdapter;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            this.validationAdapter = new ValidationAdapter(validator);
        }
    }
    @Data
    static class UserForm {
        @NotBlank(message = "用户名不能为空")
        private String username;

        @Email(message = "邮箱格式不正确")
        private String email;

        @NotNull
        @Min(value = 18, message = "未成年人禁止注册")
        private Integer age;

        public UserForm(String username, String email, Integer age) {
            this.username = username;
            this.email = email;
            this.age = age;
        }
    }

    @Nested
    @DisplayName("Fail-Fast 模式测试")
    class FailFastTest {
        @Test
        @DisplayName("应当在第一个错误时立即抛出 Business 异常")
        void shouldThrowBusinessImmediately() {
            // Use only one error to ensure deterministic error code (40001 for NotBlank)
            UserForm form = new UserForm(null, "valid@example.com", 18);

            Throwable thrown = catchThrowable(() -> validationAdapter.validate(form));

            assertThat(thrown).isInstanceOf(Business.class);
            // 40001 is for NotBlank/NotNull etc.
            assertThat(((Business) thrown).getResponseCode().getCode()).isEqualTo(40001);
        }

        @Test
        @DisplayName("当验证通过时应当无异常")
        void shouldPassWhenValid() {
            UserForm form = new UserForm("user", "user@example.com", 18);
            validationAdapter.validate(form);
        }
    }

    @Nested
    @DisplayName("全量收集模式测试")
    class ValidateAllTest {
        @Test
        @DisplayName("应当收集所有错误并抛出 MultiBusiness 异常")
        void shouldThrowMultiBusinessWithAllErrors() {
            // 3 errors: username blank, email invalid, age < 18
            UserForm form = new UserForm("", "invalid", 17);

            Throwable thrown = catchThrowable(() -> validationAdapter.validateAll(form));

            assertThat(thrown).isInstanceOf(MultiBusiness.class);
            MultiBusiness mb = (MultiBusiness) thrown;
            assertThat(mb.getErrors()).hasSize(3);
        }

        @Test
        @DisplayName("当只有一个错误时应当抛出单个 Business 异常")
        void shouldThrowBusinessWhenSingleError() {
            UserForm form = new UserForm("user", "user@example.com", 17); // only age error

            Throwable thrown = catchThrowable(() -> validationAdapter.validateAll(form));

            assertThat(thrown).isInstanceOf(Business.class);
            assertThat(thrown).isNotInstanceOf(MultiBusiness.class);
        }
    }

    @Nested
    @DisplayName("validateToList 方法测试")
    class ValidateToListTest {
        @Test
        @DisplayName("应当返回错误列表而不抛出异常")
        void shouldReturnErrorList() {
            UserForm form = new UserForm("", "invalid", 17);

            List<Business> errors = validationAdapter.validateToList(form);

            assertThat(errors).hasSize(3);
        }

        @Test
        @DisplayName("当验证通过时应当返回空列表")
        void shouldReturnEmptyListWhenValid() {
            UserForm form = new UserForm("user", "user@example.com", 18);

            List<Business> errors = validationAdapter.validateToList(form);

            assertThat(errors).isEmpty();
        }
    }

    @Nested
    @DisplayName("isValid 方法测试")
    class IsValidTest {
        @Test
        @DisplayName("当验证通过时应当返回 true")
        void shouldReturnTrueWhenValid() {
            UserForm form = new UserForm("user", "user@example.com", 18);
            assertThat(validationAdapter.isValid(form)).isTrue();
        }

        @Test
        @DisplayName("当验证失败时应当返回 false")
        void shouldReturnFalseWhenInvalid() {
            UserForm form = new UserForm("", "user@example.com", 18);
            assertThat(validationAdapter.isValid(form)).isFalse();
        }
    }
}
