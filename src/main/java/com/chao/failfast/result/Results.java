package com.chao.failfast.result;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.MultiBusiness;
import com.chao.failfast.internal.ResponseCode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Result 工具类 - 提供批量操作和便捷方法
 * 封装常用的Result操作模式，简化函数式编程
 */
public final class Results {

    /**
     * 私有构造函数，防止实例化
     */
    private Results() {
    }

    /**
     * 包装可能抛出异常的操作为 Result
     * 自动捕获异常并转换为相应的Result
     *
     * @param supplier  可能抛出异常的操作
     * @param errorCode 异常时使用的错误码
     * @param <T>       返回值类型
     * @return Result结果
     */
    public static <T> Result<T> tryOf(Supplier<T> supplier, ResponseCode errorCode) {
        try {
            return Result.ok(supplier.get());
        } catch (Business e) {
            return Result.fail(e);
        } catch (Exception e) {
            return Result.fail(errorCode, e.getMessage());
        }
    }

    public static <T> Result<T> tryOf(Supplier<T> supplier, ResponseCode errorCode, String detail) {
        try {
            return Result.ok(supplier.get());
        } catch (Business e) {
            return Result.fail(e);
        } catch (Exception e) {
            return Result.fail(errorCode, detail);
        }
    }

    /**
     * 包装可能抛出异常的Runnable
     * 适用于无返回值的操作
     *
     * @param runnable  可能抛出异常的Runnable
     * @param errorCode 异常时使用的错误码
     * @return Result结果
     */
    public static Result<Void> tryRun(Runnable runnable, ResponseCode errorCode) {
        if (runnable == null) {
            throw new NullPointerException("runnable is null");
        }
        try {
            runnable.run();
            return Result.ok(null);
        } catch (Business e) {
            return Result.fail(e);
        } catch (Exception e) {
            return Result.fail(errorCode, e.getMessage());
        }
    }
    public static Result<Void> tryRun(Runnable runnable, ResponseCode errorCode, String detail) {
        if (runnable == null) {
            throw new NullPointerException("runnable is null");
        }
        try {
            runnable.run();
            return Result.ok(null);
        } catch (Business e) {
            return Result.fail(e);
        } catch (Exception e) {
            return Result.fail(errorCode, detail);
        }
    }

    /**
     * 收集多个 Result，返回所有成功的值或第一个错误
     * 快速失败模式：遇到第一个错误立即返回
     *
     * @param results Result数组
     * @param <T>     值类型
     * @return 包含所有成功值的Result，或第一个错误
     */
    @SafeVarargs
    public static <T> Result<List<T>> sequence(Result<T>... results) {
        List<T> successes = new ArrayList<>();
        for (Result<T> result : results) {
            if (result.isFailure()) {
                return Result.fail(result.getError());
            }
            successes.add(result.get());
        }
        return Result.ok(successes);
    }

    /**
     * 收集多个 Result，返回所有成功的值或所有错误
     * 全量收集模式：收集所有成功和失败的结果
     *
     * @param results Result数组
     * @param <T>     值类型
     * @return 包含所有成功值的Result，或包含所有错误的MultiBusiness
     */
    @SafeVarargs
    public static <T> Result<List<T>> sequenceAll(Result<T>... results) {
        List<T> successes = new ArrayList<>();
        List<Business> failures = new ArrayList<>();

        for (Result<T> result : results) {
            if (result.isSuccess()) successes.add(result.get());
            else failures.add(result.getError());
        }
        if (!failures.isEmpty()) {
            return Result.fail(new MultiBusiness(failures));
        }
        return Result.ok(successes);
    }

    /**
     * 遍历列表，对每个元素应用函数，收集所有结果
     * 快速失败模式：遇到第一个错误立即返回
     *
     * @param list   待处理的列表
     * @param mapper 映射函数
     * @param <T>    输入类型
     * @param <R>    输出类型
     * @return 包含所有成功映射结果的Result
     */
    public static <T, R> Result<List<R>> traverse(List<T> list, Function<T, Result<R>> mapper) {
        List<R> results = new ArrayList<>();
        for (T item : list) {
            Result<R> result = mapper.apply(item);
            if (result.isFailure()) return Result.fail(result.getError());
            results.add(result.get());
        }
        return Result.ok(results);
    }

    /**
     * 从 Result 中提取值，失败时返回 null
     * 适用于可以接受null值的场景
     *
     * @param result Result对象
     * @param <T>    值类型
     * @return 成功值或null
     */
    public static <T> T getOrNull(Result<T> result) {
        return result.isSuccess() ? result.get() : null;
    }

    /**
     * 条件执行
     * 根据条件决定是否执行Supplier提供的操作
     *
     * @param condition 执行条件
     * @param supplier  Result提供者
     * @param <T>       值类型
     * @return 条件满足时返回supplier的结果，否则返回成功的null值
     */
    public static <T> Result<T> when(boolean condition, Supplier<Result<T>> supplier) {
        if (condition) return supplier.get();
        return Result.ok(null);
    }
}
