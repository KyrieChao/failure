package com.chao.failfast.internal;

import com.chao.failfast.config.CodeMappingConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Ex 工具类测试")
class ExTest {

    private FailureContext context;

    @BeforeEach
    void setUp() {
        context = mock(FailureContext.class);
        Ex.setContext(context);
    }

    @AfterEach
    void tearDown() {
        Ex.setContext(null);
    }

    @Test
    @DisplayName("getContext 应返回设置的上下文")
    void shouldGetContext() {
        assertThat(Ex.getContext()).isEqualTo(context);
    }

    @Test
    @DisplayName("location 当未启用 shadowTrace 时应返回 null")
    void locationShouldReturnNullWhenDisabled() {
        when(context.isShadowTrace()).thenReturn(false);
        assertThat(Ex.location()).isNull();
    }

    @Test
    @DisplayName("method 当未启用 shadowTrace 时应返回 null")
    void methodShouldReturnNullWhenDisabled() {
        when(context.isShadowTrace()).thenReturn(false);
        assertThat(Ex.method()).isNull();
    }

    @Test
    @DisplayName("location 当启用 shadowTrace 时应返回位置信息")
    void locationShouldReturnInfoWhenEnabled() {
        when(context.isShadowTrace()).thenReturn(true);
        // ExTest is in skipped package, so it finds next frame which is likely ReflectionUtils or similar
        // Just verify it returns something non-null and valid-looking
        String location = Ex.location();
        assertThat(location).isNotNull();
        assertThat(location).contains(".java:");
    }

    @Test
    @DisplayName("method 当启用 shadowTrace 时应返回方法信息")
    void methodShouldReturnInfoWhenEnabled() {
        when(context.isShadowTrace()).thenReturn(true);
        String method = Ex.method();
        assertThat(method).isNotNull();
        assertThat(method).contains("#");
    }
}
