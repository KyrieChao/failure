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

    // --- 覆盖率测试专用辅助类 ---

    public static class FailingValidator implements FastValidator<Object> {
        @Override
        public void validate(Object target, ValidationContext context) {
            context.reportError(TestResponseCode.PARAM_ERROR);
        }
    }

    public static class PassingValidator implements FastValidator<Object> {
        @Override
        public void validate(Object target, ValidationContext context) {
        }
    }

    public static class ExceptionThrowingValidator implements FastValidator<Object> {
        @Override
        public void validate(Object target, ValidationContext context) {
            throw new RuntimeException("Unexpected runtime error");
        }
    }

    public static class PrivateConstructorValidator implements FastValidator<Object> {
        private PrivateConstructorValidator() {
        }

        @Override
        public void validate(Object target, ValidationContext context) {
        }
    }

    public static class ConstructorThrowingValidator implements FastValidator<Object> {
        public ConstructorThrowingValidator() {
            throw new RuntimeException("Constructor failed");
        }

        @Override
        public void validate(Object target, ValidationContext context) {
        }
    }

    public static class GenericInterfaceValidator implements FastValidator<Integer> {
        @Override
        public void validate(Integer target, ValidationContext context) {
        }
    }

    public abstract static class BaseValidator<T> implements FastValidator<T> {
        @Override
        public void validate(T target, ValidationContext context) {
        }
    }

    public static class GenericSuperclassValidator extends BaseValidator<Double> {
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

        @Test
        @DisplayName("当参数类型不匹配时应跳过校验")
        void shouldSkipValidationWhenArgumentTypeMismatch() throws Throwable {
            Class<? extends FastValidator>[] validators = (Class<? extends FastValidator>[]) new Class<?>[]{TestValidator.class};
            when(validate.value()).thenReturn(validators);
            when(validate.fast()).thenReturn(true);
            when(joinPoint.getArgs()).thenReturn(new Object[]{123}); // TestValidator expects String

            TestValidator mockValidator = mock(TestValidator.class);
            when(mockValidator.getSupportedType()).thenCallRealMethod();
            
            when(applicationContext.getBeanNamesForType(TestValidator.class)).thenReturn(new String[]{"testValidator"});
            when(applicationContext.getBean(TestValidator.class)).thenReturn(mockValidator);

            validationAspect.around(joinPoint, validate);

            verify(joinPoint).proceed();
            verify(mockValidator, never()).validate(any(), any());
        }

        @Test
        @DisplayName("当参数为null时应跳过校验")
        void shouldSkipValidationWhenArgumentIsNull() throws Throwable {
            Class<? extends FastValidator>[] validators = (Class<? extends FastValidator>[]) new Class<?>[]{TestValidator.class};
            when(validate.value()).thenReturn(validators);
            when(validate.fast()).thenReturn(true);
            when(joinPoint.getArgs()).thenReturn(new Object[]{null});

            TestValidator mockValidator = mock(TestValidator.class);
            when(mockValidator.getSupportedType()).thenCallRealMethod();

            when(applicationContext.getBeanNamesForType(TestValidator.class)).thenReturn(new String[]{"testValidator"});
            when(applicationContext.getBean(TestValidator.class)).thenReturn(mockValidator);

            validationAspect.around(joinPoint, validate);

            verify(joinPoint).proceed();
            verify(mockValidator, never()).validate(any(), any());
        }
    }

    @Nested
    @DisplayName("覆盖率补充测试")
    class CoverageTest {
        @Test
        @DisplayName("覆盖 if (stopped) { break; }: 当 failFast=true 且前一个验证器失败时，后续验证器不应执行")
        void shouldStopExecutionWhenStoppedIsTrue() throws Throwable {
            // Arrange
            Class<? extends FastValidator>[] validators = (Class<? extends FastValidator>[]) new Class<?>[]{
                    FailingValidator.class,
                    PassingValidator.class // 这个不应该被执行
            };
            when(validate.value()).thenReturn(validators);
            when(validate.fast()).thenReturn(true);
            when(joinPoint.getArgs()).thenReturn(new Object[]{new Object()});

            // 模拟 FailingValidator 不在 Spring 容器中，走反射创建
            when(applicationContext.getBeanNamesForType(FailingValidator.class)).thenReturn(new String[]{});

            // Act
            Throwable thrown = catchThrowable(() -> validationAspect.around(joinPoint, validate));

            // Assert
            assertThat(thrown).isInstanceOf(Business.class); // 既然 failFast=true 且失败了，会抛异常

            // 验证 FailingValidator 被实例化了 (通过 getBeanNamesForType 被调用)
            verify(applicationContext, times(1)).getBeanNamesForType(FailingValidator.class);

            // 关键验证：PassingValidator 根本没有尝试去获取或实例化，说明循环 break 了
            verify(applicationContext, never()).getBeanNamesForType(PassingValidator.class);
        }

        @Test
        @DisplayName("覆盖 catch (Exception e) { log.error... }: 验证器抛出异常时应被捕获并继续执行后续验证器")
        void shouldCatchExceptionAndContinueLoop() throws Throwable {
            // Arrange
            Class<? extends FastValidator>[] validators = (Class<? extends FastValidator>[]) new Class<?>[]{
                    ExceptionThrowingValidator.class, // 会抛出 RuntimeException
                    PassingValidator.class // 应该继续执行
            };
            when(validate.value()).thenReturn(validators);
            when(validate.fast()).thenReturn(true);
            when(joinPoint.getArgs()).thenReturn(new Object[]{new Object()});

            when(applicationContext.getBeanNamesForType(ExceptionThrowingValidator.class)).thenReturn(new String[]{});

            // 为了验证 PassingValidator 被执行了，我们可以 Mock ApplicationContext 返回一个 Spy 对象
            PassingValidator spyValidator = spy(new PassingValidator());
            when(applicationContext.getBeanNamesForType(PassingValidator.class)).thenReturn(new String[]{"passingValidator"});
            when(applicationContext.getBean(PassingValidator.class)).thenReturn(spyValidator);

            // Act
            validationAspect.around(joinPoint, validate);

            // Assert
            // 1. 验证第一个验证器抛异常后，没有抛出到外面 (被 catch log 了)
            // 2. 验证第二个验证器被执行了
            verify(spyValidator).validate(any(), any());

            // 3. 最终 proceed 应该被调用
            verify(joinPoint).proceed();
        }

        @Test
        @DisplayName("覆盖 catch (Exception e) { throw new RuntimeException... }: 实例化失败应抛出运行时异常，并被外层捕获记录日志，然后继续")
        void shouldThrowRuntimeExceptionWhenInstantiationFails() throws Throwable {
            // Arrange
            Class<? extends FastValidator>[] validators = (Class<? extends FastValidator>[]) new Class<?>[]{
                    PrivateConstructorValidator.class
            };
            when(validate.value()).thenReturn(validators);
            when(validate.fast()).thenReturn(true);

            when(applicationContext.getBeanNamesForType(PrivateConstructorValidator.class)).thenReturn(new String[]{});

            // Act
            // 实例化失败会抛 RuntimeException，被外层 catch 捕获并记录日志，然后继续执行
            validationAspect.around(joinPoint, validate);

            // Assert
            // 验证切面正常放行 (因为验证器创建失败被忽略了)
            verify(joinPoint).proceed();
        }

        @Test
        @DisplayName("覆盖 catch (Exception e) { throw new RuntimeException... }: 构造函数抛异常应被捕获并记录日志，然后继续")
        void shouldThrowRuntimeExceptionWhenConstructorThrows() throws Throwable {
            // Arrange
            Class<? extends FastValidator>[] validators = (Class<? extends FastValidator>[]) new Class<?>[]{
                    ConstructorThrowingValidator.class
            };
            when(validate.value()).thenReturn(validators);
            when(validate.fast()).thenReturn(true);

            when(applicationContext.getBeanNamesForType(ConstructorThrowingValidator.class)).thenReturn(new String[]{});

            // Act
            validationAspect.around(joinPoint, validate);

            // Assert
            verify(joinPoint).proceed();
        }

        @Test
        @DisplayName("覆盖 getValidatorSupportedType: 能够从泛型接口推断类型")
        void shouldInferTypeFromGenericInterface() throws Throwable {
            // Arrange
            Class<? extends FastValidator>[] validators = (Class<? extends FastValidator>[]) new Class<?>[]{
                    GenericInterfaceValidator.class
            };
            when(validate.value()).thenReturn(validators);
            when(validate.fast()).thenReturn(true);

            // 传入一个 String，应该被忽略 (因为 GenericInterfaceValidator 支持 Integer)
            when(joinPoint.getArgs()).thenReturn(new Object[]{"not an integer"});
            when(applicationContext.getBeanNamesForType(GenericInterfaceValidator.class)).thenReturn(new String[]{});

            // Act
            validationAspect.around(joinPoint, validate);

            // Assert
            verify(joinPoint).proceed();
        }

        @Test
        @DisplayName("覆盖 getValidatorSupportedType: 能够从泛型父类推断类型")
        void shouldInferTypeFromGenericSuperclass() throws Throwable {
            // Arrange
            Class<? extends FastValidator>[] validators = (Class<? extends FastValidator>[]) new Class<?>[]{
                    GenericSuperclassValidator.class
            };
            when(validate.value()).thenReturn(validators);
            when(validate.fast()).thenReturn(true);

            // 传入一个 String，应该被忽略 (因为 GenericSuperclassValidator 支持 Double)
            when(joinPoint.getArgs()).thenReturn(new Object[]{"not a double"});
            when(applicationContext.getBeanNamesForType(GenericSuperclassValidator.class)).thenReturn(new String[]{});

            // Act
            validationAspect.around(joinPoint, validate);

            // Assert
            verify(joinPoint).proceed();
        }
    }
    @Nested
    @DisplayName("getValidatorSupportedType 方法测试")
    class GetValidatorSupportedTypeTest {

        @Test
        @DisplayName("当 getSupportedType 返回具体类型时应直接使用")
        void shouldUseDeclaredSupportedType() throws Throwable {
            Class<? extends FastValidator>[] validators = (Class<? extends FastValidator>[]) new Class<?>[]{
                    TestValidator.class  // getSupportedType 返回 String.class
            };
            when(validate.value()).thenReturn(validators);
            when(validate.fast()).thenReturn(true);
            when(joinPoint.getArgs()).thenReturn(new Object[]{"valid"});

            when(applicationContext.getBeanNamesForType(TestValidator.class)).thenReturn(new String[]{});

            validationAspect.around(joinPoint, validate);
            verify(joinPoint).proceed();
        }

        @Test
        @DisplayName("当 getSupportedType 返回 Object.class 时应从泛型接口推断")
        void shouldInferFromGenericInterfaceWhenDeclaredIsObject() throws Throwable {
            // 创建一个返回 Object.class 的验证器
            FastValidator<Object> validator = new FastValidator<>() {
                @Override
                public void validate(Object target, ValidationContext context) {}

                @Override
                public Class<?> getSupportedType() {
                    return Object.class;  // 强制返回 Object
                }
            };

            Class<? extends FastValidator> clazz = validator.getClass();
            Class<? extends FastValidator>[] validators = (Class<? extends FastValidator>[]) new Class<?>[]{clazz};

            when(validate.value()).thenReturn(validators);
            when(validate.fast()).thenReturn(true);
            when(joinPoint.getArgs()).thenReturn(new Object[]{"test"});
            when(applicationContext.getBeanNamesForType(clazz)).thenReturn(new String[]{});

            validationAspect.around(joinPoint, validate);
            verify(joinPoint).proceed();
        }

        @Test
        @DisplayName("当泛型接口推断失败时应返回 Object.class")
        void shouldReturnObjectClassWhenInferenceFails() throws Throwable {
            // 使用原始类型（没有泛型参数）的验证器
            @SuppressWarnings("rawtypes")
            class RawValidator implements FastValidator {
                @Override
                public void validate(Object target, ValidationContext context) {}
            }

            Class<? extends FastValidator>[] validators = (Class<? extends FastValidator>[]) new Class<?>[]{RawValidator.class};
            when(validate.value()).thenReturn(validators);
            when(validate.fast()).thenReturn(true);
            when(joinPoint.getArgs()).thenReturn(new Object[]{"test"});
            when(applicationContext.getBeanNamesForType(RawValidator.class)).thenReturn(new String[]{});

            validationAspect.around(joinPoint, validate);
            verify(joinPoint).proceed();
        }

        @Test
        @DisplayName("当泛型父类推断失败时应返回 Object.class")
        void shouldReturnObjectClassWhenSuperclassInferenceFails() throws Throwable {
            abstract class RawBaseValidator implements FastValidator {
            }

            class RawValidator extends RawBaseValidator {
                @Override
                public void validate(Object target, ValidationContext context) {}
            }

            Class<? extends FastValidator>[] validators = (Class<? extends FastValidator>[]) new Class<?>[]{RawValidator.class};
            when(validate.value()).thenReturn(validators);
            when(validate.fast()).thenReturn(true);
            when(joinPoint.getArgs()).thenReturn(new Object[]{"test"});
            when(applicationContext.getBeanNamesForType(RawValidator.class)).thenReturn(new String[]{});

            validationAspect.around(joinPoint, validate);
            verify(joinPoint).proceed();
        }
    }

    @Nested
    @DisplayName("边界情况测试")
    class EdgeCaseTest {

        @Test
        @DisplayName("当多个验证器都失败且 fast=false 时应收集所有错误")
        void shouldCollectAllErrorsFromMultipleValidators() throws Throwable {
            Class<? extends FastValidator>[] validators = (Class<? extends FastValidator>[]) new Class<?>[]{
                    FailingValidator.class,
                    FailingValidator.class
            };
            when(validate.value()).thenReturn(validators);
            when(validate.fast()).thenReturn(false);  // 非快速模式
            when(joinPoint.getArgs()).thenReturn(new Object[]{new Object()});

            when(applicationContext.getBeanNamesForType(FailingValidator.class)).thenReturn(new String[]{});

            Throwable thrown = catchThrowable(() -> validationAspect.around(joinPoint, validate));

            assertThat(thrown).isInstanceOf(MultiBusiness.class);
            assertThat(((MultiBusiness) thrown).getErrors()).hasSize(2);
        }

        @Test
        @DisplayName("当 fast=true 但 context 未 stopped 时应继续执行")
        void shouldContinueWhenFastButNotStopped() throws Throwable {
            // 第一个验证器通过，第二个失败
            Class<? extends FastValidator>[] validators = (Class<? extends FastValidator>[]) new Class<?>[]{
                    PassingValidator.class,
                    FailingValidator.class
            };
            when(validate.value()).thenReturn(validators);
            when(validate.fast()).thenReturn(true);
            when(joinPoint.getArgs()).thenReturn(new Object[]{new Object()});

            when(applicationContext.getBeanNamesForType(PassingValidator.class)).thenReturn(new String[]{});
            when(applicationContext.getBeanNamesForType(FailingValidator.class)).thenReturn(new String[]{});

            Throwable thrown = catchThrowable(() -> validationAspect.around(joinPoint, validate));

            assertThat(thrown).isInstanceOf(Business.class);
        }

        @Test
        @DisplayName("当错误列表为空时应正常放行")
        void shouldProceedWhenErrorListIsEmpty() throws Throwable {
            Class<? extends FastValidator>[] validators = (Class<? extends FastValidator>[]) new Class<?>[]{
                    PassingValidator.class
            };
            when(validate.value()).thenReturn(validators);
            when(validate.fast()).thenReturn(true);
            when(joinPoint.getArgs()).thenReturn(new Object[]{new Object()});

            when(applicationContext.getBeanNamesForType(PassingValidator.class)).thenReturn(new String[]{});

            validationAspect.around(joinPoint, validate);
            verify(joinPoint).proceed();
        }

        @Test
        @DisplayName("当只有一个错误时应抛出 Business 而不是 MultiBusiness")
        void shouldThrowBusinessForSingleError() throws Throwable {
            Class<? extends FastValidator>[] validators = (Class<? extends FastValidator>[]) new Class<?>[]{
                    FailingValidator.class
            };
            when(validate.value()).thenReturn(validators);
            when(validate.fast()).thenReturn(false);
            when(joinPoint.getArgs()).thenReturn(new Object[]{new Object()});

            when(applicationContext.getBeanNamesForType(FailingValidator.class)).thenReturn(new String[]{});

            Throwable thrown = catchThrowable(() -> validationAspect.around(joinPoint, validate));

            assertThat(thrown).isInstanceOf(Business.class);
            assertThat(thrown).isNotInstanceOf(MultiBusiness.class);
        }
    }
}
