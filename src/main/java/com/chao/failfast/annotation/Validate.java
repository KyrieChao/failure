package com.chao.failfast.annotation;

import java.lang.annotation.*;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Validate {

    /**
     * 自定义验证器类数组
     * 可以指定多个验证器，按顺序执行验证逻辑
     *
     * @return 验证器类数组，默认为空数组
     */
    Class<? extends Validator>[] value() default {};

    /**
     * 是否启用快速失败模式
     * true: 遇到第一个验证错误立即终止验证并抛出异常
     * false: 收集所有验证错误后再统一处理
     *
     * @return 快速失败开关，默认为true
     */
    boolean failFast() default true;

    /**
     * 目标参数名称数组，默认为空
     * 用于指定需要验证的参数名称
     *
     * @return 参数名称数组，默认为空数组
     */
    String[] targets() default {};

    /**
     * 验证分组
     * 用于支持不同场景下的差异化验证需求
     * 可以配合验证器实现基于分组的条件验证
     *
     * @return 分组标识类数组，默认为空数组
     */
    Class<?>[] groups() default {};

    /**
     * 简单场景下的错误码
     * 适用于不需要复杂验证逻辑的简单校验场景
     * 当指定了errorCode时，会优先使用此错误码
     *
     * @return 错误码字符串，默认为空字符串
     */
    String errorCode() default "";

    /**
     * 自定义错误消息
     * 当errorCode指定时，可以配合errorMessage提供更友好的错误提示
     *
     * @return 错误消息字符串，默认为空字符串
     */
    String errorMessage() default "";
}
