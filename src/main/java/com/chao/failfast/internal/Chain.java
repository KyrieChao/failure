package com.chao.failfast.internal;

import com.chao.failfast.internal.check.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 验证链 - 支持FailFast和非FailFast双模式
 * 提供流畅的链式验证API，支持快速失败和全量收集两种验证策略
 * <p>
 * 重构说明：
 * 原有的 11 层继承结构已重构为扁平化设计，采用组合静态工具类的方式实现。
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
            throw errors.get(0);
        }
    }

    public void failAll() {
        if (!isValid()) {
            if (errors.size() == 1) throw errors.get(0);
            throw new MultiBusiness(errors);
        }
    }

    public Chain failNow(ResponseCode code) {
        if (!alive) throw Business.of(code);
        return this;
    }

    public Chain failNow(ResponseCode code, String msg) {
        if (!alive) throw Business.of(code, msg);
        return this;
    }

    public Chain failNow(ResponseCode code, String msgFormat, Object... args) {
        if (!alive) throw Business.of(code, String.format(msgFormat, args));
        return this;
    }

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

    public Chain exists(Object obj, Consumer<Business.Fabricator> consumer) {
        return check(ObjectChecks.exists(obj), consumer);
    }

    public Chain notNull(Object obj) {
        return exists(obj);
    }

    public Chain notNull(Object obj, ResponseCode code) {
        return exists(obj, code);
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

    public Chain isNull(Object obj, Consumer<Business.Fabricator> consumer) {
        return check(ObjectChecks.isNull(obj), consumer);
    }

    // ==================== 布尔校验 (From BooleanChain) ====================

    public Chain state(boolean condition) {
        return check(BooleanChecks.state(condition));
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

    public Chain isTrue(boolean cond, ResponseCode code) {
        return state(cond, code);
    }

    public Chain isTrue(boolean cond, Consumer<Business.Fabricator> consumer) {
        return state(cond, consumer);
    }

    public Chain isFalse(boolean cond) {
        return state(!cond);
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

    public Chain blank(String str, ResponseCode code) {
        return check(StringChecks.blank(str), code);
    }

    public Chain blank(String str, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.blank(str), consumer);
    }

    public Chain notBlank(String str) {
        return check(StringChecks.notBlank(str));
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

    public Chain email(String email, ResponseCode code) {
        return check(StringChecks.email(email), code);
    }

    public Chain email(String email, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.email(email), consumer);
    }

    public Chain equalsIgnoreCase(String str1, String str2) {
        return check(StringChecks.equalsIgnoreCase(str1, str2));
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

    public Chain startsWith(String str, String prefix, ResponseCode code) {
        return check(StringChecks.startsWith(str, prefix), code);
    }

    public Chain startsWith(String str, String prefix, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.startsWith(str, prefix), consumer);
    }

    public Chain endsWith(String str, String suffix) {
        return check(StringChecks.endsWith(str, suffix));
    }

    public Chain endsWith(String str, String suffix, ResponseCode code) {
        return check(StringChecks.endsWith(str, suffix), code);
    }

    public Chain endsWith(String str, String suffix, Consumer<Business.Fabricator> consumer) {
        return check(StringChecks.endsWith(str, suffix), consumer);
    }

    // ==================== 集合校验 (From CollectionChain) ====================

    public Chain notEmpty(Collection<?> col) {
        return check(CollectionChecks.notEmpty(col));
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

    public Chain sizeEquals(Collection<?> col, int expectedSize, ResponseCode code) {
        return check(CollectionChecks.sizeEquals(col, expectedSize), code);
    }

    public Chain sizeEquals(Collection<?> col, int expectedSize, Consumer<Business.Fabricator> consumer) {
        return check(CollectionChecks.sizeEquals(col, expectedSize), consumer);
    }

    public Chain contains(Collection<?> col, Object o) {
        return check(CollectionChecks.contains(col, o));
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

    public Chain notContains(Collection<?> col, Object o, ResponseCode code) {
        if (shouldSkip()) return this;
        return check(CollectionChecks.notContains(col, o), code);
    }

    public Chain notContains(Collection<?> col, Object o, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return this;
        return check(CollectionChecks.notContains(col, o), consumer);
    }

    // ==================== 数组校验 (From ArrayChain) ====================

    public <T> Chain notEmpty(T[] array) {
        return check(ArrayChecks.notEmpty(array));
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

    public <T> Chain notContains(T[] array, T o, ResponseCode code) {
        if (shouldSkip()) return this;
        return check(ArrayChecks.notContains(array, o), code);
    }

    public <T> Chain notContains(T[] array, T o, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return this;
        return check(ArrayChecks.notContains(array, o), consumer);
    }

    // ==================== 数值校验 (From NumberChain) ====================

    public Chain positive(Number value) {
        return check(NumberChecks.positive(value));
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

    public <T extends Number & Comparable<T>> Chain nonNegative(T value, ResponseCode code) {
        return check(NumberChecks.nonNegative(value), code);
    }

    public <T extends Number & Comparable<T>> Chain nonNegative(T value, Consumer<Business.Fabricator> consumer) {
        return check(NumberChecks.nonNegative(value), consumer);
    }

    // ==================== 日期校验 (From DateChain) ====================

    public Chain after(Date date1, Date date2) {
        return check(DateChecks.after(date1, date2));
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

    public Chain before(Date date1, Date date2, ResponseCode code) {
        return check(DateChecks.before(date1, date2), code);
    }

    public Chain before(Date date1, Date date2, Consumer<Business.Fabricator> consumer) {
        return check(DateChecks.before(date1, date2), consumer);
    }

    // ==================== 枚举校验 (From EnumChain) ====================

    public <E extends Enum<E>> Chain enumValue(Class<E> enumType, String value) {
        if (!alive) return this;
        return check(EnumChecks.isValidEnum(enumType, value));
    }

    public <E extends Enum<E>> Chain enumValue(Class<E> enumType, String value, ResponseCode code) {
        if (shouldSkip()) return this;
        return check(EnumChecks.isValidEnum(enumType, value), code);
    }

    public <E extends Enum<E>> Chain enumValue(Class<E> enumType, String value, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return this;
        return check(EnumChecks.isValidEnum(enumType, value), consumer);
    }

    // ==================== 对象同一性校验 (From IdentityChain) ====================

    public Chain same(Object obj1, Object obj2) {
        return check(IdentityChecks.same(obj1, obj2));
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

    public Chain notSame(Object obj1, Object obj2, ResponseCode code) {
        return check(IdentityChecks.notSame(obj1, obj2), code);
    }

    public Chain notSame(Object obj1, Object obj2, Consumer<Business.Fabricator> consumer) {
        return check(IdentityChecks.notSame(obj1, obj2), consumer);
    }

    public Chain equals(Object obj1, Object obj2) {
        return check(IdentityChecks.equals(obj1, obj2));
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

    public Chain notEquals(Object obj1, Object obj2, ResponseCode code) {
        return check(IdentityChecks.notEquals(obj1, obj2), code);
    }

    public Chain notEquals(Object obj1, Object obj2, Consumer<Business.Fabricator> consumer) {
        return check(IdentityChecks.notEquals(obj1, obj2), consumer);
    }
}
