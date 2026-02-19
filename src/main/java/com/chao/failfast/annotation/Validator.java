package com.chao.failfast.annotation;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.ResponseCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 验证器函数式接口 - 支持自定义验证逻辑
 * 用于实现特定业务场景的参数验证规则
 *
 * @param <T> 要验证的目标类型，可以是任何Java对象
 */
@FunctionalInterface
public interface Validator<T> {

    /**
     * 执行具体的验证逻辑
     * 实现类需要在此方法中编写验证规则，并通过context报告验证结果
     *
     * @param target  待验证的目标对象
     * @param context 验证上下文，用于收集验证错误和控制验证流程
     */
    void validate(T target, ValidationContext context);


    /**
     * 返回此验证器主要支持的目标类型
     * 用于框架自动过滤不匹配的参数，避免 ClassCastException
     * 默认返回 Object.class，表示支持任意类型（兼容旧实现）
     */
    default Class<?> getSupportedType() {
        return Object.class;
    }

    /**
     * 验证上下文类 - 管理验证过程中的状态和错误收集
     * 提供统一的错误收集机制和验证流程控制
     */
    class ValidationContext {
        /**
         * 是否启用快速失败模式
         */
        private final boolean failFast;

        private final Class<?>[] groups;
        /**
         * 收集的验证错误列表
         */
        private final List<Business> errors = new ArrayList<>();

        /**
         * 验证是否已停止（快速失败模式下使用）
         */
        @Getter
        private boolean stopped = false;

        /**
         * 构造验证上下文
         *
         * @param failFast 是否启用快速失败模式
         */
        public ValidationContext(boolean failFast, Class<?>[] groups) {
            this.failFast = failFast;
            this.groups = groups;
        }

        /**
         * 添加验证错误（使用默认描述）
         *
         * @param code 响应码，包含错误类型和默认描述
         */
        public void addError(ResponseCode code) {
            addError(code, code.getDescription());
        }

        /**
         * 添加验证错误（指定详细描述）
         *
         * @param code   响应码
         * @param detail 详细的错误描述信息
         */
        public void addError(ResponseCode code, String detail) {
            // 如果已停止验证，则忽略后续错误
            if (stopped) return;
            errors.add(Business.of(code, detail));
            // 快速失败模式下，添加第一个错误后立即停止
            if (failFast) stopped = true;
        }

        /**
         * 判断当前上下文是否包含指定的分组
         * - 如果注解没指定 groups → 总是返回 true（全执行）
         * - 如果指定了，则只有匹配的分组才执行
         */
        public boolean matchesGroup(Class<?> requiredGroup) {
            if (groups.length == 0) {
                return true;
            }
            for (Class<?> g : groups) {
                if (requiredGroup.isAssignableFrom(g)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 添加已构建的Business异常作为验证错误
         *
         * @param business 已构建的Business异常对象
         */
        public void addError(Business business) {
            // 如果已停止验证，则忽略后续错误
            if (stopped) return;

            errors.add(business);

            // 快速失败模式下，添加第一个错误后立即停止
            if (failFast) stopped = true;
        }

        /**
         * 主动停止验证过程
         * 在某些特殊场景下，验证器可以主动要求停止后续验证
         */
        public void stop() {
            this.stopped = true;
        }

        /**
         * 检查验证是否通过
         *
         * @return 如果没有收集到任何错误则返回true，否则返回false
         */
        public boolean isValid() {
            return errors.isEmpty();
        }

        /**
         * 获取所有验证错误的不可变副本
         *
         * @return 验证错误列表的只读副本
         */
        public List<Business> getErrors() {
            return List.copyOf(errors);
        }

        /**
         * 获取第一个验证错误
         *
         * @return 第一个验证错误，如果没有错误则返回null
         */
        public Business getFirstError() {
            return errors.isEmpty() ? null : errors.get(0);
        }
    }
}
