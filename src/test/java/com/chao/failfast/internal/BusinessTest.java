package com.chao.failfast.internal;

import com.chao.failfast.model.enums.TestResponseCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Business 异常类测试")
class BusinessTest {

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
}
