package com.chao.failfast.annotation;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.core.ResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 验证器接口 - 支持自定义验证逻辑
 *
 * @param <T> 目标类型
 */
@FunctionalInterface
public interface FastValidator<T> {

    /**
     * 验证方法，用于对目标对象进行有效性验证
     *
     * @param target  需要验证的目标对象
     * @param context 验证上下文，包含验证所需的规则和条件
     */
    void validate(T target, ValidationContext context);

    /**
     * 获取当前处理器支持的默认类型
     *
     * @return 返回Object.class作为默认支持的类型
     */
    default Class<?> getSupportedType() {
        return Object.class;
    }

    @RequiredArgsConstructor
    class ValidationContext {
        @Getter
        private final boolean fast;
        private final List<Business> errors = new ArrayList<>();
        @Getter
        private boolean stopped;

        public void reportError(ResponseCode code) {
            reportError(Business.of(code));
        }

        public void reportError(ResponseCode code, String detail) {
            reportError(Business.of(code, detail));
        }

        public void reportError(Business error) {
            if (stopped) return;
            errors.add(error);
            if (fast) stopped = true;
        }

        public void stop() {
            this.stopped = true;
        }

        public boolean isFailed() {
            return !isValid() || stopped;
        }

        public boolean isValid() {
            return errors.isEmpty();
        }

        /**
         * 获取包含原因的业务对象列表
         * 返回一个不可修改的列表，以确保数据的不可变性
         *
         * @return 包含原因的业务对象列表，类型为List<Business>
         */
        public List<Business> hasCauses() {
            return Collections.unmodifiableList(errors);
        }

        /**
         * 获取第一个错误信息
         * 如果错误列表为空，则返回null，否则返回列表中的第一个错误
         *
         * @return Business类型的错误对象，如果没有错误则返回null
         */
        public Business getFirstError() {
            return errors.isEmpty() ? null : errors.get(0);
        }
    }
}
