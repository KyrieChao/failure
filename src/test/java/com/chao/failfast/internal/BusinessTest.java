package com.chao.failfast.internal;

import com.chao.failfast.config.CodeMappingConfig;
import com.chao.failfast.model.enums.TestResponseCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Business 异常类测试")
class BusinessTest {

    @AfterEach
    void tearDown() {
        Ex.setContext(null);
    }

    @Nested
    @DisplayName("工厂方法测试")
    class FactoryMethodTest {
        @Test
        @DisplayName("of(ResponseCode) 应创建基本异常")
        void shouldCreateBasicException() {
            Business business = Business.of(TestResponseCode.PARAM_ERROR);
            assertThat(business.getResponseCode().getCode()).isEqualTo(TestResponseCode.PARAM_ERROR.getCode());
            assertThat(business.getMessage()).isEqualTo(TestResponseCode.PARAM_ERROR.getMessage());
        }

        @Test
        @DisplayName("of(ResponseCode, String) 应创建带详情异常")
        void shouldCreateExceptionWithDetail() {
            Business business = Business.of(TestResponseCode.PARAM_ERROR, "detail");
            assertThat(business.getResponseCode().getCode()).isEqualTo(TestResponseCode.PARAM_ERROR.getCode());
            assertThat(business.getDetail()).isEqualTo("detail");
        }

        @Test
        @DisplayName("of(ResponseCode, String, Object...) 应创建带格式化详情异常")
        void shouldCreateExceptionWithFormattedDetail() {
            Business business = Business.of(TestResponseCode.PARAM_ERROR, "detail %s", "123");
            assertThat(business.getDetail()).isEqualTo("detail 123");
        }
    }

    @Nested
    @DisplayName("toString 格式测试")
    class ToStringTest {
        @Test
        @DisplayName("toString 应包含错误码和消息")
        void toStringShouldContainCodeAndMessage() {
            Business business = Business.of(TestResponseCode.PARAM_ERROR);
            String str = business.toString();
            // Code 40001 -> 400_01
            assertThat(str).contains("code=400_01");
            assertThat(str).contains("mes=" + TestResponseCode.PARAM_ERROR.getMessage());
        }
    }

    @Nested
    @DisplayName("fillInStackTrace 测试")
    class FillInStackTraceTest {
        @Test
        @DisplayName("当 code 为 null 时应填充堆栈")
        void shouldFillStackTraceWhenCodeIsNull() {
            // responseCode is null
            Business business = new Business(null, null, null, null, null);
            // Stack trace should be present
            assertThat(business.getStackTrace()).isNotEmpty();
        }

        @Test
        @DisplayName("当启用 shadowTrace 时应填充堆栈")
        void shouldFillStackTraceWhenShadowTraceEnabled() {
            FailureContext context = mock(FailureContext.class);
            when(context.isShadowTrace()).thenReturn(true);
            Ex.setContext(context);

            Business business = Business.of(TestResponseCode.PARAM_ERROR);
            assertThat(business.getStackTrace()).isNotEmpty();
        }

        @Test
        @DisplayName("当禁用 shadowTrace 且非 5xx 错误时应跳过堆栈填充")
        void shouldSkipStackTraceWhenShadowTraceDisabledAndNot5xx() {
            FailureContext context = mock(FailureContext.class);
            when(context.isShadowTrace()).thenReturn(false);
            CodeMappingConfig config = mock(CodeMappingConfig.class);
            when(context.getCodeMappingConfig()).thenReturn(config);
            // 400 is not 5xx
            when(config.resolveHttpStatus(anyInt())).thenReturn(HttpStatus.BAD_REQUEST);
            Ex.setContext(context);

            Business business = Business.of(TestResponseCode.PARAM_ERROR);
            // Stack trace should be empty (suppressed)
            assertThat(business.getStackTrace()).isEmpty();
        }
        
        @Test
        @DisplayName("当禁用 shadowTrace 但为 5xx 错误时应填充堆栈")
        void shouldFillStackTraceWhen5xx() {
            FailureContext context = mock(FailureContext.class);
            when(context.isShadowTrace()).thenReturn(false);
            CodeMappingConfig config = mock(CodeMappingConfig.class);
            when(context.getCodeMappingConfig()).thenReturn(config);
            when(config.resolveHttpStatus(anyInt())).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
            Ex.setContext(context);

            Business business = Business.of(TestResponseCode.ERROR);
            assertThat(business.getStackTrace()).isNotEmpty();
        }
    }
}
