package com.chao.failfast.internal.chain;

import com.chao.failfast.annotation.FastValidator;
import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.MultiBusiness;
import com.chao.failfast.internal.core.ResponseCode;
import com.chao.failfast.internal.core.ViolationSpec;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ChainCore 核心逻辑测试")
class ChainCoreTest {

    // 最小化实现，用于测试核心逻辑
    static class TestChain extends ChainCore<TestChain> {
        protected TestChain(boolean failFast, FastValidator.ValidationContext context) {
            super(failFast, context);
        }

        public static TestChain create(boolean failFast) {
            return new TestChain(failFast, null);
        }

        public static TestChain create(FastValidator.ValidationContext context) {
            return new TestChain(context.isFast(), context);
        }
        
        // 暴露 protected 方法用于测试
        public TestChain publicCheck(boolean condition, Consumer<ViolationSpec> configurer) {
            return check(condition, configurer);
        }
        
        public TestChain publicCheck(boolean condition) {
            return check(condition);
        }
    }

    @Test
    @DisplayName("isValid: 初始状态应为有效")
    void shouldBeValidInitially() {
        TestChain chain = TestChain.create(true);
        assertThat(chain.isValid()).isTrue();
        assertThat(chain.isAlive()).isTrue();
        assertThat(chain.getCauses()).isEmpty();
    }

    @Test
    @DisplayName("check: 当条件为true时，不应产生错误")
    void shouldNotErrorWhenConditionIsTrue() {
        TestChain chain = TestChain.create(true);
        chain.publicCheck(true, s -> s.responseCode(ResponseCode.of(400, "Error")));
        
        assertThat(chain.isValid()).isTrue();
        assertThat(chain.getCauses()).isEmpty();
    }

    @Test
    @DisplayName("check: 当条件为false时，应产生错误")
    void shouldErrorWhenConditionIsFalse() {
        TestChain chain = TestChain.create(true);
        chain.publicCheck(false, s -> s.responseCode(ResponseCode.of(400, "Error")));
        
        assertThat(chain.isValid()).isFalse();
        assertThat(chain.getCauses()).hasSize(1);
        assertThat(chain.getCauses().get(0).getResponseCode().getCode()).isEqualTo(400);
    }

    @Test
    @DisplayName("check: Fail-Fast模式下，第一次失败后应标记为不活跃")
    void shouldBeNotAliveAfterErrorInFailFastMode() {
        TestChain chain = TestChain.create(true); // failFast = true
        chain.publicCheck(false, s -> s.responseCode(ResponseCode.of(400, "Error")));
        
        assertThat(chain.isAlive()).isFalse();
    }

    @Test
    @DisplayName("check: Fail-Strict模式下，失败后仍应保持活跃")
    void shouldRemainAliveAfterErrorInFailStrictMode() {
        TestChain chain = TestChain.create(false); // failFast = false
        chain.publicCheck(false, s -> s.responseCode(ResponseCode.of(400, "Error")));
        
        assertThat(chain.isAlive()).isTrue();
        assertThat(chain.isValid()).isFalse();
    }

    @Test
    @DisplayName("check: Fail-Fast模式下，不活跃时应跳过后续检查")
    void shouldSkipChecksWhenNotAlive() {
        TestChain chain = TestChain.create(true);
        
        // First error
        chain.publicCheck(false, s -> s.responseCode(ResponseCode.of(400, "Error 1")));
        assertThat(chain.isAlive()).isFalse();
        
        // Second check (should be skipped)
        chain.publicCheck(false, s -> s.responseCode(ResponseCode.of(500, "Error 2")));
        
        assertThat(chain.getCauses()).hasSize(1); // Only the first error
        assertThat(chain.getCauses().get(0).getResponseCode().getMessage()).isEqualTo("Error 1");
    }

    @Test
    @DisplayName("check: 无配置 check(boolean) 使用默认错误")
    void shouldAddDefaultErrorWhenNoConfig() {
        TestChain chain = TestChain.create(true);
        chain.publicCheck(false);
        
        assertThat(chain.isValid()).isFalse();
        assertThat(chain.getCauses().get(0).getResponseCode().getCode()).isEqualTo(500);
        assertThat(chain.getCauses().get(0).getDetail()).contains("未通过");
    }

    @Test
    @DisplayName("Context集成: 错误应报告给 Context")
    void shouldReportToContext() {
        FastValidator.ValidationContext context = new FastValidator.ValidationContext(true);
        TestChain chain = TestChain.create(context);
        
        chain.publicCheck(false, s -> s.responseCode(ResponseCode.of(400, "Error")));
        
        assertThat(context.isValid()).isFalse();
        assertThat(context.getErrors()).hasSize(1);
    }
}
