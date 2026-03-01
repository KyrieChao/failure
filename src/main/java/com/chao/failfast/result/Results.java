package com.chao.failfast.result;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.MultiBusiness;
import com.chao.failfast.internal.core.ResponseCode;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.*;

/**
 * Result 工具类 - 提供批量操作和便捷方法
 * 封装常用的Result操作模式，简化函数式编程
 */
public final class Results {

    private Results() {
    }

    private record SplitResult<T>(List<T> successes, List<Business> failures) {
    }

    // ==================== 异常捕获 ====================

    public static <T> Result<T> tryOf(Supplier<T> supplier, ResponseCode errorCode) {
        return tryOf(supplier, errorCode, null);
    }

    public static <T> Result<T> tryOf(Supplier<T> supplier, ResponseCode errorCode, String detail) {
        try {
            return Result.ok(supplier.get());
        } catch (Business e) {
            return Result.fail(e);
        } catch (Exception e) {
            String des = detail != null ? detail : e.getMessage() != null ? e.getMessage() : errorCode.getDescription();
            return Result.fail(errorCode, des);
        }
    }

    public static Result<Void> tryRun(Runnable runnable, ResponseCode errorCode) {
        return tryRun(runnable, errorCode, null);
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
            String des = detail != null ? detail : e.getMessage() != null ? e.getMessage() : errorCode.getDescription();
            return Result.fail(errorCode, des);
        }
    }

    // ==================== Optional 转换 ====================
    public static <T> Result<T> fromOptional(Optional<T> optional, ResponseCode errorCode) {
        return fromOptional(optional, errorCode, null);
    }

    public static <T> Result<T> fromOptional(Optional<T> optional, ResponseCode errorCode, String detail) {
        if (optional == null) {
            return Result.fail(errorCode, detail != null ? detail : "Optional is null");
        }
        return optional.map(Result::ok).orElseGet(() -> Result.fail(errorCode, detail));
    }

    public static <T> Result<T> fromOptionalOrElse(Optional<T> optional, T defaultValue) {
        return Result.ok(optional != null ? optional.orElse(defaultValue) : defaultValue);
    }

    // ==================== 条件执行 ====================

    /**
     * 条件为 true 时执行 supplier，false 返回 ok(null)
     */
    public static <T> Result<T> when(boolean condition, Supplier<Result<T>> supplier) {
        if (condition) {
            if (supplier == null) {
                throw new NullPointerException("supplier is null");
            }
            return supplier.get();
        }
        return Result.ok(null);
    }

    /**
     * 条件为 true 时返回 successValue，false 返回 fail
     */
    public static <T> Result<T> whenOrFail(boolean condition, T successValue, ResponseCode failCode) {
        return whenOrFail(condition, successValue, failCode, null);
    }

    public static <T> Result<T> whenOrFail(boolean condition, T successValue, ResponseCode failCode, String detail) {
        return condition ? Result.ok(successValue) : Result.fail(failCode, detail);
    }

    /**
     * 条件为 true 时执行 supplier，false 返回 fail
     */
    public static <T> Result<T> whenOrFail(boolean condition, Supplier<T> supplier, ResponseCode failCode) {
        return whenOrFail(condition, supplier, failCode, null);
    }

    public static <T> Result<T> whenOrFail(boolean condition, Supplier<T> supplier, ResponseCode failCode, String detail) {
        if (!condition) {
            return Result.fail(failCode, detail);
        }
        try {
            return Result.ok(supplier.get());
        } catch (Business e) {
            return Result.fail(e);
        } catch (Exception e) {
            return Result.fail(failCode, detail != null ? detail : e.getMessage());
        }
    }

    // ==================== 批量收集 ====================

    @SafeVarargs
    public static <T> Result<List<T>> sequence(Result<T>... results) {
        return sequence(List.of(results));
    }

    public static <T> Result<List<T>> sequence(List<Result<T>> results) {
        List<T> successes = new ArrayList<>();
        for (Result<T> result : results) {
            if (result.isFail()) {
                return Result.fail(result.getError());
            }
            successes.add(result.get());
        }
        return Result.ok(successes);
    }

    @SafeVarargs
    public static <T> Result<List<T>> sequenceAll(Result<T>... results) {
        return sequenceAll(List.of(results));
    }

    // sequenceAll 使用公共逻辑
    public static <T> Result<List<T>> sequenceAll(List<Result<T>> results) {
        SplitResult<T> split = splitResults(results);

        if (!split.failures.isEmpty()) {
            return Result.fail(new MultiBusiness(split.failures));
        }
        return Result.ok(split.successes);
    }

    /**
     * 分区收集：同时获取成功和失败
     */
    public static <T> Partition<T> partition(List<Result<T>> results) {
        SplitResult<T> split = splitResults(results);
        return new Partition<>(split.successes, split.failures);
    }

    // 提取公共逻辑
    private static <T> SplitResult<T> splitResults(List<Result<T>> results) {
        List<T> successes = new ArrayList<>();
        List<Business> failures = new ArrayList<>();

        for (Result<T> result : results) {
            if (result.isSuccess()) {
                successes.add(result.get());
            } else {
                failures.add(result.getError());
            }
        }
        return new SplitResult<>(successes, failures);
    }

    /**
     * 只提取成功值
     */
    public static <T> List<T> successes(List<Result<T>> results) {
        return results.stream()
                .filter(Result::isSuccess)
                .map(Result::get)
                .toList();
    }

    /**
     * 只提取失败
     */
    public static <T> List<Business> failures(List<Result<T>> results) {
        return results.stream()
                .filter(Result::isFail)
                .map(Result::getError)
                .toList();
    }

    /**
     * 折叠：将 List<Result<T>> 合并为单个 Result，使用组合函数
     */
    public static <T> Result<T> fold(List<Result<T>> results, T identity, BiFunction<T, T, T> combiner) {
        Result<T> acc = Result.ok(identity);
        for (Result<T> result : results) {
            acc = acc.flatMap(a -> result.map(r -> combiner.apply(a, r)));
        }
        return acc;
    }

    /**
     * 归约：从第一个成功元素开始归约
     */
    public static <T> Result<T> reduce(List<Result<T>> results, BiFunction<T, T, T> combiner) {
        if (results.isEmpty()) {
            return Result.fail(ResponseCode.ILLEGAL_ARGUMENT, "Cannot reduce empty list");
        }
        return sequence(results).map(list -> {
            T acc = list.get(0);
            for (int i = 1; i < list.size(); i++) {
                acc = combiner.apply(acc, list.get(i));
            }
            return acc;
        });
    }

    // ==================== 遍历映射 ====================

    public static <T, R> Result<List<R>> traverse(List<T> list, Function<T, Result<R>> mapper) {
        List<R> results = new ArrayList<>();
        for (T item : list) {
            Result<R> result = mapper.apply(item);
            if (result.isFail()) {
                return Result.fail(result.getError());
            }
            results.add(result.get());
        }
        return Result.ok(results);
    }

    public static <T, R> Result<List<R>> traverseAll(List<T> list, Function<T, Result<R>> mapper) {
        List<R> successes = new ArrayList<>();
        List<Business> failures = new ArrayList<>();

        for (T item : list) {
            Result<R> result = mapper.apply(item);
            if (result.isSuccess()) {
                successes.add(result.get());
            } else {
                failures.add(result.getError());
            }
        }

        if (!failures.isEmpty()) {
            return Result.fail(new MultiBusiness(failures));
        }
        return Result.ok(successes);
    }

    /**
     * 带索引的遍历（快速失败）
     */
    public static <T, R> Result<List<R>> traverseIndexed(List<T> list, BiFunction<Integer, T, Result<R>> mapper) {
        List<R> results = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Result<R> result = mapper.apply(i, list.get(i));
            if (result.isFail()) {
                return Result.fail(result.getError());
            }
            results.add(result.get());
        }
        return Result.ok(results);
    }

    /**
     * 带索引的遍历（全量收集）
     */
    public static <T, R> Result<List<R>> traverseAllIndexed(List<T> list, BiFunction<Integer, T, Result<R>> mapper) {
        List<R> successes = new ArrayList<>();
        List<Business> failures = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Result<R> result = mapper.apply(i, list.get(i));
            if (result.isSuccess()) {
                successes.add(result.get());
            } else {
                failures.add(result.getError());
            }
        }

        if (!failures.isEmpty()) {
            return Result.fail(new MultiBusiness(failures));
        }
        return Result.ok(successes);
    }

    // ==================== 组合操作 ====================

    public static <T1, T2, R> Result<R> zip(Result<T1> r1, Result<T2> r2, BiFunction<T1, T2, R> combiner) {
        Result<R> fail = firstFailure(r1, r2);
        if (fail != null) return fail;

        return Result.ok(combiner.apply(r1.get(), r2.get()));
    }

    public static <T1, T2, T3, R> Result<R> zip(Result<T1> r1, Result<T2> r2, Result<T3> r3, Function3<T1, T2, T3, R> combiner) {
        Result<R> fail = firstFailure(r1, r2, r3);
        if (fail != null) return fail;

        return Result.ok(combiner.apply(r1.get(), r2.get(), r3.get()));
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    private static <T> Result<T> firstFailure(Result<?>... results) {
        for (Result<?> r : results) {
            if (r.isFail()) {
                return (Result<T>) r;
            }
        }
        return null;
    }

    /**
     * 组合 4 个 Result
     */
    public static <T1, T2, T3, T4, R> Result<R> zip(
            Result<T1> r1,
            Result<T2> r2,
            Result<T3> r3,
            Result<T4> r4,
            Function4<T1, T2, T3, T4, R> combiner) {

        if (r1.isFail()) return castFail(r1);
        if (r2.isFail()) return castFail(r2);
        if (r3.isFail()) return castFail(r3);
        if (r4.isFail()) return castFail(r4);

        return Result.ok(combiner.apply(r1.get(), r2.get(), r3.get(), r4.get()));
    }

    @SuppressWarnings("unchecked")
    private static <T, R> Result<R> castFail(Result<T> fail) {
        return (Result<R>) fail;
    }

    @FunctionalInterface
    public interface Function4<A, B, C, D, R> {
        R apply(A a, B b, C c, D d);
    }

    // ==================== 副作用 ====================

    public static <T> Result<T> tap(Result<T> result, Consumer<Result<T>> action) {
        action.accept(result);
        return result;
    }

    public static <T> Result<T> tapSuccess(Result<T> result, Consumer<T> action) {
        if (result.isSuccess()) {
            action.accept(result.get());
        }
        return result;
    }

    public static <T> Result<T> tapFailure(Result<T> result, Consumer<Business> action) {
        if (result.isFail()) {
            action.accept(result.getError());
        }
        return result;
    }

    /**
     * 异步执行副作用（不阻塞主流程）
     */
    public static <T> Result<T> tapAsync(Result<T> result, Consumer<Result<T>> action) {
        CompletableFuture.runAsync(() -> action.accept(result));
        return result;
    }

    // ==================== 验证 ====================

    public static <T> Result<T> ensure(Result<T> result, Predicate<T> predicate, ResponseCode errorCode) {
        return ensure(result, predicate, errorCode, null);
    }

    public static <T> Result<T> ensure(Result<T> result, Predicate<T> predicate, ResponseCode errorCode, String detail) {
        if (result.isFail()) {
            return result;
        }
        if (!predicate.test(result.get())) {
            return Result.fail(errorCode, detail);
        }
        return result;
    }

    // ==================== 其他工具 ====================

    public static <T> T getOrNull(Result<T> result) {
        return result.isSuccess() ? result.get() : null;
    }

    /**
     * 竞争执行：返回第一个成功的，或最后一个失败的结果（缓存结果避免重复执行）
     */
    @SafeVarargs
    public static <T> Result<T> race(Supplier<Result<T>>... suppliers) {
        Result<T> lastResult = null;
        for (Supplier<Result<T>> supplier : suppliers) {
            lastResult = supplier.get();
            if (lastResult.isSuccess()) {
                return lastResult;
            }
        }
        return lastResult != null ? lastResult : Result.ok(null);
    }

    /**
     * 重试机制：失败时重试指定次数
     */
    public static <T> Result<T> retry(int times, Supplier<Result<T>> supplier) {
        return retry(times, Duration.ZERO, supplier);
    }

    /**
     * 带延迟的重试
     */
    public static <T> Result<T> retry(int times, Duration delay, Supplier<Result<T>> supplier) {
        Result<T> lastResult = null;
        for (int i = 0; i < times; i++) {
            if (i > 0 && !delay.isZero()) {
                try {
                    Thread.sleep(delay.toMillis());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return Result.fail(ResponseCode.INTERRUPTED_ERROR, "Retry interrupted");
                }
            }
            lastResult = supplier.get();
            if (lastResult.isSuccess()) {
                return lastResult;
            }
        }
        return lastResult != null ? lastResult : Result.ok(null);
    }

    /**
     * 管道操作：按顺序执行，前一个成功才执行后一个
     */
    @SafeVarargs
    public static <T> Result<T> pipe(Result<T> initial, Function<T, Result<T>>... functions) {
        Result<T> current = initial;
        for (Function<T, Result<T>> fn : functions) {
            if (current.isFail()) {
                return current;
            }
            current = current.flatMap(fn);
        }
        return current;
    }

    // ====================== 延迟执行 ======================

    /**
     * 真正的延迟执行：Supplier 在第一次 get() 时才执行（线程安全）
     */
    public static <T> Supplier<Result<T>> defer(Supplier<Result<T>> supplier) {
        return new Supplier<>() {
            private volatile Result<T> result;
            private volatile boolean computed = false;

            @Override
            public Result<T> get() {
                if (!computed) {
                    synchronized (this) {
                        if (!computed) {
                            result = supplier.get();
                            computed = true;
                        }
                    }
                }
                return result;
            }
        };
    }

    /**
     * 延迟执行并自动记忆化（线程安全版本）
     */
    public static <T> Supplier<Result<T>> lazy(Supplier<Result<T>> supplier) {
        return defer(supplier);
    }

    /**
     * 记忆化：缓存 Supplier 的结果（非线程安全，性能更好）
     */
    public static <T> Supplier<Result<T>> memoize(Supplier<Result<T>> supplier) {
        return new Supplier<>() {
            private Result<T> cached;
            private boolean hasValue = false;

            @Override
            public Result<T> get() {
                if (!hasValue) {
                    cached = supplier.get();
                    hasValue = true;
                }
                return cached;
            }
        };
    }

    // ==================== 内部类 ====================

    public record Partition<T>(List<T> successes, List<Business> failures) {
        public Partition(List<T> successes, List<Business> failures) {
            this.successes = List.copyOf(successes);
            this.failures = List.copyOf(failures);
        }

        public boolean hasSuccesses() {
            return !successes.isEmpty();
        }

        public boolean hasFailures() {
            return !failures.isEmpty();
        }

        public boolean isAllSuccess() {
            return failures.isEmpty();
        }

        public boolean isAllFail() {
            return successes.isEmpty();
        }
    }

    @FunctionalInterface
    public interface Function3<A, B, C, R> {
        R apply(A a, B b, C c);
    }
}