package com.chao.failfast.annotation;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.ResponseCode;
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

        public void addError(ResponseCode code) {
            addError(Business.of(code));
        }

        public void addErrorAndHalt(ResponseCode code) {
            addErrorAndHalt(Business.of(code));
        }

        public void addError(ResponseCode code, String detail) {
            addError(Business.of(code, detail));
        }

        public void addErrorAndHalt(ResponseCode code, String detail) {
            addErrorAndHalt(Business.of(code, detail));
        }

        public void addError(Business error) {
            if (stopped) return;
            errors.add(error);
            if (fast) stopped = true;
        }

        public void addErrorAndHalt(Business error) {
            if (stopped) return;
            errors.add(error);
            stopped = true;
        }

        public void stop() {
            this.stopped = true;
        }

        public boolean isValid() {
            return errors.isEmpty();
        }

        public List<Business> getErrors() {
            return Collections.unmodifiableList(errors);
        }

        public Business getFirstError() {
            return errors.isEmpty() ? null : errors.get(0);
        }
    }
}
