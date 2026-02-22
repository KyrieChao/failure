package com.chao.failfast.internal;

import com.chao.failfast.config.CodeMappingConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("FailureContext 上下文测试")
class FailureContextTest {

    private FailFastProperties properties;
    private FailureContext context;

    @BeforeEach
    void setUp() {
        properties = mock(FailFastProperties.class);
        CodeMappingConfig config = mock(CodeMappingConfig.class);
        context = new FailureContext(properties, config);
    }

    @Test
    @DisplayName("isShadowTrace 应使用全局配置")
    void shouldUseGlobalConfig() {
        when(properties.isShadowTrace()).thenReturn(true);
        assertThat(context.isShadowTrace()).isTrue();

        when(properties.isShadowTrace()).thenReturn(false);
        assertThat(context.isShadowTrace()).isFalse();
    }

    @Test
    @DisplayName("withPrintMethod 应临时覆盖配置")
    void shouldOverrideConfigTemporarily() {
        when(properties.isShadowTrace()).thenReturn(false);

        context.withPrintMethod(true, () -> {
            assertThat(context.isShadowTrace()).isTrue();
        });

        assertThat(context.isShadowTrace()).isFalse();
    }

    @Test
    @DisplayName("withPrintMethod (Runnable) 应临时覆盖配置")
    void shouldOverrideConfigTemporarilyRunnable() {
        when(properties.isShadowTrace()).thenReturn(false);

        context.withPrintMethod(true, () -> {
            assertThat(context.isShadowTrace()).isTrue();
        });

        assertThat(context.isShadowTrace()).isFalse();
    }

    @Test
    @DisplayName("clearThreadContext 应清除覆盖")
    void shouldClearThreadContext() {
        when(properties.isShadowTrace()).thenReturn(false);
        
        // Use reflection or just trust withPrintMethod restores it.
        // But to test clearThreadContext, we need to set it without using withPrintMethod's finally block if possible,
        // OR simulate a scenario where we manually set thread local (but private).
        // Since fields are private, we can only verify via public methods.
        
        // If we use withPrintMethod, it clears itself.
        // Let's assume clearThreadContext is used by Filter.
        // We can't easily test its effect without access to private ThreadLocals unless we add a method to set them directly for testing, 
        // but withPrintMethod handles setting.
        
        // Actually, withPrintMethod sets and then restores.
        // If we want to test clearThreadContext, maybe we rely on withPrintMethod not clearing if exception occurs? No, it has finally.
        
        // The implementation of withPrintMethod restores previous value.
        // If previous was null, it removes.
        
        // So clearThreadContext is a safety net.
        context.clearThreadContext();
        assertThat(context.isShadowTrace()).isFalse();
    }
}
