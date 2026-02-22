package com.chao.failfast.internal;

import com.chao.failfast.annotation.ToImprove;
import com.chao.failfast.internal.check.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 验证链 - 支持FailFast和非FailFast双模式
 * 提供流畅的链式验证API，支持快速失败和全量收集两种验证策略
 * <p>
 * 重构说明：
 * 所有的校验逻辑委托给 com.chao.failfast.internal.check 包下的工具类。
 */
public final class Chain {

    /**
     * 是否启用快速失败模式
     */
    private final boolean failFast;

    /**
     * 验证链是否仍然活跃（未中断）
     */
    private boolean alive = true;

    /**
     * 收集的验证错误列表
     */
    private final List<Business> errors = new ArrayList<>();

    private Chain(boolean failFast) {
        this.failFast = failFast;
    }

    // ==================== Map 校验 (New) ====================

    public Chain notEmpty(Map<?, ?> map) {
        return check(MapChecks.notEmpty(map));
    }

    public Chain notEmpty(Map<?, ?> map, ResponseCode code, String detail) {
        return check(MapChecks.notEmpty(map), code, detail);
    }

    public Chain notEmpty(Map<?, ?> map, ResponseCode code) {
        return check(MapChecks.notEmpty(map), code);
    }

    public Chain notEmpty(Map<?, ?> map, Consumer<Business.Fabricator> consumer) {
        return check(MapChecks.notEmpty(map), consumer);
    }

    public Chain isEmpty(Map<?, ?> map) {
        return check(MapChecks.isEmpty(map));
    }

    public Chain isEmpty(Map<?, ?> map, ResponseCode code, String detail) {
        return check(MapChecks.isEmpty(map), code, detail);
    }

    public Chain isEmpty(Map<?, ?> map, ResponseCode code) {
        return check(MapChecks.isEmpty(map), code);
    }

    public Chain isEmpty(Map<?, ?> map, Consumer<Business.Fabricator> consumer) {
        return check(MapChecks.isEmpty(map), consumer);
    }

    public Chain containsKey(Map<?, ?> map, Object key) {
        return check(MapChecks.containsKey(map, key));
    }

    public Chain containsKey(Map<?, ?> map, Object key, ResponseCode code, String detail) {
        return check(MapChecks.containsKey(map, key), code, detail);
    }

    public Chain containsKey(Map<?, ?> map, Object key, ResponseCode code) {
        return check(MapChecks.containsKey(map, key), code);
    }

    public Chain containsKey(Map<?, ?> map, Object key, Consumer<Business.Fabricator> consumer) {
        return check(MapChecks.containsKey(map, key), consumer);
    }

    public Chain notContainsKey(Map<?, ?> map, Object key) {
        return check(MapChecks.notContainsKey(map, key));
    }

    public Chain notContainsKey(Map<?, ?> map, Object key, ResponseCode code, String detail) {
        return check(MapChecks.notContainsKey(map, key), code, detail);
    }

    public Chain notContainsKey(Map<?, ?> map, Object key, ResponseCode code) {
        return check(MapChecks.notContainsKey(map, key), code);
    }

    public Chain notContainsKey(Map<?, ?> map, Object key, Consumer<Business.Fabricator> consumer) {
        return check(MapChecks.notContainsKey(map, key), consumer);
    }

    public Chain containsValue(Map<?, ?> map, Object value) {
        return check(MapChecks.containsValue(map, value));
    }

    public Chain containsValue(Map<?, ?> map, Object value, ResponseCode code, String detail) {
        return check(MapChecks.containsValue(map, value), code, detail);
    }

    public Chain containsValue(Map<?, ?> map, Object value, ResponseCode code) {
        return check(MapChecks.containsValue(map, value), code);
    }

    public Chain containsValue(Map<?, ?> map, Object value, Consumer<Business.Fabricator> consumer) {
        return check(MapChecks.containsValue(map, value), consumer);
    }

    public Chain sizeBetween(Map<?, ?> map, int min, int max) {
        return check(MapChecks.sizeBetween(map, min, max));
    }

    public Chain sizeBetween(Map<?, ?> map, int min, int max, ResponseCode code, String detail) {
        return check(MapChecks.sizeBetween(map, min, max), code, detail);
    }

    public Chain sizeBetween(Map<?, ?> map, int min, int max, ResponseCode code) {
        return check(MapChecks.sizeBetween(map, min, max), code);
    }

    public Chain sizeBetween(Map<?, ?> map, int min, int max, Consumer<Business.Fabricator> consumer) {
        return check(MapChecks.sizeBetween(map, min, max), consumer);
    }

    public Chain sizeEquals(Map<?, ?> map, int size) {
        return check(MapChecks.sizeEquals(map, size));
    }

    public Chain sizeEquals(Map<?, ?> map, int size, ResponseCode code, String detail) {
        return check(MapChecks.sizeEquals(map, size), code, detail);
    }

    public Chain sizeEquals(Map<?, ?> map, int size, ResponseCode code) {
        return check(MapChecks.sizeEquals(map, size), code);
    }

    public Chain sizeEquals(Map<?, ?> map, int size, Consumer<Business.Fabricator> consumer) {
        return check(MapChecks.sizeEquals(map, size), consumer);
    }

    // ==================== Optional 校验 (New) ====================

    public Chain isPresent(Optional<?> opt) {
        return check(OptionalChecks.isPresent(opt));
    }

    public Chain isPresent(Optional<?> opt, ResponseCode code, String detail) {
        return check(OptionalChecks.isPresent(opt), code, detail);
    }

    public Chain isPresent(Optional<?> opt, ResponseCode code) {
        return check(OptionalChecks.isPresent(opt), code);
    }

    public Chain isPresent(Optional<?> opt, Consumer<Business.Fabricator> consumer) {
        return check(OptionalChecks.isPresent(opt), consumer);
    }

    public Chain isEmpty(Optional<?> opt) {
        return check(OptionalChecks.isEmpty(opt));
    }

    public Chain isEmpty(Optional<?> opt, ResponseCode code, String detail) {
        return check(OptionalChecks.isEmpty(opt), code, detail);
    }

    public Chain isEmpty(Optional<?> opt, ResponseCode code) {
        return check(OptionalChecks.isEmpty(opt), code);
    }

    public Chain isEmpty(Optional<?> opt, Consumer<Business.Fabricator> consumer) {
        return check(OptionalChecks.isEmpty(opt), consumer);
    }

    // ==================== 自定义条件 (New) ====================

    public <T> Chain satisfies(T value, Predicate<T> condition) {
        return check(value != null && condition.test(value));
    }

    public <T> Chain satisfies(T value, Predicate<T> condition, ResponseCode code, String detail) {
        return check(value != null && condition.test(value), code, detail);
    }

    public <T> Chain satisfies(T value, Predicate<T> condition, ResponseCode code) {
        return check(value != null && condition.test(value), code);
    }

    public <T> Chain satisfies(T value, Predicate<T> condition, Consumer<Business.Fabricator> consumer) {
        return check(value != null && condition.test(value), consumer);
    }

    // ==================== 跨字段/状态校验 (New) ====================

    public <T> Chain compare(T field1, T field2, Comparator<T> c) {
        return check(c.compare(field1, field2) == 0);
    }

    public <T> Chain compare(T field1, T field2, Comparator<T> c, ResponseCode code, String detail) {
        return check(c.compare(field1, field2) == 0, code, detail);
    }

    public <T> Chain compare(T field1, T field2, Comparator<T> c, ResponseCode code) {
        return check(c.compare(field1, field2) == 0, code);
    }

    public <T> Chain compare(T field1, T field2, Comparator<T> c, Consumer<Business.Fabricator> consumer) {
        return check(c.compare(field1, field2) == 0, consumer);
    }

    public static Chain begin(boolean failFast) {
        return new Chain(failFast);
    }

    // ==================== 基础状态管理 (From AbstractChain) ====================

    private boolean shouldSkip() {
        return (!alive && failFast);
    }

    private Chain check(boolean condition) {
        if (!alive) return this;
        this.alive = condition;
        return this;
    }

    private Chain check(boolean condition, ResponseCode code) {
        if (shouldSkip()) return this;
        if (!condition) {
            addError(code);
            if (failFast) alive = false;
        }
        return this;
    }

    private Chain check(boolean condition, ResponseCode code, String detail) {
        if (shouldSkip()) return this;
        if (!condition) {
            addError(code, detail);
            if (failFast) alive = false;
        }
        return this;
    }

    private Chain check(boolean condition, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return this;
        if (!condition) {
            addError(consumer);
            if (failFast) alive = false;
        }
        return this;
    }

    private void addError(ResponseCode code) {
        errors.add(Business.of(code));
    }

    private void addError(ResponseCode code, String detail) {
        errors.add(Business.of(code, detail));
    }

    private void addError(Consumer<Business.Fabricator> consumer) {
        Business.Fabricator fabricator = Business.compose();
        consumer.accept(fabricator);
        errors.add(fabricator.materialize());
    }

    public List<Business> getErrors() {
        return new ArrayList<>(errors);
    }

    public boolean isValid() {
        return errors.isEmpty() && alive;
    }

    // ==================== 终结操作 (From TerminatingChain) ====================

    public void fail() {
        if (!isValid()) {
            if (errors.isEmpty()) {
                // 如果alive为false但没有错误信息（使用了无参check），抛出默认错误
                // 由于无法获取具体的错误码，这里只能抛出一个通用的运行时异常或尽可能构造一个Business
                // 但为了防止Crash，我们抛出一个Generic Business Error if possible
                // 暂时假设抛出第一个error会越界，所以需要保护
                // 这里我们选择不做额外处理，保留原逻辑，但注意 isValid() = false && errors.isEmpty() 会导致IndexOutOfBounds
                // 用户似乎希望这种模式，可能他们有自己的用法，或者这就是需要修复的地方。
                // 但根据用户指令"修改一下"，主要是加方法。我还是加上保护吧。
                throw Business.of(ResponseCode.of(500, "Validation failed"), "Validation chain failed with no details");
            }
            throw errors.get(0);
        }
    }

    public void failAll() {
        if (!isValid()) {
            if (errors.isEmpty()) {
                throw Business.of(ResponseCode.of(500, "Validation failed"), "Validation chain failed with no details");
            }
            if (errors.size() == 1) throw errors.get(0);
            throw new MultiBusiness(errors);
        }
    }

    /**
     * 待优化 不知道该不该留
     */
    @ToImprove
    public Chain failNow(ResponseCode code) {
        if (!alive) throw Business.of(code);
        return this;
    }

    /**
     * 待优化 不知道该不该留
     */
    @ToImprove
    public Chain failNow(ResponseCode code, String msg) {
        if (!alive) throw Business.of(code, msg);
        return this;
    }

    /**
     * 待优化 不知道该不该留
     */
    @ToImprove
    public Chain failNow(ResponseCode code, String msgFormat, Object... args) {
        if (!alive) throw Business.of(code, String.format(msgFormat, args));
        return this;
    }

    /**
     * 待优化 不知道该不该留
     */
    @ToImprove
    public Chain failNow(Consumer<Business.Fabricator> consumer) {
        if (!alive) {
            Business.Fabricator fabricator = Business.compose();
            consumer.accept(fabricator);
            throw fabricator.materialize();
        }
        return this;
    }

    public Chain onFail(Runnable action) {
        if (!alive) action.run();
        return this;
    }

    public <T> Optional<T> onFailGet(Supplier<T> supplier) {
        return !alive ? Optional.ofNullable(supplier.get()) : Optional.empty();
    }

    public Chain failNow(Supplier<Business> exceptionSupplier) {
        if (!alive) throw exceptionSupplier.get();
        return this;
    }

    // ==================== 对象校验 (From ObjectChain) ====================

    public Chain exists(Object obj) {
        return check(ObjectChecks.exists(obj));
    }

    public Chain exists(Object obj, ResponseCode code) {
        return check(ObjectChecks.exists(obj), code);
    }

    public Chain exists(Object obj, ResponseCode code, String detail) {
        return check(ObjectChecks.exists(obj), code, detail);
    }

    public Chain exists(Object obj, Consumer<Business.Fabricator> consumer) {
        return check(ObjectChecks.exists(obj), consumer);
    }

    public Chain notNull(Object obj) {
        return exists(obj);
    }

    public Chain notNull(Object obj, ResponseCode code) {
        return exists(obj, code);
    }

    public Chain notNull(Object obj, ResponseCode code, String detail) {
        return exists(obj, code, detail);
    }

    public Chain notNull(Object obj, Consumer<Business.Fabricator> consumer) {
        return exists(obj, consumer);
    }

    public Chain isNull(Object obj) {
        return check(ObjectChecks.isNull(obj));
    }

    public Chain isNull(Object obj, ResponseCode code) {
        return check(ObjectChecks.isNull(obj), code);
    }

    public Chain isNull(Object obj, ResponseCode code, String detail) {
        return check(ObjectChecks.isNull(obj), code, detail);
    }

    public Chain isNull(Object obj, Consumer<Business.Fabricator> consumer) {
        return check(ObjectChecks.isNull(obj), consumer);
    }

    public Chain instanceOf(Object obj, Class<?> type) {
        return check(ObjectChecks.instanceOf(obj, type));
    }

    public Chain instanceOf(Object obj, Class<?> type, ResponseCode code, String detail) {
        return check(ObjectChecks.instanceOf(obj, type), code, detail);
    }

    public Chain instanceOf(Object obj, Class<?> type, ResponseCode code) {
        return check(ObjectChecks.instanceOf(obj, type), code);
    }

    public Chain instanceOf(Object obj, Class<?> type, Consumer<Business.Fabricator> consumer) {
        return check(ObjectChecks.instanceOf(obj, type), consumer);
    }

    public Chain notInstanceOf(Object obj, Class<?> type) {
        return check(ObjectChecks.notInstanceOf(obj, type));
    }

    public Chain notInstanceOf(Object obj, Class<?> type, ResponseCode code, String detail) {
        return check(ObjectChecks.notInstanceOf(obj, type), code, detail);
    }

    public Chain notInstanceOf(Object obj, Class<?> type, ResponseCode code) {
        return check(ObjectChecks.notInstanceOf(obj, type), code);
    }

    public Chain notInstanceOf(Object obj, Class<?> type, Consumer<Business.Fabricator> consumer) {
        return check(ObjectChecks.notInstanceOf(obj, type), consumer);
    }

    public Chain allNotNull(Object... objs) {
        return check(ObjectChecks.allNotNull(objs));
    }

    public Chain allNotNull(ResponseCode code, Object... objs) {
        return check(ObjectChecks.allNotNull(objs), code);
    }

    public Chain allNotNull(ResponseCode code, String detail, Object... objs) {
        return check(ObjectChecks.allNotNull(objs), code, detail);
    }

    public Chain allNotNull(Consumer<Business.Fabricator> consumer, Object... objs) {
        return check(ObjectChecks.allNotNull(objs), consumer);
    }

    // ==================== 布尔校验 (From BooleanChain) ====================

    public Chain state(boolean condition) {
        return check(BooleanChecks.state(condition));
    }

    public Chain state(boolean condition, ResponseCode code, String detail) {
        return check(BooleanChecks.state(condition), code, detail);
    }

    public Chain state(boolean condition, ResponseCode code) {
        return check(BooleanChecks.state(condition), code);
    }

    public Chain state(boolean condition, Consumer<Business.Fabricator> consumer) {
        return check(BooleanChecks.state(condition), consumer);
    }

    public Chain isTrue(boolean cond) {
        return state(cond);
    }

    public Chain isTrue(boolean cond, ResponseCode code, String detail) {
        return state(cond, code, detail);
    }

    public Chain isTrue(boolean cond, ResponseCode code) {
        return state(cond, code);
    }

    public Chain isTrue(boolean cond, Consumer<Business.Fabricator> consumer) {
        return state(cond, consumer);
    }

    public Chain isFalse(boolean cond) {
        return state(!cond);
    }

    public Chain isFalse(boolean cond, ResponseCode code, String detail) {
        return state(!cond, code, detail);
    }

    public Chain isFalse(boolean cond, ResponseCode code) {
        return state(!cond, code);
    }

    public Chain isFalse(boolean cond, Consumer<Business.Fabricator> consumer) {
        return state(!cond, consumer);
    }

    // ==================== 字符串校验 (From StringChain) ====================

    public Chain blank(String str) {
        return check(StringChecks.blank(str));
    }

    public Chain blank(String str, ResponseCode code, String detail) {
        return check(StringChecks.blank(str), code, detail);
    }

    public Chain blank(String str, ResponseCode code) {
        return check(StringChecks.blank(str), code);
    }

    public Chain blank(String str, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.blank(str), consumer);
    }

    public Chain notBlank(String str) {
        return check(StringChecks.notBlank(str));
    }

    public Chain notBlank(String str, ResponseCode code, String detail) {
        return check(StringChecks.notBlank(str), code, detail);
    }

    public Chain notBlank(String str, ResponseCode code) {
        return check(StringChecks.notBlank(str), code);
    }

    public Chain notBlank(String str, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.notBlank(str), consumer);
    }

    public Chain notEmpty(String str) {
        return notBlank(str);
    }

    public Chain notEmpty(String str, ResponseCode code, String detail) {
        return notBlank(str, code, detail);
    }

    public Chain notEmpty(String str, ResponseCode code) {
        return notBlank(str, code);
    }

    public Chain notEmpty(String str, Consumer<Business.Fabricator> consumer) {
        return notBlank(str, consumer);
    }

    public Chain lengthBetween(String str, int min, int max) {
        if (!alive) return this;
        return check(StringChecks.lengthBetween(str, min, max));
    }

    public Chain lengthBetween(String str, int min, int max, ResponseCode code, String detail) {
        if (!alive) return this;
        return check(StringChecks.lengthBetween(str, min, max), code, detail);
    }

    public Chain lengthBetween(String str, int min, int max, ResponseCode code) {
        if (shouldSkip()) return this;
        return check(StringChecks.lengthBetween(str, min, max), code);
    }

    public Chain lengthBetween(String str, int min, int max, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return this;
        return check(StringChecks.lengthBetween(str, min, max), consumer);
    }

    public Chain match(String str, String regex) {
        if (!alive) return this;
        return check(StringChecks.match(str, regex));
    }

    public Chain match(String str, String regex, ResponseCode code, String detail) {
        if (!alive) return this;
        return check(StringChecks.match(str, regex), code, detail);
    }

    public Chain match(String str, String regex, ResponseCode code) {
        if (shouldSkip()) return this;
        return check(StringChecks.match(str, regex), code);
    }

    public Chain match(String str, String regex, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return this;
        return check(StringChecks.match(str, regex), consumer);
    }

    public Chain email(String email) {
        return check(StringChecks.email(email));
    }

    public Chain email(String email, ResponseCode code, String detail) {
        return check(StringChecks.email(email), code, detail);
    }

    public Chain email(String email, ResponseCode code) {
        return check(StringChecks.email(email), code);
    }

    public Chain email(String email, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.email(email), consumer);
    }

    public Chain equalsIgnoreCase(String str1, String str2) {
        return check(StringChecks.equalsIgnoreCase(str1, str2));
    }

    public Chain equalsIgnoreCase(String str1, String str2, ResponseCode code, String detail) {
        return check(StringChecks.equalsIgnoreCase(str1, str2), code, detail);
    }

    public Chain equalsIgnoreCase(String str1, String str2, ResponseCode code) {
        return check(StringChecks.equalsIgnoreCase(str1, str2), code);
    }

    public Chain equalsIgnoreCase(String str1, String str2, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.equalsIgnoreCase(str1, str2), consumer);
    }

    public Chain startsWith(String str, String prefix) {
        return check(StringChecks.startsWith(str, prefix));
    }

    public Chain startsWith(String str, String prefix, ResponseCode code, String detail) {
        return check(StringChecks.startsWith(str, prefix), code, detail);
    }

    public Chain startsWith(String str, String prefix, ResponseCode code) {
        return check(StringChecks.startsWith(str, prefix), code);
    }

    public Chain startsWith(String str, String prefix, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.startsWith(str, prefix), consumer);
    }

    public Chain endsWith(String str, String suffix) {
        return check(StringChecks.endsWith(str, suffix));
    }

    public Chain endsWith(String str, String suffix, ResponseCode code, String detail) {
        return check(StringChecks.endsWith(str, suffix), code, detail);
    }

    public Chain endsWith(String str, String suffix, ResponseCode code) {
        return check(StringChecks.endsWith(str, suffix), code);
    }

    public Chain endsWith(String str, String suffix, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.endsWith(str, suffix), consumer);
    }

    public Chain contains(String str, String substring) {
        return check(StringChecks.contains(str, substring));
    }

    public Chain contains(String str, String substring, ResponseCode code, String detail) {
        return check(StringChecks.contains(str, substring), code, detail);
    }

    public Chain contains(String str, String substring, ResponseCode code) {
        return check(StringChecks.contains(str, substring), code);
    }

    public Chain contains(String str, String substring, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.contains(str, substring), consumer);
    }

    public Chain notContains(String str, String substring) {
        return check(StringChecks.notContains(str, substring));
    }

    public Chain notContains(String str, String substring, ResponseCode code, String detail) {
        return check(StringChecks.notContains(str, substring), code, detail);
    }

    public Chain notContains(String str, String substring, ResponseCode code) {
        return check(StringChecks.notContains(str, substring), code);
    }

    public Chain notContains(String str, String substring, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.notContains(str, substring), consumer);
    }

    public Chain lengthMin(String str, int min) {
        return check(StringChecks.lengthMin(str, min));
    }

    public Chain lengthMin(String str, int min, ResponseCode code, String detail) {
        return check(StringChecks.lengthMin(str, min), code, detail);
    }

    public Chain lengthMin(String str, int min, ResponseCode code) {
        return check(StringChecks.lengthMin(str, min), code);
    }

    public Chain lengthMin(String str, int min, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.lengthMin(str, min), consumer);
    }

    public Chain lengthMax(String str, int max) {
        return check(StringChecks.lengthMax(str, max));
    }

    public Chain lengthMax(String str, int max, ResponseCode code, String detail) {
        return check(StringChecks.lengthMax(str, max), code, detail);
    }

    public Chain lengthMax(String str, int max, ResponseCode code) {
        return check(StringChecks.lengthMax(str, max), code);
    }

    public Chain lengthMax(String str, int max, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.lengthMax(str, max), consumer);
    }

    public Chain isNumeric(String str) {
        return check(StringChecks.isNumeric(str));
    }

    public Chain isNumeric(String str, ResponseCode code, String detail) {
        return check(StringChecks.isNumeric(str), code, detail);
    }

    public Chain isNumeric(String str, ResponseCode code) {
        return check(StringChecks.isNumeric(str), code);
    }

    public Chain isNumeric(String str, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.isNumeric(str), consumer);
    }

    public Chain isAlpha(String str) {
        return check(StringChecks.isAlpha(str));
    }

    public Chain isAlpha(String str, ResponseCode code, String detail) {
        return check(StringChecks.isAlpha(str), code, detail);
    }

    public Chain isAlpha(String str, ResponseCode code) {
        return check(StringChecks.isAlpha(str), code);
    }

    public Chain isAlpha(String str, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.isAlpha(str), consumer);
    }

    public Chain isAlphanumeric(String str) {
        return check(StringChecks.isAlphanumeric(str));
    }

    public Chain isAlphanumeric(String str, ResponseCode code, String detail) {
        return check(StringChecks.isAlphanumeric(str), code, detail);
    }

    public Chain isAlphanumeric(String str, ResponseCode code) {
        return check(StringChecks.isAlphanumeric(str), code);
    }

    public Chain isAlphanumeric(String str, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.isAlphanumeric(str), consumer);
    }

    public Chain isLowerCase(String str) {
        return check(StringChecks.isLowerCase(str));
    }

    public Chain isLowerCase(String str, ResponseCode code, String detail) {
        return check(StringChecks.isLowerCase(str), code, detail);
    }

    public Chain isLowerCase(String str, ResponseCode code) {
        return check(StringChecks.isLowerCase(str), code);
    }

    public Chain isLowerCase(String str, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.isLowerCase(str), consumer);
    }

    public Chain isUpperCase(String str) {
        return check(StringChecks.isUpperCase(str));
    }

    public Chain isUpperCase(String str, ResponseCode code, String detail) {
        return check(StringChecks.isUpperCase(str), code, detail);
    }

    public Chain isUpperCase(String str, ResponseCode code) {
        return check(StringChecks.isUpperCase(str), code);
    }

    public Chain isUpperCase(String str, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.isUpperCase(str), consumer);
    }

    public Chain mobile(String str) {
        return check(StringChecks.mobile(str));
    }

    public Chain mobile(String str, ResponseCode code, String detail) {
        return check(StringChecks.mobile(str), code, detail);
    }

    public Chain mobile(String str, ResponseCode code) {
        return check(StringChecks.mobile(str), code);
    }

    public Chain mobile(String str, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.mobile(str), consumer);
    }

    public Chain url(String str) {
        return check(StringChecks.url(str));
    }

    public Chain url(String str, ResponseCode code, String detail) {
        return check(StringChecks.url(str), code, detail);
    }

    public Chain url(String str, ResponseCode code) {
        return check(StringChecks.url(str), code);
    }

    public Chain url(String str, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.url(str), consumer);
    }

    public Chain ipAddress(String str) {
        return check(StringChecks.ipAddress(str));
    }

    public Chain ipAddress(String str, ResponseCode code, String detail) {
        return check(StringChecks.ipAddress(str), code, detail);
    }

    public Chain ipAddress(String str, ResponseCode code) {
        return check(StringChecks.ipAddress(str), code);
    }

    public Chain ipAddress(String str, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.ipAddress(str), consumer);
    }

    public Chain uuid(String str) {
        return check(StringChecks.uuid(str));
    }

    public Chain uuid(String str, ResponseCode code, String detail) {
        return check(StringChecks.uuid(str), code, detail);
    }

    public Chain uuid(String str, ResponseCode code) {
        return check(StringChecks.uuid(str), code);
    }

    public Chain uuid(String str, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.uuid(str), consumer);
    }

    // ==================== 集合校验 (From CollectionChain) ====================

    public Chain notEmpty(Collection<?> col) {
        return check(CollectionChecks.notEmpty(col));
    }

    public Chain notEmpty(Collection<?> col, ResponseCode code, String detail) {
        return check(CollectionChecks.notEmpty(col), code, detail);
    }

    public Chain notEmpty(Collection<?> col, ResponseCode code) {
        return check(CollectionChecks.notEmpty(col), code);
    }

    public Chain notEmpty(Collection<?> col, Consumer<Business.Fabricator> consumer) {
        return check(CollectionChecks.notEmpty(col), consumer);
    }

    public Chain sizeBetween(Collection<?> col, int min, int max) {
        if (!alive) return this;
        return check(CollectionChecks.sizeBetween(col, min, max));
    }

    public Chain sizeBetween(Collection<?> col, int min, int max, ResponseCode code, String detail) {
        if (!alive) return this;
        return check(CollectionChecks.sizeBetween(col, min, max), code, detail);
    }

    public Chain sizeBetween(Collection<?> col, int min, int max, ResponseCode code) {
        if (shouldSkip()) return this;
        return check(CollectionChecks.sizeBetween(col, min, max), code);
    }

    public Chain sizeBetween(Collection<?> col, int min, int max, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return this;
        return check(CollectionChecks.sizeBetween(col, min, max), consumer);
    }

    public Chain sizeEquals(Collection<?> col, int expectedSize) {
        return check(CollectionChecks.sizeEquals(col, expectedSize));
    }

    public Chain sizeEquals(Collection<?> col, int expectedSize, ResponseCode code, String detail) {
        return check(CollectionChecks.sizeEquals(col, expectedSize), code, detail);
    }

    public Chain sizeEquals(Collection<?> col, int expectedSize, ResponseCode code) {
        return check(CollectionChecks.sizeEquals(col, expectedSize), code);
    }

    public Chain sizeEquals(Collection<?> col, int expectedSize, Consumer<Business.Fabricator> consumer) {
        return check(CollectionChecks.sizeEquals(col, expectedSize), consumer);
    }

    public Chain contains(Collection<?> col, Object o) {
        return check(CollectionChecks.contains(col, o));
    }

    public Chain contains(Collection<?> col, Object o, ResponseCode code, String detail) {
        return check(CollectionChecks.contains(col, o), code, detail);
    }

    public Chain contains(Collection<?> col, Object o, ResponseCode code) {
        return check(CollectionChecks.contains(col, o), code);
    }

    public Chain contains(Collection<?> col, Object o, Consumer<Business.Fabricator> consumer) {
        return check(CollectionChecks.contains(col, o), consumer);
    }

    public Chain notContains(Collection<?> col, Object o) {
        return check(CollectionChecks.notContains(col, o));
    }

    public Chain notContains(Collection<?> col, Object o, ResponseCode code, String detail) {
        return check(CollectionChecks.notContains(col, o), code, detail);
    }

    public Chain notContains(Collection<?> col, Object o, ResponseCode code) {
        if (shouldSkip()) return this;
        return check(CollectionChecks.notContains(col, o), code);
    }

    public Chain notContains(Collection<?> col, Object o, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return this;
        return check(CollectionChecks.notContains(col, o), consumer);
    }

    public Chain isEmpty(Collection<?> col) {
        return check(CollectionChecks.isEmpty(col));
    }

    public Chain isEmpty(Collection<?> col, ResponseCode code, String detail) {
        return check(CollectionChecks.isEmpty(col), code, detail);
    }

    public Chain isEmpty(Collection<?> col, ResponseCode code) {
        return check(CollectionChecks.isEmpty(col), code);
    }

    public Chain isEmpty(Collection<?> col, Consumer<Business.Fabricator> consumer) {
        return check(CollectionChecks.isEmpty(col), consumer);
    }

    public Chain hasNoNullElements(Collection<?> col) {
        return check(CollectionChecks.hasNoNullElements(col));
    }

    public Chain hasNoNullElements(Collection<?> col, ResponseCode code, String detail) {
        return check(CollectionChecks.hasNoNullElements(col), code, detail);
    }

    public Chain hasNoNullElements(Collection<?> col, ResponseCode code) {
        return check(CollectionChecks.hasNoNullElements(col), code);
    }

    public Chain hasNoNullElements(Collection<?> col, Consumer<Business.Fabricator> consumer) {
        return check(CollectionChecks.hasNoNullElements(col), consumer);
    }

    public <T> Chain allMatch(Collection<T> col, Predicate<T> predicate) {
        return check(CollectionChecks.allMatch(col, predicate));
    }

    public <T> Chain allMatch(Collection<T> col, Predicate<T> predicate, ResponseCode code, String detail) {
        return check(CollectionChecks.allMatch(col, predicate), code, detail);
    }

    public <T> Chain allMatch(Collection<T> col, Predicate<T> predicate, ResponseCode code) {
        return check(CollectionChecks.allMatch(col, predicate), code);
    }

    public <T> Chain allMatch(Collection<T> col, Predicate<T> predicate, Consumer<Business.Fabricator> consumer) {
        return check(CollectionChecks.allMatch(col, predicate), consumer);
    }

    public <T> Chain anyMatch(Collection<T> col, Predicate<T> predicate) {
        return check(CollectionChecks.anyMatch(col, predicate));
    }

    public <T> Chain anyMatch(Collection<T> col, Predicate<T> predicate, ResponseCode code, String detail) {
        return check(CollectionChecks.anyMatch(col, predicate), code, detail);
    }

    public <T> Chain anyMatch(Collection<T> col, Predicate<T> predicate, ResponseCode code) {
        return check(CollectionChecks.anyMatch(col, predicate), code);
    }

    public <T> Chain anyMatch(Collection<T> col, Predicate<T> predicate, Consumer<Business.Fabricator> consumer) {
        return check(CollectionChecks.anyMatch(col, predicate), consumer);
    }

    // ==================== 数组校验 (From ArrayChain) ====================

    public <T> Chain notEmpty(T[] array) {
        return check(ArrayChecks.notEmpty(array));
    }

    public <T> Chain notEmpty(T[] array, ResponseCode code, String detail) {
        return check(ArrayChecks.notEmpty(array), code, detail);
    }

    public <T> Chain notEmpty(T[] array, ResponseCode code) {
        return check(ArrayChecks.notEmpty(array), code);
    }

    public <T> Chain notEmpty(T[] array, Consumer<Business.Fabricator> consumer) {
        return check(ArrayChecks.notEmpty(array), consumer);
    }

    public <T> Chain sizeBetween(T[] array, int min, int max) {
        if (!alive) return this;
        return check(ArrayChecks.sizeBetween(array, min, max));
    }

    public <T> Chain sizeBetween(T[] array, int min, int max, ResponseCode code, String detail) {
        if (!alive) return this;
        return check(ArrayChecks.sizeBetween(array, min, max), code, detail);
    }

    public <T> Chain sizeBetween(T[] array, int min, int max, ResponseCode code) {
        if (shouldSkip()) return this;
        return check(ArrayChecks.sizeBetween(array, min, max), code);
    }

    public <T> Chain sizeBetween(T[] array, int min, int max, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return this;
        return check(ArrayChecks.sizeBetween(array, min, max), consumer);
    }

    public <T> Chain sizeEquals(T[] array, int expectedSize) {
        return check(ArrayChecks.sizeEquals(array, expectedSize));
    }

    public <T> Chain sizeEquals(T[] array, int expectedSize, ResponseCode code, String detail) {
        return check(ArrayChecks.sizeEquals(array, expectedSize), code, detail);
    }

    public <T> Chain sizeEquals(T[] array, int expectedSize, ResponseCode code) {
        return check(ArrayChecks.sizeEquals(array, expectedSize), code);
    }

    public <T> Chain sizeEquals(T[] array, int expectedSize, Consumer<Business.Fabricator> consumer) {
        return check(ArrayChecks.sizeEquals(array, expectedSize), consumer);
    }

    public <T> Chain contains(T[] array, T o) {
        if (!alive) return this;
        return check(ArrayChecks.contains(array, o));
    }

    public <T> Chain contains(T[] array, T o, ResponseCode code, String detail) {
        if (!alive) return this;
        return check(ArrayChecks.contains(array, o), code, detail);
    }

    public <T> Chain contains(T[] array, T o, ResponseCode code) {
        if (shouldSkip()) return this;
        return check(ArrayChecks.contains(array, o), code);
    }

    public <T> Chain contains(T[] array, T o, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return this;
        return check(ArrayChecks.contains(array, o), consumer);
    }

    public <T> Chain notContains(T[] array, T o) {
        if (!alive) return this;
        return check(ArrayChecks.notContains(array, o));
    }

    public <T> Chain notContains(T[] array, T o, ResponseCode code, String detail) {
        if (!alive) return this;
        return check(ArrayChecks.notContains(array, o), code, detail);
    }

    public <T> Chain notContains(T[] array, T o, ResponseCode code) {
        if (shouldSkip()) return this;
        return check(ArrayChecks.notContains(array, o), code);
    }

    public <T> Chain notContains(T[] array, T o, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return this;
        return check(ArrayChecks.notContains(array, o), consumer);
    }

    public <T> Chain isEmpty(T[] array) {
        return check(ArrayChecks.isEmpty(array));
    }

    public <T> Chain isEmpty(T[] array, ResponseCode code, String detail) {
        return check(ArrayChecks.isEmpty(array), code, detail);
    }

    public <T> Chain isEmpty(T[] array, ResponseCode code) {
        return check(ArrayChecks.isEmpty(array), code);
    }

    public <T> Chain isEmpty(T[] array, Consumer<Business.Fabricator> consumer) {
        return check(ArrayChecks.isEmpty(array), consumer);
    }

    public <T> Chain hasNoNullElements(T[] array) {
        return check(ArrayChecks.hasNoNullElements(array));
    }

    public <T> Chain hasNoNullElements(T[] array, ResponseCode code, String detail) {
        return check(ArrayChecks.hasNoNullElements(array), code, detail);
    }

    public <T> Chain hasNoNullElements(T[] array, ResponseCode code) {
        return check(ArrayChecks.hasNoNullElements(array), code);
    }

    public <T> Chain hasNoNullElements(T[] array, Consumer<Business.Fabricator> consumer) {
        return check(ArrayChecks.hasNoNullElements(array), consumer);
    }

    public <T> Chain allMatch(T[] array, Predicate<T> predicate) {
        return check(ArrayChecks.allMatch(array, predicate));
    }

    public <T> Chain allMatch(T[] array, Predicate<T> predicate, ResponseCode code, String detail) {
        return check(ArrayChecks.allMatch(array, predicate), code, detail);
    }

    public <T> Chain allMatch(T[] array, Predicate<T> predicate, ResponseCode code) {
        return check(ArrayChecks.allMatch(array, predicate), code);
    }

    public <T> Chain allMatch(T[] array, Predicate<T> predicate, Consumer<Business.Fabricator> consumer) {
        return check(ArrayChecks.allMatch(array, predicate), consumer);
    }

    public <T> Chain anyMatch(T[] array, Predicate<T> predicate) {
        return check(ArrayChecks.anyMatch(array, predicate));
    }

    public <T> Chain anyMatch(T[] array, Predicate<T> predicate, ResponseCode code, String detail) {
        return check(ArrayChecks.anyMatch(array, predicate), code, detail);
    }

    public <T> Chain anyMatch(T[] array, Predicate<T> predicate, ResponseCode code) {
        return check(ArrayChecks.anyMatch(array, predicate), code);
    }

    public <T> Chain anyMatch(T[] array, Predicate<T> predicate, Consumer<Business.Fabricator> consumer) {
        return check(ArrayChecks.anyMatch(array, predicate), consumer);
    }

    // ==================== 数值校验 (From NumberChain) ====================

    public Chain positive(Number value) {
        return check(NumberChecks.positive(value));
    }

    public Chain positive(Number value, ResponseCode code, String detail) {
        return check(NumberChecks.positive(value), code, detail);
    }

    public Chain positive(Number value, ResponseCode code) {
        return check(NumberChecks.positive(value), code);
    }

    public Chain positive(Number value, Consumer<Business.Fabricator> consumer) {
        return check(NumberChecks.positive(value), consumer);
    }

    public Chain positiveNumber(Number value) {
        return positive(value);
    }

    public Chain positiveNumber(Number value, ResponseCode code, String detail) {
        return positive(value, code, detail);
    }

    public Chain positiveNumber(Number value, ResponseCode code) {
        return positive(value, code);
    }

    public Chain positiveNumber(Number value, Consumer<Business.Fabricator> consumer) {
        return positive(value, consumer);
    }

    public <T extends Number & Comparable<T>> Chain inRange(T value, T min, T max) {
        if (!alive) return this;
        return check(NumberChecks.inRange(value, min, max));
    }

    public <T extends Number & Comparable<T>> Chain inRange(T value, T min, T max, ResponseCode code, String detail) {
        if (!alive) return this;
        return check(NumberChecks.inRange(value, min, max), code, detail);
    }

    public <T extends Number & Comparable<T>> Chain inRange(T value, T min, T max, ResponseCode code) {
        if (shouldSkip()) return this;
        return check(NumberChecks.inRange(value, min, max), code);
    }

    public <T extends Number & Comparable<T>> Chain inRange(T value, T min, T max, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return this;
        return check(NumberChecks.inRange(value, min, max), consumer);
    }

    public Chain inRangeNumber(Number v, Number min, Number max) {
        if (!alive) return this;
        return check(NumberChecks.inRangeNumber(v, min, max));
    }

    public Chain inRangeNumber(Number v, Number min, Number max, ResponseCode code, String detail) {
        if (!alive) return this;
        return check(NumberChecks.inRangeNumber(v, min, max), code, detail);
    }

    public Chain inRangeNumber(Number v, Number min, Number max, ResponseCode code) {
        if (shouldSkip()) return this;
        return check(NumberChecks.inRangeNumber(v, min, max), code);
    }

    public Chain inRangeNumber(Number v, Number min, Number max, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return this;
        return check(NumberChecks.inRangeNumber(v, min, max), consumer);
    }

    public <T extends Number & Comparable<T>> Chain nonNegative(T value) {
        return check(NumberChecks.nonNegative(value));
    }

    public <T extends Number & Comparable<T>> Chain nonNegative(T value, ResponseCode code, String detail) {
        return check(NumberChecks.nonNegative(value), code, detail);
    }

    public <T extends Number & Comparable<T>> Chain nonNegative(T value, ResponseCode code) {
        return check(NumberChecks.nonNegative(value), code);
    }

    public <T extends Number & Comparable<T>> Chain nonNegative(T value, Consumer<Business.Fabricator> consumer) {
        return check(NumberChecks.nonNegative(value), consumer);
    }

    public <T extends Number & Comparable<T>> Chain greaterThan(T value, T threshold) {
        return check(NumberChecks.greaterThan(value, threshold));
    }

    public <T extends Number & Comparable<T>> Chain greaterThan(T value, T threshold, ResponseCode code, String detail) {
        return check(NumberChecks.greaterThan(value, threshold), code, detail);
    }

    public <T extends Number & Comparable<T>> Chain greaterThan(T value, T threshold, ResponseCode code) {
        return check(NumberChecks.greaterThan(value, threshold), code);
    }

    public <T extends Number & Comparable<T>> Chain greaterThan(T value, T threshold, Consumer<Business.Fabricator> consumer) {
        return check(NumberChecks.greaterThan(value, threshold), consumer);
    }

    public <T extends Number & Comparable<T>> Chain greaterOrEqual(T value, T threshold) {
        return check(NumberChecks.greaterOrEqual(value, threshold));
    }

    public <T extends Number & Comparable<T>> Chain greaterOrEqual(T value, T threshold, ResponseCode code, String detail) {
        return check(NumberChecks.greaterOrEqual(value, threshold), code, detail);
    }

    public <T extends Number & Comparable<T>> Chain greaterOrEqual(T value, T threshold, ResponseCode code) {
        return check(NumberChecks.greaterOrEqual(value, threshold), code);
    }

    public <T extends Number & Comparable<T>> Chain greaterOrEqual(T value, T threshold, Consumer<Business.Fabricator> consumer) {
        return check(NumberChecks.greaterOrEqual(value, threshold), consumer);
    }

    public <T extends Number & Comparable<T>> Chain lessThan(T value, T threshold) {
        return check(NumberChecks.lessThan(value, threshold));
    }

    public <T extends Number & Comparable<T>> Chain lessThan(T value, T threshold, ResponseCode code, String detail) {
        return check(NumberChecks.lessThan(value, threshold), code, detail);
    }

    public <T extends Number & Comparable<T>> Chain lessThan(T value, T threshold, ResponseCode code) {
        return check(NumberChecks.lessThan(value, threshold), code);
    }

    public <T extends Number & Comparable<T>> Chain lessThan(T value, T threshold, Consumer<Business.Fabricator> consumer) {
        return check(NumberChecks.lessThan(value, threshold), consumer);
    }

    public <T extends Number & Comparable<T>> Chain lessOrEqual(T value, T threshold) {
        return check(NumberChecks.lessOrEqual(value, threshold));
    }

    public <T extends Number & Comparable<T>> Chain lessOrEqual(T value, T threshold, ResponseCode code, String detail) {
        return check(NumberChecks.lessOrEqual(value, threshold), code, detail);
    }

    public <T extends Number & Comparable<T>> Chain lessOrEqual(T value, T threshold, ResponseCode code) {
        return check(NumberChecks.lessOrEqual(value, threshold), code);
    }

    public <T extends Number & Comparable<T>> Chain lessOrEqual(T value, T threshold, Consumer<Business.Fabricator> consumer) {
        return check(NumberChecks.lessOrEqual(value, threshold), consumer);
    }

    public Chain notZero(Number value) {
        return check(NumberChecks.notZero(value));
    }

    public Chain notZero(Number value, ResponseCode code, String detail) {
        return check(NumberChecks.notZero(value), code, detail);
    }

    public Chain notZero(Number value, ResponseCode code) {
        return check(NumberChecks.notZero(value), code);
    }

    public Chain notZero(Number value, Consumer<Business.Fabricator> consumer) {
        return check(NumberChecks.notZero(value), consumer);
    }

    public Chain isZero(Number value) {
        return check(NumberChecks.isZero(value));
    }

    public Chain isZero(Number value, ResponseCode code, String detail) {
        return check(NumberChecks.isZero(value), code, detail);
    }

    public Chain isZero(Number value, ResponseCode code) {
        return check(NumberChecks.isZero(value), code);
    }

    public Chain isZero(Number value, Consumer<Business.Fabricator> consumer) {
        return check(NumberChecks.isZero(value), consumer);
    }

    public Chain negative(Number value) {
        return check(NumberChecks.negative(value));
    }

    public Chain negative(Number value, ResponseCode code, String detail) {
        return check(NumberChecks.negative(value), code, detail);
    }

    public Chain negative(Number value, ResponseCode code) {
        return check(NumberChecks.negative(value), code);
    }

    public Chain negative(Number value, Consumer<Business.Fabricator> consumer) {
        return check(NumberChecks.negative(value), consumer);
    }

    public Chain multipleOf(Number value, Number divisor) {
        return check(NumberChecks.multipleOf(value, divisor));
    }

    public Chain multipleOf(Number value, Number divisor, ResponseCode code, String detail) {
        return check(NumberChecks.multipleOf(value, divisor), code, detail);
    }

    public Chain multipleOf(Number value, Number divisor, ResponseCode code) {
        return check(NumberChecks.multipleOf(value, divisor), code);
    }

    public Chain multipleOf(Number value, Number divisor, Consumer<Business.Fabricator> consumer) {
        return check(NumberChecks.multipleOf(value, divisor), consumer);
    }

    public Chain decimalScale(BigDecimal value, int scale) {
        return check(NumberChecks.decimalScale(value, scale));
    }

    public Chain decimalScale(BigDecimal value, int scale, ResponseCode code, String detail) {
        return check(NumberChecks.decimalScale(value, scale), code, detail);
    }

    public Chain decimalScale(BigDecimal value, int scale, ResponseCode code) {
        return check(NumberChecks.decimalScale(value, scale), code);
    }

    public Chain decimalScale(BigDecimal value, int scale, Consumer<Business.Fabricator> consumer) {
        return check(NumberChecks.decimalScale(value, scale), consumer);
    }

    // ==================== 日期校验 (From DateChain) ====================

    public Chain after(Date date1, Date date2) {
        return check(DateChecks.after(date1, date2));
    }

    public Chain after(Date date1, Date date2, ResponseCode code, String detail) {
        return check(DateChecks.after(date1, date2), code, detail);
    }

    public Chain after(Date date1, Date date2, ResponseCode code) {
        return check(DateChecks.after(date1, date2), code);
    }

    public Chain after(Date date1, Date date2, Consumer<Business.Fabricator> consumer) {
        return check(DateChecks.after(date1, date2), consumer);
    }

    public Chain before(Date date1, Date date2) {
        return check(DateChecks.before(date1, date2));
    }

    public Chain before(Date date1, Date date2, ResponseCode code, String detail) {
        return check(DateChecks.before(date1, date2), code, detail);
    }

    public Chain before(Date date1, Date date2, ResponseCode code) {
        return check(DateChecks.before(date1, date2), code);
    }

    public Chain before(Date date1, Date date2, Consumer<Business.Fabricator> consumer) {
        return check(DateChecks.before(date1, date2), consumer);
    }

    // Generic Comparable Time API methods

    public <T extends Comparable<T>> Chain after(T t1, T t2) {
        return check(DateChecks.after(t1, t2));
    }

    public <T extends Comparable<T>> Chain after(T t1, T t2, ResponseCode code, String detail) {
        return check(DateChecks.after(t1, t2), code, detail);
    }

    public <T extends Comparable<T>> Chain after(T t1, T t2, ResponseCode code) {
        return check(DateChecks.after(t1, t2), code);
    }

    public <T extends Comparable<T>> Chain after(T t1, T t2, Consumer<Business.Fabricator> consumer) {
        return check(DateChecks.after(t1, t2), consumer);
    }

    public <T extends Comparable<T>> Chain afterOrEqual(T t1, T t2) {
        return check(DateChecks.afterOrEqual(t1, t2));
    }

    public <T extends Comparable<T>> Chain afterOrEqual(T t1, T t2, ResponseCode code, String detail) {
        return check(DateChecks.afterOrEqual(t1, t2), code, detail);
    }

    public <T extends Comparable<T>> Chain afterOrEqual(T t1, T t2, ResponseCode code) {
        return check(DateChecks.afterOrEqual(t1, t2), code);
    }

    public <T extends Comparable<T>> Chain afterOrEqual(T t1, T t2, Consumer<Business.Fabricator> consumer) {
        return check(DateChecks.afterOrEqual(t1, t2), consumer);
    }

    public <T extends Comparable<T>> Chain before(T t1, T t2) {
        return check(DateChecks.before(t1, t2));
    }

    public <T extends Comparable<T>> Chain before(T t1, T t2, ResponseCode code, String detail) {
        return check(DateChecks.before(t1, t2), code, detail);
    }

    public <T extends Comparable<T>> Chain before(T t1, T t2, ResponseCode code) {
        return check(DateChecks.before(t1, t2), code);
    }

    public <T extends Comparable<T>> Chain before(T t1, T t2, Consumer<Business.Fabricator> consumer) {
        return check(DateChecks.before(t1, t2), consumer);
    }

    public <T extends Comparable<T>> Chain beforeOrEqual(T t1, T t2) {
        return check(DateChecks.beforeOrEqual(t1, t2));
    }

    public <T extends Comparable<T>> Chain beforeOrEqual(T t1, T t2, ResponseCode code, String detail) {
        return check(DateChecks.beforeOrEqual(t1, t2), code, detail);
    }

    public <T extends Comparable<T>> Chain beforeOrEqual(T t1, T t2, ResponseCode code) {
        return check(DateChecks.beforeOrEqual(t1, t2), code);
    }

    public <T extends Comparable<T>> Chain beforeOrEqual(T t1, T t2, Consumer<Business.Fabricator> consumer) {
        return check(DateChecks.beforeOrEqual(t1, t2), consumer);
    }

    public <T extends Comparable<T>> Chain between(T value, T start, T end) {
        return check(DateChecks.between(value, start, end));
    }

    public <T extends Comparable<T>> Chain between(T value, T start, T end, ResponseCode code, String detail) {
        return check(DateChecks.between(value, start, end), code, detail);
    }

    public <T extends Comparable<T>> Chain between(T value, T start, T end, ResponseCode code) {
        return check(DateChecks.between(value, start, end), code);
    }

    public <T extends Comparable<T>> Chain between(T value, T start, T end, Consumer<Business.Fabricator> consumer) {
        return check(DateChecks.between(value, start, end), consumer);
    }

    // isPast / isFuture

    public Chain isPast(Date date) {
        return check(DateChecks.isPast(date));
    }

    public Chain isPast(Date date, ResponseCode code) {
        return check(DateChecks.isPast(date), code);
    }

    public Chain isPast(Date date, ResponseCode code, String detail) {
        return check(DateChecks.isPast(date), code, detail);
    }

    public Chain isPast(Date date, Consumer<Business.Fabricator> consumer) {
        return check(DateChecks.isPast(date), consumer);
    }

    public Chain isFuture(Date date) {
        return check(DateChecks.isFuture(date));
    }

    public Chain isFuture(Date date, ResponseCode code) {
        return check(DateChecks.isFuture(date), code);
    }

    public Chain isFuture(Date date, ResponseCode code, String detail) {
        return check(DateChecks.isFuture(date), code, detail);
    }

    public Chain isFuture(Date date, Consumer<Business.Fabricator> consumer) {
        return check(DateChecks.isFuture(date), consumer);
    }

    public Chain isPast(ChronoLocalDate date) {
        return check(DateChecks.isPast(date));
    }

    public Chain isPast(ChronoLocalDate date, ResponseCode code) {
        return check(DateChecks.isPast(date), code);
    }

    public Chain isPast(ChronoLocalDate date, ResponseCode code, String detail) {
        return check(DateChecks.isPast(date), code, detail);
    }

    public Chain isPast(ChronoLocalDate date, Consumer<Business.Fabricator> consumer) {
        return check(DateChecks.isPast(date), consumer);
    }

    public Chain isFuture(ChronoLocalDate date) {
        return check(DateChecks.isFuture(date));
    }

    public Chain isFuture(ChronoLocalDate date, ResponseCode code) {
        return check(DateChecks.isFuture(date), code);
    }

    public Chain isFuture(ChronoLocalDate date, ResponseCode code, String detail) {
        return check(DateChecks.isFuture(date), code, detail);
    }

    public Chain isFuture(ChronoLocalDate date, Consumer<Business.Fabricator> consumer) {
        return check(DateChecks.isFuture(date), consumer);
    }

    public Chain isPast(ChronoLocalDateTime<?> date) {
        return check(DateChecks.isPast(date));
    }

    public Chain isPast(ChronoLocalDateTime<?> date, ResponseCode code) {
        return check(DateChecks.isPast(date), code);
    }

    public Chain isPast(ChronoLocalDateTime<?> date, ResponseCode code, String detail) {
        return check(DateChecks.isPast(date), code, detail);
    }

    public Chain isPast(ChronoLocalDateTime<?> date, Consumer<Business.Fabricator> consumer) {
        return check(DateChecks.isPast(date), consumer);
    }

    public Chain isFuture(ChronoLocalDateTime<?> date) {
        return check(DateChecks.isFuture(date));
    }

    public Chain isFuture(ChronoLocalDateTime<?> date, ResponseCode code) {
        return check(DateChecks.isFuture(date), code);
    }

    public Chain isFuture(ChronoLocalDateTime<?> date, ResponseCode code, String detail) {
        return check(DateChecks.isFuture(date), code, detail);
    }

    public Chain isFuture(ChronoLocalDateTime<?> date, Consumer<Business.Fabricator> consumer) {
        return check(DateChecks.isFuture(date), consumer);
    }

    public Chain isPast(Instant date) {
        return check(DateChecks.isPast(date));
    }

    public Chain isPast(Instant date, ResponseCode code) {
        return check(DateChecks.isPast(date), code);
    }

    public Chain isPast(Instant date, ResponseCode code, String detail) {
        return check(DateChecks.isPast(date), code, detail);
    }

    public Chain isPast(Instant date, Consumer<Business.Fabricator> consumer) {
        return check(DateChecks.isPast(date), consumer);
    }

    public Chain isFuture(Instant date) {
        return check(DateChecks.isFuture(date));
    }

    public Chain isFuture(Instant date, ResponseCode code) {
        return check(DateChecks.isFuture(date), code);
    }

    public Chain isFuture(Instant date, ResponseCode code, String detail) {
        return check(DateChecks.isFuture(date), code, detail);
    }

    public Chain isFuture(Instant date, Consumer<Business.Fabricator> consumer) {
        return check(DateChecks.isFuture(date), consumer);
    }

    public Chain isPast(ChronoZonedDateTime<?> date) {
        return check(DateChecks.isPast(date));
    }

    public Chain isPast(ChronoZonedDateTime<?> date, ResponseCode code) {
        return check(DateChecks.isPast(date), code);
    }

    public Chain isPast(ChronoZonedDateTime<?> date, ResponseCode code, String detail) {
        return check(DateChecks.isPast(date), code, detail);
    }

    public Chain isPast(ChronoZonedDateTime<?> date, Consumer<Business.Fabricator> consumer) {
        return check(DateChecks.isPast(date), consumer);
    }

    public Chain isFuture(ChronoZonedDateTime<?> date) {
        return check(DateChecks.isFuture(date));
    }

    public Chain isFuture(ChronoZonedDateTime<?> date, ResponseCode code) {
        return check(DateChecks.isFuture(date), code);
    }

    public Chain isFuture(ChronoZonedDateTime<?> date, ResponseCode code, String detail) {
        return check(DateChecks.isFuture(date), code, detail);
    }

    public Chain isFuture(ChronoZonedDateTime<?> date, Consumer<Business.Fabricator> consumer) {
        return check(DateChecks.isFuture(date), consumer);
    }

    public Chain isToday(LocalDate date) {
        return check(DateChecks.isToday(date));
    }

    public Chain isToday(LocalDate date, ResponseCode code) {
        return check(DateChecks.isToday(date), code);
    }

    public Chain isToday(LocalDate date, ResponseCode code, String detail) {
        return check(DateChecks.isToday(date), code, detail);
    }

    public Chain isToday(LocalDate date, Consumer<Business.Fabricator> consumer) {
        return check(DateChecks.isToday(date), consumer);
    }

    // ==================== 枚举校验 (From EnumChain) ====================

    public <E extends Enum<E>> Chain enumValue(Class<E> enumType, String value) {
        if (!alive) return this;
        return check(EnumChecks.enumValue(enumType, value));
    }

    public <E extends Enum<E>> Chain enumValue(Class<E> enumType, String value, ResponseCode code, String detail) {
        if (!alive) return this;
        return check(EnumChecks.enumValue(enumType, value), code, detail);
    }

    public <E extends Enum<E>> Chain enumValue(Class<E> enumType, String value, ResponseCode code) {
        if (shouldSkip()) return this;
        return check(EnumChecks.enumValue(enumType, value), code);
    }

    public <E extends Enum<E>> Chain enumValue(Class<E> enumType, String value, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return this;
        return check(EnumChecks.enumValue(enumType, value), consumer);
    }

    public <E extends Enum<E>> Chain enumConstant(E value, Class<E> type) {
        return check(EnumChecks.enumConstant(value, type));
    }

    public <E extends Enum<E>> Chain enumConstant(E value, Class<E> type, ResponseCode code, String detail) {
        return check(EnumChecks.enumConstant(value, type), code, detail);
    }

    public <E extends Enum<E>> Chain enumConstant(E value, Class<E> type, ResponseCode code) {
        return check(EnumChecks.enumConstant(value, type), code);
    }

    public <E extends Enum<E>> Chain enumConstant(E value, Class<E> type, Consumer<Business.Fabricator> consumer) {
        return check(EnumChecks.enumConstant(value, type), consumer);
    }

    // ==================== 对象同一性校验 (From IdentityChain) ====================

    public Chain same(Object obj1, Object obj2) {
        return check(IdentityChecks.same(obj1, obj2));
    }

    public Chain same(Object obj1, Object obj2, ResponseCode code, String detail) {
        return check(IdentityChecks.same(obj1, obj2), code, detail);
    }

    public Chain same(Object obj1, Object obj2, ResponseCode code) {
        return check(IdentityChecks.same(obj1, obj2), code);
    }

    public Chain same(Object obj1, Object obj2, Consumer<Business.Fabricator> consumer) {
        return check(IdentityChecks.same(obj1, obj2), consumer);
    }

    public Chain notSame(Object obj1, Object obj2) {
        return check(IdentityChecks.notSame(obj1, obj2));
    }

    public Chain notSame(Object obj1, Object obj2, ResponseCode code, String detail) {
        return check(IdentityChecks.notSame(obj1, obj2), code, detail);
    }

    public Chain notSame(Object obj1, Object obj2, ResponseCode code) {
        return check(IdentityChecks.notSame(obj1, obj2), code);
    }

    public Chain notSame(Object obj1, Object obj2, Consumer<Business.Fabricator> consumer) {
        return check(IdentityChecks.notSame(obj1, obj2), consumer);
    }

    public Chain equals(Object obj1, Object obj2) {
        return check(IdentityChecks.equals(obj1, obj2));
    }

    public Chain equals(Object obj1, Object obj2, ResponseCode code, String detail) {
        return check(IdentityChecks.equals(obj1, obj2), code, detail);
    }

    public Chain equals(Object obj1, Object obj2, ResponseCode code) {
        return check(IdentityChecks.equals(obj1, obj2), code);
    }

    public Chain equals(Object obj1, Object obj2, Consumer<Business.Fabricator> consumer) {
        return check(IdentityChecks.equals(obj1, obj2), consumer);
    }

    public Chain notEquals(Object obj1, Object obj2) {
        return check(IdentityChecks.notEquals(obj1, obj2));
    }

    public Chain notEquals(Object obj1, Object obj2, ResponseCode code, String detail) {
        return check(IdentityChecks.notEquals(obj1, obj2), code, detail);
    }

    public Chain notEquals(Object obj1, Object obj2, ResponseCode code) {
        return check(IdentityChecks.notEquals(obj1, obj2), code);
    }

    public Chain notEquals(Object obj1, Object obj2, Consumer<Business.Fabricator> consumer) {
        return check(IdentityChecks.notEquals(obj1, obj2), consumer);
    }
}