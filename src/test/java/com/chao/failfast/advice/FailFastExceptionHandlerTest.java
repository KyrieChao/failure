package com.chao.failfast.advice;

import com.chao.failfast.internal.Business;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FailFastExceptionHandlerTest {

    static class TestController {
        public void updateUser() {
        }
    }

    static class TestHandler extends FailFastExceptionHandler {
        public final AtomicReference<Business> capturedException = new AtomicReference<>();

        @Override
        protected void logException(Business e) {
            capturedException.set(e);
            // We don't call super.logException(e) to avoid needing a logger or full context
        }
    }

    @Test
    void testHandleMethodArgumentNotValidException_ShouldUseMethodName() throws NoSuchMethodException {
        // Arrange
        TestHandler handler = new TestHandler();

        Method method = TestController.class.getMethod("updateUser");
        MethodParameter parameter = mock(MethodParameter.class);
        when(parameter.getMethod()).thenReturn(method);

        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("user", "name", "Name cannot be empty");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
        when(bindingResult.getTarget()).thenReturn(new TestController());

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);

        // Act
        handler.handleMethodArgumentNotValidException(exception);

        // Assert
        Business captured = handler.capturedException.get();
        // The method name should be "TestController#updateUser"
        assertEquals("TestController#updateUser", captured.getMethod());
    }

    @Test
    void testHandleConstraintViolationException_ShouldUseMethodNameFromPath() {
        // Arrange
        TestHandler handler = new TestHandler();

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("updateUser.name");
        when(violation.getPropertyPath()).thenReturn(path);
        doReturn(TestController.class).when(violation).getRootBeanClass();
        when(violation.getMessage()).thenReturn("Name cannot be empty");

        ConstraintViolationException exception = new ConstraintViolationException(Collections.singleton(violation));

        // Act
        handler.handleConstraintViolationException(exception);

        // Assert
        Business captured = handler.capturedException.get();
        assertEquals("TestController#updateUser", captured.getMethod());
    }
}
