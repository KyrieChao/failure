package com.chao.failfast.result;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.ResponseCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 函数式结果封装 - 避免异常作为控制流
 * 提供Success/Failure两种状态的安全结果处理方式
 * 支持函数式编程风格的操作链
 *
 * @param <T> 成功时的返回值类型
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public sealed class Result<T> permits Result.Success, Result.Failure {

    protected int code;
    protected String message;
    protected String description;
    protected long timestamp;

    /**
     * 私有构造函数，防止外部实例化
     */
    private Result(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 创建成功的Result
     *
     * @param value 成功值
     * @param <T>   值类型
     * @return Success结果
     */
    public static <T> Result<T> ok(T value) {
        return new Success<>(value);
    }

    /**
     * 创建失败的Result（使用响应码）
     *
     * @param code 响应码
     * @param <T>  值类型
     * @return Failure结果
     */
    public static <T> Result<T> fail(ResponseCode code) {
        return new Failure<>(Business.of(code));
    }

    /**
     * 创建失败的Result（使用响应码和详细描述）
     *
     * @param code   响应码
     * @param detail 详细描述
     * @param <T>    值类型
     * @return Failure结果
     */
    public static <T> Result<T> fail(ResponseCode code, String detail) {
        return new Failure<>(Business.of(code, detail));
    }

    /**
     * 创建失败的Result（使用Business异常）
     *
     * @param business Business异常
     * @param <T>      值类型
     * @return Failure结果
     */
    public static <T> Result<T> fail(Business business) {
        return new Failure<>(business);
    }

    /**
     * 根据值是否为null创建Result
     *
     * @param value 值
     * @param code  失败时的响应码
     * @param <T>   值类型
     * @return Result结果
     */
    public static <T> Result<T> ofNullable(T value, ResponseCode code) {
        return value != null ? ok(value) : fail(code);
    }

    /**
     * 根据值是否为null创建Result（带详细描述）
     *
     * @param value  值
     * @param code   失败时的响应码
     * @param detail 失败时的详细描述
     * @param <T>    值类型
     * @return Result结果
     */
    public static <T> Result<T> ofNullable(T value, ResponseCode code, String detail) {
        return value != null ? ok(value) : fail(code, detail);
    }

    /**
     * 检查是否为成功状态
     *
     * @return true表示成功，false表示失败
     */
    @JsonIgnore
    public boolean isSuccess() {
        return this instanceof Success;
    }

    /**
     * 检查是否为失败状态
     *
     * @return true表示失败，false表示成功
     */
    @JsonIgnore
    public boolean isFailure() {
        return this instanceof Failure;
    }

    /**
     * 获取成功值
     *
     * @return 成功值
     * @throws IllegalStateException 当Result为失败状态时抛出
     */
    @JsonIgnore
    public T get() {
        if (this instanceof Success<T> s) return s.data;
        throw new IllegalStateException("Result is failure");
    }

    /**
     * 获取错误信息
     *
     * @return Business异常
     * @throws IllegalStateException 当Result为成功状态时抛出
     */
    @JsonIgnore
    public Business getError() {
        if (this instanceof Failure<T> f) return f.error;
        throw new IllegalStateException("Result is success");
    }

    // ============ 函数式操作 ============

    /**
     * 映射成功值到新类型
     *
     * @param mapper 映射函数
     * @param <R>    目标类型
     * @return 映射后的Result
     */
    public <R> Result<R> map(Function<T, R> mapper) {
        if (this instanceof Success<T> s) {
            try {
                return Result.ok(mapper.apply(s.data));
            } catch (Exception e) {
                if (e instanceof Business b) {
                    return Result.fail(b);
                }
                throw e;
            }
        }
        @SuppressWarnings("unchecked")
        Result<R> failureResult = (Result<R>) this;
        return failureResult;
    }

    /**
     * 扁平映射成功值到新的Result
     *
     * @param mapper 扁平映射函数
     * @param <R>    目标类型
     * @return 映射后的Result
     */
    public <R> Result<R> flatMap(Function<T, Result<R>> mapper) {
        if (this instanceof Success<T> s) {
            return mapper.apply(s.data);
        }
        @SuppressWarnings("unchecked")
        Result<R> failureResult = (Result<R>) this;
        return failureResult;
    }

    /**
     * 对成功值执行副作用操作
     *
     * @param action 副作用操作
     * @return 原始Result
     */
    public Result<T> peek(Consumer<T> action) {
        if (this instanceof Success<T> s) {
            action.accept(s.data);
        }
        return this;
    }

    /**
     * 对错误执行副作用操作
     *
     * @param action 副作用操作
     * @return 原始Result
     */
    public Result<T> peekError(Consumer<Business> action) {
        if (this instanceof Failure<T> f) {
            action.accept(f.error);
        }
        return this;
    }

    /**
     * 过滤成功值
     *
     * @param predicate 过滤条件
     * @param code      不满足条件时的错误码
     * @return 过滤后的Result
     */
    public Result<T> filter(Function<T, Boolean> predicate, ResponseCode code) {
        if (this instanceof Success<T> s) {
            if (!predicate.apply(s.data)) {
                return Result.fail(code);
            }
        }
        return this;
    }

    // ============ 恢复操作 ============

    /**
     * 从错误中恢复为成功值
     *
     * @param recovery 回复函数
     * @return 恢复后的Result
     */
    public Result<T> recover(Function<Business, T> recovery) {
        if (this instanceof Failure<T> f) {
            return Result.ok(recovery.apply(f.error));
        }
        return this;
    }

    /**
     * 从错误中恢复为新的Result
     *
     * @param recovery 恢复函数
     * @return 恢复后的Result
     */
    public Result<T> recoverWith(Function<Business, Result<T>> recovery) {
        if (this instanceof Failure<T> f) {
            return recovery.apply(f.error);
        }
        return this;
    }

    // ============ 终结操作 ============

    /**
     * 获取值或通过Supplier提供的默认值
     *
     * @param supplier 默认值提供者
     * @return 成功值或默认值
     */
    public T onFailGet(Supplier<T> supplier) {
        return isSuccess() ? get() : supplier.get();
    }

    /**
     * 获取值或抛出Business异常
     *
     * @return 成功值
     * @throws Business 当Result为失败状态时抛出
     */
    public T failNow() {
        if (this instanceof Failure<T> f) throw f.error;
        return get();
    }

    /**
     * 获取值或默认值
     *
     * @param defaultValue 默认值
     * @return 成功值或默认值
     */
    public T failNow(T defaultValue) {
        return isSuccess() ? get() : defaultValue;
    }

    /**
     * 获取值或抛出自定义异常
     *
     * @param exceptionProvider 异常提供者
     * @param <X>               异常类型
     * @return 成功值
     * @throws X 当Result为失败状态时抛出
     */
    public <X extends Throwable> T failNow(Function<Business, X> exceptionProvider) throws X {
        if (this instanceof Failure<T> f) {
            throw exceptionProvider.apply(f.error);
        }
        return get();
    }

    /**
     * 组合两个Result
     *
     * @param other    另一个Result
     * @param combiner 组合函数
     * @param <U>      另一个Result的类型
     * @param <R>      组合后的类型
     * @return 组合后的Result
     */
    public <U, R> Result<R> combine(Result<U> other, BiFunction<T, U, R> combiner) {
        if (this.isFailure()) {
            @SuppressWarnings("unchecked")
            Result<R> failureResult = (Result<R>) this;
            return failureResult;
        }
        if (other.isFailure()) {
            @SuppressWarnings("unchecked")
            Result<R> failureResult = (Result<R>) other;
            return failureResult;
        }
        return Result.ok(combiner.apply(this.get(), other.get()));
    }

    // ============ 内部类 ============

    /**
     * 成功状态的Result实现
     *
     * @param <T> 数据类型
     */
    @Getter
    public static final class Success<T> extends Result<T> {
        /**
         * 成功数据
         */
        private final T data;

        /**
         * 构造函数
         *
         * @param data 成功数据
         */
        public Success(T data) {
            super(200, "Success", "操作成功");
            this.data = data;
        }
    }

    /**
     * 失败状态的Result实现
     *
     * @param <T> 数据类型
     */
    @Getter
    public static final class Failure<T> extends Result<T> {
        /**
         * 错误信息
         */
        @JsonIgnore
        private final Business error;

        /**
         * 构造函数
         *
         * @param error 错误信息
         */
        public Failure(Business error) {
            super(error.getCode().getCode(), error.getCode().getMessage(), error.getDetail());
            this.error = error;
        }
    }
}
