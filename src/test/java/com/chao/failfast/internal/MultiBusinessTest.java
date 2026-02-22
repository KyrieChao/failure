package com.chao.failfast.internal;

import com.chao.failfast.model.enums.TestResponseCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MultiBusiness 异常类测试")
class MultiBusinessTest {

    @Nested
    @DisplayName("构造函数测试")
    class ConstructorTest {
        @Test
        @DisplayName("应正确保存传入的错误列表")
        void shouldStoreErrorsCorrectly() {
            List<Business> errors = new ArrayList<>();
            errors.add(Business.of(TestResponseCode.PARAM_ERROR));
            errors.add(Business.of(TestResponseCode.SYSTEM_ERROR));

            MultiBusiness multiBusiness = new MultiBusiness(errors);
            assertThat(multiBusiness.getErrors()).hasSize(2);
            assertThat(multiBusiness.getErrors().get(0).getResponseCode().getCode()).isEqualTo(TestResponseCode.PARAM_ERROR.getCode());
        }

        @Test
        @DisplayName("错误列表应为不可变副本")
        void errorsShouldBeImmutable() {
            List<Business> errors = new ArrayList<>();
            errors.add(Business.of(TestResponseCode.PARAM_ERROR));

            MultiBusiness multiBusiness = new MultiBusiness(errors);
            
            // Modify original list
            errors.add(Business.of(TestResponseCode.SYSTEM_ERROR));
            
            assertThat(multiBusiness.getErrors()).hasSize(1);
        }
        
        @Test
        @DisplayName("当错误数量超过限制时应截断")
        void shouldTruncateWhenErrorsExceedLimit() {
            List<Business> errors = new ArrayList<>();
            for (int i = 0; i < 60; i++) {
                errors.add(Business.of(TestResponseCode.PARAM_ERROR));
            }
            
            MultiBusiness multiBusiness = new MultiBusiness(errors);
            assertThat(multiBusiness.getErrors()).hasSize(50);
        }
    }
}
