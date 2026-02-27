package com.chao.failfast.annotation;

import java.lang.annotation.*;

/**
 * 待实现 未来计划引入 @SkipValidation 注解，用于显式标记“无需校验的参数”
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SkipValidation {
}