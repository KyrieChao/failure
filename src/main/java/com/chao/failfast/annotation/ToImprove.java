package com.chao.failfast.annotation;

import java.lang.annotation.*;

/**
 * 表示此方法/类/字段需要重构或优化
 * 原因/计划请见 Javadoc 或关联 Issue
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface ToImprove {

    /**
     * 重构原因或计划
     */
    String value() default "";

    /**
     * 预计完成版本
     */
    String inVersion() default "";

    /**
     * 关联的 Issue / PR 链接
     */
    String issue() default "";
}