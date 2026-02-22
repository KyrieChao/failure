package com.chao.failfast.aspect;

import com.chao.failfast.annotation.FastValidator;
import com.chao.failfast.annotation.Validate;
import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.MultiBusiness;
import com.chao.failfast.model.enums.TestResponseCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ValidationAspect 切面测试")
class ValidationAspectTest {

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private Validate validate;

    @InjectMocks
    private ValidationAspect validationAspect;

    @BeforeEach
    void setUp() {
    }

    // Mock Validator
    public static class TestValidator implements FastValidator<String> {
        @Override
        public void validate(String target, ValidationContext context) {
            if ("invalid".equals(target)) {
                context.reportError(TestResponseCode.PARAM_ERROR);
            }
        }

        @Override
        public Class<?> getSupportedType() {
            return String.class;
        }
    }

    public static class MultiErrorValidator implements FastValidator<String> {
        @Override
        public void validate(String target, ValidationContext context) {
            context.reportError(TestResponseCode.PARAM_ERROR);
            context.reportError(TestResponseCode.SYSTEM_ERROR);
        }

        @Override
        public Class<?> getSupportedType() {
            return String.class;
        }
    }

    @Nested
    @DisplayName("切面逻辑测试")
    class AspectLogicTest {

        @Test
        @DisplayName("当没有指定验证器时应直接放行")
        void shouldProceedWhenNoValidatorSpecified() throws Throwable {
            Class<? extends FastValidator>[] validators = (Class<? extends FastValidator>[]) new Class<?>[0];
            when(validate.value()).thenReturn(validators);
            when(validate.fast()).thenReturn(true);
            
            validationAspect.around(joinPoint, validate);
            
            verify(joinPoint).proceed();
        }

        @Test
        @DisplayName("当验证通过时应直接放行")
        void shouldProceedWhenValidationPasses() throws Throwable {
            Class<? extends FastValidator>[] validators = (Class<? extends FastValidator>[]) new Class<?>[]{TestValidator.class};
            when(validate.value()).thenReturn(validators);
            when(validate.fast()).thenReturn(true);
            when(joinPoint.getArgs()).thenReturn(new Object[]{"valid"});
            
            // Mock ApplicationContext to return validator instance
            when(applicationContext.getBeanNamesForType(TestValidator.class)).thenReturn(new String[]{"testValidator"});
            when(applicationContext.getBean(TestValidator.class)).thenReturn(new TestValidator());

            validationAspect.around(joinPoint, validate);

            verify(joinPoint).proceed();
        }

        @Test
        @DisplayName("当验证失败且fast=true时应抛出Business异常")
        void shouldThrowBusinessWhenValidationFailsAndFastIsTrue() throws Throwable {
            Class<? extends FastValidator>[] validators = (Class<? extends FastValidator>[]) new Class<?>[]{TestValidator.class};
            when(validate.value()).thenReturn(validators);
            when(validate.fast()).thenReturn(true);
            when(joinPoint.getArgs()).thenReturn(new Object[]{"invalid"});

            when(applicationContext.getBeanNamesForType(TestValidator.class)).thenReturn(new String[]{});
            // Fallback to instantiation

            Throwable thrown = catchThrowable(() -> validationAspect.around(joinPoint, validate));

            assertThat(thrown).isInstanceOf(Business.class);
            Business business = (Business) thrown;
            assertThat(business.getResponseCode().getCode()).isEqualTo(TestResponseCode.PARAM_ERROR.getCode());
            verify(joinPoint, never()).proceed();
        }

        @Test
        @DisplayName("当产生多个错误时应抛出MultiBusiness异常")
        void shouldThrowMultiBusinessWithMultipleErrors() throws Throwable {
            Class<? extends FastValidator>[] validators = (Class<? extends FastValidator>[]) new Class<?>[]{MultiErrorValidator.class};
            when(validate.value()).thenReturn(validators);
            when(validate.fast()).thenReturn(false); // Must be false to collect multiple errors
            when(joinPoint.getArgs()).thenReturn(new Object[]{"any"});

            when(applicationContext.getBeanNamesForType(MultiErrorValidator.class)).thenReturn(new String[]{});

            Throwable thrown = catchThrowable(() -> validationAspect.around(joinPoint, validate));

            assertThat(thrown).isInstanceOf(MultiBusiness.class);
            assertThat(((MultiBusiness) thrown).getErrors()).hasSize(2);
        }
    }
}
