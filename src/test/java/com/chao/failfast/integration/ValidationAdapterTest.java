package com.chao.failfast.integration;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.MultiBusiness;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;

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

    @Nested
    @DisplayName("各种注解类型映射测试")
    class AnnotationMappingTest {

        @Data
        static class AllAnnotationsForm {
            @NotNull(message = "不能为null")
            private String notNullField;

            @NotEmpty(message = "不能为空")
            private String notEmptyField;

            @NotBlank(message = "不能为blank")
            private String notBlankField;

            @Size(min = 2, max = 10, message = "长度不合法")
            private String sizeField;

            @Length(min = 2, max = 10, message = "长度不合法")
            private String lengthField;

            @Min(value = 10, message = "数值太小")
            private Integer minField;

            @Max(value = 100, message = "数值太大")
            private Integer maxField;

            @DecimalMin(value = "10.0", message = "数值太小")
            private Double decimalMinField;

            @DecimalMax(value = "100.0", message = "数值太大")
            private Double decimalMaxField;

            @Email(message = "邮箱格式错误")
            private String emailField;

            @Pattern(regexp = "\\d+", message = "格式错误")
            private String patternField;

            @Positive(message = "必须为正数")
            private Integer positiveField;

            @PositiveOrZero(message = "必须为正数或零")
            private Integer positiveOrZeroField;

            @Negative(message = "必须为负数")
            private Integer negativeField;

            @NegativeOrZero(message = "必须为负数或零")
            private Integer negativeOrZeroField;

            @Future(message = "必须是未来时间")
            private java.time.LocalDateTime futureField;

            @FutureOrPresent(message = "必须是未来或现在")
            private java.time.LocalDateTime futureOrPresentField;

            @Past(message = "必须是过去时间")
            private java.time.LocalDateTime pastField;

            @PastOrPresent(message = "必须是过去或现在")
            private java.time.LocalDateTime pastOrPresentField;

            @AssertTrue(message = "必须为true")
            private Boolean assertTrueField;

            @AssertFalse(message = "必须为false")
            private Boolean assertFalseField;

            // 未知注解测试
            @Digits(integer = 3, fraction = 2, message = "数字格式错误")
            private Double digitsField;
        }

        @Test
        @DisplayName("NotNull 注解应映射到 40001")
        void shouldMapNotNullTo40001() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setNotNullField(null);

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40001);
        }

        @Test
        @DisplayName("NotEmpty 注解应映射到 40001")
        void shouldMapNotEmptyTo40001() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setNotEmptyField("");

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40001);
        }

        @Test
        @DisplayName("NotBlank 注解应映射到 40001")
        void shouldMapNotBlankTo40001() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setNotBlankField("   ");

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40001);
        }

        @Test
        @DisplayName("Size 注解应映射到 40002")
        void shouldMapSizeTo40002() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setSizeField("a");

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40002);
        }

        @Test
        @DisplayName("Length 注解应映射到 40002")
        void shouldMapLengthTo40002() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setLengthField("a");

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40002);
        }

        @Test
        @DisplayName("Min 注解应映射到 40003")
        void shouldMapMinTo40003() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setMinField(5);

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40003);
        }

        @Test
        @DisplayName("Max 注解应映射到 40003")
        void shouldMapMaxTo40003() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setMaxField(200);

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40003);
        }

        @Test
        @DisplayName("DecimalMin 注解应映射到 40003")
        void shouldMapDecimalMinTo40003() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setDecimalMinField(5.0);

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40003);
        }

        @Test
        @DisplayName("DecimalMax 注解应映射到 40003")
        void shouldMapDecimalMaxTo40003() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setDecimalMaxField(200.0);

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40003);
        }

        @Test
        @DisplayName("Email 注解应映射到 40004")
        void shouldMapEmailTo40004() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setEmailField("invalid");

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40004);
        }

        @Test
        @DisplayName("Pattern 注解应映射到 40005")
        void shouldMapPatternTo40005() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setPatternField("abc");

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40005);
        }

        @Test
        @DisplayName("Positive 注解应映射到 40006")
        void shouldMapPositiveTo40006() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setPositiveField(-1);

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40006);
        }

        @Test
        @DisplayName("PositiveOrZero 注解应映射到 40006")
        void shouldMapPositiveOrZeroTo40006() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setPositiveOrZeroField(-1);

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40006);
        }

        @Test
        @DisplayName("Negative 注解应映射到 40007")
        void shouldMapNegativeTo40007() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setNegativeField(1);

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40007);
        }

        @Test
        @DisplayName("NegativeOrZero 注解应映射到 40007")
        void shouldMapNegativeOrZeroTo40007() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setNegativeOrZeroField(1);

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40007);
        }

        @Test
        @DisplayName("Future 注解应映射到 40008")
        void shouldMapFutureTo40008() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setFutureField(java.time.LocalDateTime.now().minusDays(1));

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40008);
        }

        @Test
        @DisplayName("FutureOrPresent 注解应映射到 40008")
        void shouldMapFutureOrPresentTo40008() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setFutureOrPresentField(java.time.LocalDateTime.now().minusDays(1));

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40008);
        }

        @Test
        @DisplayName("Past 注解应映射到 40009")
        void shouldMapPastTo40009() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setPastField(java.time.LocalDateTime.now().plusDays(1));

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40009);
        }

        @Test
        @DisplayName("PastOrPresent 注解应映射到 40009")
        void shouldMapPastOrPresentTo40009() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setPastOrPresentField(java.time.LocalDateTime.now().plusDays(1));

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40009);
        }

        @Test
        @DisplayName("AssertTrue 注解应映射到 40010")
        void shouldMapAssertTrueTo40010() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setAssertTrueField(false);

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40010);
        }

        @Test
        @DisplayName("AssertFalse 注解应映射到 40010")
        void shouldMapAssertFalseTo40010() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setAssertFalseField(true);

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40010);
        }

        @Test
        @DisplayName("未知注解应映射到默认错误码 40000")
        void shouldMapUnknownAnnotationTo40000() {
            AllAnnotationsForm form = new AllAnnotationsForm();
            form.setDigitsField(1234.56789); // 超出 digits 限制

            List<Business> errors = validationAdapter.validateToList(form);
            assertThat(errors).anyMatch(e -> e.getResponseCode().getCode() == 40000);
        }
    }

    @Nested
    @DisplayName("边界情况测试")
    class EdgeCaseTest {

        @Test
        @DisplayName("当对象为 null 时应抛出 Business 异常")
        void shouldThrowBusinessWhenObjectIsNull() {
            Business exception = catchThrowableOfType(
                    () -> validationAdapter.validate(null),
                    Business.class
            );

            assertThat(exception.getResponseCode().getCode()).isEqualTo(500);
            assertThat(exception.getMessage()).contains("参数校验失败");
        }

        @Test
        @DisplayName("validateAll 当没有错误时不应抛出异常")
        void validateAllShouldNotThrowWhenNoErrors() {
            UserForm form = new UserForm("user", "user@example.com", 18);
            // 不应抛出异常
            validationAdapter.validateAll(form);
        }

        @Test
        @DisplayName("validate 当验证通过时不应抛出异常")
        void validateShouldNotThrowWhenValid() {
            UserForm form = new UserForm("user", "user@example.com", 18);
            // 不应抛出异常
            validationAdapter.validate(form);
        }

        @Test
        @DisplayName("错误消息应包含字段名和实际值")
        void errorMessageShouldContainFieldNameAndValue() {
            UserForm form = new UserForm("user", "user@example.com", 17);

            List<Business> errors = validationAdapter.validateToList(form);

            assertThat(errors).isNotEmpty();
            Business error = errors.get(0);
            // 验证 detail 包含字段信息
            assertThat(error.getDetail()).contains("当前:");
            assertThat(error.getDetail()).contains("17");
        }

        @Test
        @DisplayName("当 invalidValue 为 null 时应显示 null")
        void shouldShowNullForInvalidValue() {
            @Data
            class NullValueForm {
                @NotNull(message = "不能为null")
                private String field;
            }

            NullValueForm form = new NullValueForm();
            form.setField(null);

            List<Business> errors = validationAdapter.validateToList(form);

            assertThat(errors).isNotEmpty();
            assertThat(errors.get(0).getDetail()).contains("null");
        }
    }
}
