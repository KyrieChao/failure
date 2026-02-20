package com.chao.failfast.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Validate {
    /**
     * 验证器
     *
     * @return 验证器
     */
    Class<? extends FastValidator>[] value() default {};

    /**
     * 是否启用快速模式
     *
     * @return true: 快速模式，不立即抛出异常
     */
    boolean fast() default true;
}
