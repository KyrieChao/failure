package com.chao.failfast.config;

import com.chao.failfast.advice.DefaultExceptionHandler;
import com.chao.failfast.advice.FailFastExceptionHandler;
import com.chao.failfast.aspect.ValidationAspect;
import com.chao.failfast.internal.FailureContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FailFastAutoConfiguration 自动配置测试")
class FailFastAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(FailFastAutoConfiguration.class, ValidationAutoConfiguration.class));

    @Test
    @DisplayName("应当创建核心 Bean")
    void shouldCreateCoreBeans() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(FailFastAutoConfiguration.class);
            assertThat(context).hasSingleBean(FailureContext.class);
            assertThat(context).hasSingleBean(CodeMappingConfig.class);
            assertThat(context).hasSingleBean(DefaultExceptionHandler.class);
            assertThat(context).hasSingleBean(ValidationAspect.class);
            assertThat(context).hasSingleBean(FailFastAutoConfiguration.ExInitializer.class);
        });
    }

    @Test
    @DisplayName("应当根据配置创建 CodeMappingConfig")
    void shouldCreateCodeMappingConfigWithProperties() {
        contextRunner.withPropertyValues(
                "fail-fast.code-mapping.http-status.40001=400",
                "fail-fast.code-mapping.groups.auth=40100..40199"
        ).run(context -> {
            CodeMappingConfig config = context.getBean(CodeMappingConfig.class);
            assertThat(config.resolveHttpStatus(40001)).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(config.isInGroup(40101, "auth")).isTrue();
        });
    }

    @Test
    @DisplayName("应当创建 Filter")
    void shouldCreateFilterInWebApplication() {
        new WebApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(FailFastAutoConfiguration.class, ValidationAutoConfiguration.class))
                .run(context -> assertThat(context).hasBean("failFastCleanupFilter"));
    }

    @Test
    @DisplayName("当用户自定义异常处理器时应当不创建 DefaultExceptionHandler")
    void shouldNotCreateDefaultExceptionHandlerWhenUserDefined() {
        contextRunner.withUserConfiguration(CustomExceptionHandlerConfig.class)
                .run(context -> {
                    assertThat(context).hasSingleBean(CustomExceptionHandler.class);
                    assertThat(context).doesNotHaveBean(DefaultExceptionHandler.class);
                });
    }

    @Configuration
    static class CustomExceptionHandlerConfig {
        @Bean
        public CustomExceptionHandler customExceptionHandler() {
            return new CustomExceptionHandler();
        }
    }

    static class CustomExceptionHandler extends FailFastExceptionHandler {
        // Implementation
    }
}
