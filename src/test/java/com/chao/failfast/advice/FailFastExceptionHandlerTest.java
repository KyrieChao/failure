package com.chao.failfast.advice;

import com.chao.failfast.annotation.Validate;
import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.MultiBusiness;
import com.chao.failfast.internal.ResponseCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * FailFastExceptionHandler 单元测试
 */
@DisplayName("FailFastExceptionHandler 单元测试")
class FailFastExceptionHandlerTest {

    // 具体的测试子类
    static class TestFailFastExceptionHandler extends FailFastExceptionHandler {
        // 可以重写 protected 方法来暴露测试，或者直接测试 public 方法
    }

    private final TestFailFastExceptionHandler handler = new TestFailFastExceptionHandler();

    @Test
    @DisplayName("handleBusinessException: 处理单个业务异常")
    void handleBusinessException() {
        // Arrange
        Business business = Business.of(ResponseCode.of(40001, "Test Error"));
        
        // Act
        ResponseEntity<?> response = handler.handleBusinessException(business);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR); // Business 默认是 500，除非配置了 CodeMapping
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertThat(body).containsEntry("code", 40001);
        assertThat(body).containsEntry("message", "Test Error");
        assertThat(body).containsKey("timestamp");
    }

    @Test
    @DisplayName("handleMultiBusinessException: 处理多个业务异常")
    void handleMultiBusinessException() {
        // Arrange
        List<Business> errors = Arrays.asList(
                Business.of(ResponseCode.of(1001, "Error 1")),
                Business.of(ResponseCode.of(1002, "Error 2"))
        );
        MultiBusiness multiBusiness = new MultiBusiness(errors);

        // Act
        ResponseEntity<?> response = handler.handleMultiBusinessException(multiBusiness);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertThat(body).containsEntry("code", 400); // MultiBusiness 默认 code
        String description = (String) body.get("description");
        assertThat(description).contains("共 2 项错误");
        assertThat(description).contains("1.Error 1");
        assertThat(description).contains("2.Error 2");
    }

    @Test
    @DisplayName("handleMethodArgumentNotValidException: 默认快速失败 (Fail-Fast)，只返回第一个错误")
    void handleMethodArgumentNotValidException_FailFast_Default() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        List<FieldError> fieldErrors = Arrays.asList(
                new FieldError("object", "field1", "Error 1"),
                new FieldError("object", "field2", "Error 2")
        );
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
        when(bindingResult.getTarget()).thenReturn(new Object());

        MethodParameter parameter = mock(MethodParameter.class);
        // 模拟方法上没有 @Validate 注解
        when(parameter.getMethod()).thenReturn(null);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        // Act
        ResponseEntity<?> response = handler.handleMethodArgumentNotValidException(ex);

        // Assert
        // 在无配置环境下，单个 Business 异常默认计算为 500
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        // 因为是 fail-fast，只应该保留第一个错误
        assertThat(body).containsEntry("message", "Validation Error"); // Business 的 message 是 ResponseCode 的 message
        assertThat(body).containsEntry("description", "Error 1"); // Business 的 detail 是 parseError 传入的 message
        assertThat(body.get("description").toString()).doesNotContain("共 2 项错误");
    }

    @Test
    @DisplayName("handleMethodArgumentNotValidException: 关闭快速失败 (Fail-Safe)，返回所有错误")
    void handleMethodArgumentNotValidException_FailSafe() throws NoSuchMethodException {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        List<FieldError> fieldErrors = Arrays.asList(
                new FieldError("object", "field1", "Error 1"),
                new FieldError("object", "field2", "Error 2")
        );
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
        when(bindingResult.getTarget()).thenReturn(new Object());

        // 模拟方法上有 @Validate(fast = false) 注解
        MethodParameter parameter = mock(MethodParameter.class);
        Method method = TestController.class.getMethod("failSafeMethod");
        when(parameter.getMethod()).thenReturn(method);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        // Act
        ResponseEntity<?> response = handler.handleMethodArgumentNotValidException(ex);

        // Assert
        // MultiBusiness 固定为 400
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        // 返回多重错误结构
        assertThat(body.get("description").toString()).contains("共 2 项错误");
        assertThat(body.get("description").toString()).contains("Error 1");
        assertThat(body.get("description").toString()).contains("Error 2");
    }

    @Test
    @DisplayName("handleMethodArgumentNotValidException: 解析自定义错误码 (code:message)")
    void handleMethodArgumentNotValidException_CustomCode() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        // 消息格式为 "5000:Custom Error Message"
        List<FieldError> fieldErrors = Collections.singletonList(
                new FieldError("object", "field1", "5000:Custom Error Message")
        );
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
        when(bindingResult.getTarget()).thenReturn(new Object());

        MethodParameter parameter = mock(MethodParameter.class);
        when(parameter.getMethod()).thenReturn(null);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        // Act
        ResponseEntity<?> response = handler.handleMethodArgumentNotValidException(ex);

        // Assert
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertThat(body).containsEntry("code", 5000);
        assertThat(body).containsEntry("message", "Custom Error Message");
    }
    
    @Test
    @DisplayName("handleMethodArgumentNotValidException: 空错误列表处理")
    void handleMethodArgumentNotValidException_EmptyErrors() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());
        when(bindingResult.getTarget()).thenReturn(new Object());

        MethodParameter parameter = mock(MethodParameter.class);
        when(parameter.getMethod()).thenReturn(null);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        // Act
        ResponseEntity<?> response = handler.handleMethodArgumentNotValidException(ex);

        // Assert
        // 应该返回默认的 500 错误
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertThat(body).containsEntry("code", 500);
        assertThat(body).containsEntry("message", "Validation Error");
    }

    @Test
    @DisplayName("handleConstraintViolationException: 处理 Bean Validation 异常")
    void handleConstraintViolationException() {
        // Arrange
        ConstraintViolation violation1 = mock(ConstraintViolation.class);
        when(violation1.getMessage()).thenReturn("Violation 1");
        doReturn(TestController.class).when(violation1).getRootBeanClass();
        Path path1 = mock(Path.class);
        when(path1.toString()).thenReturn("method.param1");
        when(violation1.getPropertyPath()).thenReturn(path1);

        ConstraintViolation violation2 = mock(ConstraintViolation.class);
        when(violation2.getMessage()).thenReturn("Violation 2");
        doReturn(TestController.class).when(violation2).getRootBeanClass();
        Path path2 = mock(Path.class);
        when(path2.toString()).thenReturn("method.param2");
        when(violation2.getPropertyPath()).thenReturn(path2);

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation1);
        violations.add(violation2);

        ConstraintViolationException ex = new ConstraintViolationException(violations);

        // Act
        ResponseEntity<?> response = handler.handleConstraintViolationException(ex);

        // Assert
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        
        // 注意：Set 迭代顺序不确定，所以这里只检查包含
        String description = (String) body.get("description");
        assertThat(description).contains("共 2 项错误");
        assertThat(description).contains("Violation 1");
        assertThat(description).contains("Violation 2");
        
        // location 信息只在 Business 对象内部，不在 description 中展示，所以移除该断言
        // assertThat(description).contains("TestController.method at param1");
    }

    // --- 辅助类 ---

    static class TestController {
        @Validate(fast = false)
        public void failSafeMethod() {
        }
    }
}
