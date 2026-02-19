package com.chao.failfast;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.Chain;
import com.chao.failfast.internal.ResponseCode;

import java.util.Collection;
import java.util.Date;
import java.util.function.Consumer;

/**
 * Fail-Fast 静态入口类 - 链式验证API
 * 提供流畅的链式调用接口，支持快速失败和严格模式两种验证策略
 * 通过静态方法提供各种常见验证场景的便捷入口
 */
public final class Failure {

    private Failure() {
    }

    /**
     * 开始一个新的验证链（默认快速失败模式）
     *
     * @return 新的 Chain 实例
     */
    private static Chain begin() {
        return Chain.begin(true);
    }

    /**
     * 开始一个新的验证链（全量收集模式）
     * 在此模式下，验证失败不会立即抛出异常，而是收集所有错误
     *
     * @return 新的 Chain 实例
     */
    public static Chain strict() {
        return Chain.begin(false);
    }

    /**
     * 验证对象是否存在（非空）
     *
     * @param obj 待验证对象
     * @return 验证链实例
     */
    public static Chain exists(Object obj) {
        return begin().exists(obj);
    }

    /**
     * 验证对象是否存在（非空），失败时使用指定错误码
     *
     * @param obj  待验证对象
     * @param code 错误码
     * @return 验证链实例
     */
    public static Chain exists(Object obj, ResponseCode code) {
        return begin().exists(obj, code);
    }

    /**
     * 验证对象是否存在（非空），失败时使用自定义错误构建器
     *
     * @param obj      待验证对象
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain exists(Object obj, Consumer<Business.Fabricator> consumer) {
        return begin().exists(obj, consumer);
    }

    /**
     * 验证对象不为 null
     *
     * @param obj 待验证对象
     * @return 验证链实例
     */
    public static Chain notNull(Object obj) {
        return begin().notNull(obj);
    }

    /**
     * 验证对象不为 null，失败时使用指定错误码
     *
     * @param obj  待验证对象
     * @param code 错误码
     * @return 验证链实例
     */
    public static Chain notNull(Object obj, ResponseCode code) {
        return begin().notNull(obj, code);
    }

    /**
     * 验证对象不为 null，失败时使用自定义错误构建器
     *
     * @param obj      待验证对象
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain notNull(Object obj, Consumer<Business.Fabricator> consumer) {
        return begin().notNull(obj, consumer);
    }

    /**
     * 验证对象必须为 null
     *
     * @param obj 待验证对象
     * @return 验证链实例
     */
    public static Chain isNull(Object obj) {
        return begin().isNull(obj);
    }

    /**
     * 验证对象必须为 null，失败时使用指定错误码
     *
     * @param obj  待验证对象
     * @param code 错误码
     * @return 验证链实例
     */
    public static Chain isNull(Object obj, ResponseCode code) {
        return begin().isNull(obj, code);
    }

    /**
     * 验证对象必须为 null，失败时使用自定义错误构建器
     *
     * @param obj      待验证对象
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain isNull(Object obj, Consumer<Business.Fabricator> consumer) {
        return begin().isNull(obj, consumer);
    }

    /**
     * 验证布尔状态是否为 true
     *
     * @param condition 待验证条件
     * @return 验证链实例
     */
    public static Chain state(boolean condition) {
        return begin().state(condition);
    }

    /**
     * 验证布尔状态是否为 true，失败时使用指定错误码
     *
     * @param condition 待验证条件
     * @param code      错误码
     * @return 验证链实例
     */
    public static Chain state(boolean condition, ResponseCode code) {
        return begin().state(condition, code);
    }

    /**
     * 验证布尔状态是否为 true，失败时使用自定义错误构建器
     *
     * @param condition 待验证条件
     * @param consumer  错误构建器消费者
     * @return 验证链实例
     */
    public static Chain state(boolean condition, Consumer<Business.Fabricator> consumer) {
        return begin().state(condition, consumer);
    }

    /**
     * 验证布尔状态是否为 true，失败时使用指定错误码和描述
     *
     * @param condition   待验证条件
     * @param code        错误码
     * @param description 错误描述
     * @return 验证链实例
     */
    public static Chain state(boolean condition, ResponseCode code, String description) {
        return begin().state(condition, b -> b.code(code).detail(description));
    }

    /**
     * 验证布尔值是否为 true
     *
     * @param cond 待验证布尔值
     * @return 验证链实例
     */
    public static Chain isTrue(boolean cond) {
        return begin().isTrue(cond);
    }

    /**
     * 验证布尔值是否为 true，失败时使用指定错误码
     *
     * @param cond 待验证布尔值
     * @param code 错误码
     * @return 验证链实例
     */
    public static Chain isTrue(boolean cond, ResponseCode code) {
        return begin().isTrue(cond, code);
    }

    /**
     * 验证布尔值是否为 true，失败时使用自定义错误构建器
     *
     * @param cond     待验证布尔值
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain isTrue(boolean cond, Consumer<Business.Fabricator> consumer) {
        return begin().isTrue(cond, consumer);
    }

    /**
     * 验证布尔值是否为 false
     *
     * @param cond 待验证布尔值
     * @return 验证链实例
     */
    public static Chain isFalse(boolean cond) {
        return begin().isFalse(cond);
    }

    /**
     * 验证布尔值是否为 false，失败时使用指定错误码
     *
     * @param cond 待验证布尔值
     * @param code 错误码
     * @return 验证链实例
     */
    public static Chain isFalse(boolean cond, ResponseCode code) {
        return begin().isFalse(cond, code);
    }

    /**
     * 验证布尔值是否为 false，失败时使用自定义错误构建器
     *
     * @param cond     待验证布尔值
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain isFalse(boolean cond, Consumer<Business.Fabricator> consumer) {
        return begin().isFalse(cond, consumer);
    }

    // ==================== 字符串 ====================

    /**
     * 验证字符串必须为空白（null、空串或全空格）
     *
     * @param str 待验证字符串
     * @return 验证链实例
     */
    public static Chain blank(String str) {
        return begin().blank(str);
    }

    /**
     * 验证字符串必须为空白，失败时使用指定错误码
     *
     * @param str  待验证字符串
     * @param code 错误码
     * @return 验证链实例
     */
    public static Chain blank(String str, ResponseCode code) {
        return begin().blank(str, code);
    }

    /**
     * 验证字符串必须为空白，失败时使用自定义错误构建器
     *
     * @param str      待验证字符串
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain blank(String str, Consumer<Business.Fabricator> consumer) {
        return begin().blank(str, consumer);
    }

    /**
     * 验证字符串不能为 null 且不能全为空格
     *
     * @param str 待验证字符串
     * @return 验证链实例
     */
    public static Chain notBlank(String str) {
        return begin().notBlank(str);
    }

    /**
     * 验证字符串不能为 null 且不能全为空格，失败时使用指定错误码
     *
     * @param str  待验证字符串
     * @param code 错误码
     * @return 验证链实例
     */
    public static Chain notBlank(String str, ResponseCode code) {
        return begin().notBlank(str, code);
    }

    /**
     * 验证字符串不能为 null 且不能全为空格，失败时使用自定义错误构建器
     *
     * @param str      待验证字符串
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain notBlank(String str, Consumer<Business.Fabricator> consumer) {
        return begin().notBlank(str, consumer);
    }

    /**
     * 验证字符串不能为 null 且不能为空串
     *
     * @param str 待验证字符串
     * @return 验证链实例
     */
    public static Chain notEmpty(String str) {
        return begin().notEmpty(str);
    }

    /**
     * 验证字符串不能为 null 且不能为空串，失败时使用指定错误码
     *
     * @param str  待验证字符串
     * @param code 错误码
     * @return 验证链实例
     */
    public static Chain notEmpty(String str, ResponseCode code) {
        return begin().notEmpty(str, code);
    }

    /**
     * 验证字符串不能为 null 且不能为空串，失败时使用自定义错误构建器
     *
     * @param str      待验证字符串
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain notEmpty(String str, Consumer<Business.Fabricator> consumer) {
        return begin().notEmpty(str, consumer);
    }

    /**
     * 验证字符串长度在指定范围内
     *
     * @param str 待验证字符串
     * @param min 最小长度
     * @param max 最大长度
     * @return 验证链实例
     */
    public static Chain lengthBetween(String str, int min, int max) {
        return begin().lengthBetween(str, min, max);
    }

    /**
     * 验证字符串长度在指定范围内，失败时使用指定错误码
     *
     * @param str  待验证字符串
     * @param min  最小长度
     * @param max  最大长度
     * @param code 错误码
     * @return 验证链实例
     */
    public static Chain lengthBetween(String str, int min, int max, ResponseCode code) {
        return begin().lengthBetween(str, min, max, code);
    }

    /**
     * 验证字符串长度在指定范围内，失败时使用自定义错误构建器
     *
     * @param str      待验证字符串
     * @param min      最小长度
     * @param max      最大长度
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain lengthBetween(String str, int min, int max, Consumer<Business.Fabricator> consumer) {
        return begin().lengthBetween(str, min, max, consumer);
    }

    /**
     * 验证字符串是否匹配指定正则表达式
     *
     * @param str   待验证字符串
     * @param regex 正则表达式
     * @return 验证链实例
     */
    public static Chain match(String str, String regex) {
        return begin().match(str, regex);
    }

    /**
     * 验证字符串是否匹配指定正则表达式，失败时使用指定错误码
     *
     * @param str   待验证字符串
     * @param regex 正则表达式
     * @param code  错误码
     * @return 验证链实例
     */
    public static Chain match(String str, String regex, ResponseCode code) {
        return begin().match(str, regex, code);
    }

    /**
     * 验证字符串是否匹配指定正则表达式，失败时使用自定义错误构建器
     *
     * @param str      待验证字符串
     * @param regex    正则表达式
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain match(String str, String regex, Consumer<Business.Fabricator> consumer) {
        return begin().match(str, regex, consumer);
    }

    /**
     * 验证字符串是否为有效的邮箱格式
     *
     * @param email 待验证邮箱字符串
     * @return 验证链实例
     */
    public static Chain email(String email) {
        return begin().email(email);
    }

    /**
     * 验证字符串是否为有效的邮箱格式，失败时使用指定错误码
     *
     * @param email 待验证邮箱字符串
     * @param code  错误码
     * @return 验证链实例
     */
    public static Chain email(String email, ResponseCode code) {
        return begin().email(email, code);
    }

    /**
     * 验证字符串是否为有效的邮箱格式，失败时使用自定义错误构建器
     *
     * @param email    待验证邮箱字符串
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain email(String email, Consumer<Business.Fabricator> consumer) {
        return begin().email(email, consumer);
    }

    /**
     * 验证两个字符串是否相等（忽略大小写）
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 验证链实例
     */
    public static Chain equalsIgnoreCase(String str1, String str2) {
        return begin().equalsIgnoreCase(str1, str2);
    }

    /**
     * 验证两个字符串是否相等（忽略大小写），失败时使用指定错误码
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @param code 错误码
     * @return 验证链实例
     */
    public static Chain equalsIgnoreCase(String str1, String str2, ResponseCode code) {
        return begin().equalsIgnoreCase(str1, str2, code);
    }

    /**
     * 验证两个字符串是否相等（忽略大小写），失败时使用自定义错误构建器
     *
     * @param str1     字符串1
     * @param str2     字符串2
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain equalsIgnoreCase(String str1, String str2, Consumer<Business.Fabricator> consumer) {
        return begin().equalsIgnoreCase(str1, str2, consumer);
    }

    /**
     * 验证字符串是否以指定前缀开头
     *
     * @param str    待验证字符串
     * @param prefix 前缀
     * @return 验证链实例
     */
    public static Chain startsWith(String str, String prefix) {
        return begin().startsWith(str, prefix);
    }

    /**
     * 验证字符串是否以指定前缀开头，失败时使用指定错误码
     *
     * @param str    待验证字符串
     * @param prefix 前缀
     * @param code   错误码
     * @return 验证链实例
     */
    public static Chain startsWith(String str, String prefix, ResponseCode code) {
        return begin().startsWith(str, prefix, code);
    }

    /**
     * 验证字符串是否以指定前缀开头，失败时使用自定义错误构建器
     *
     * @param str      待验证字符串
     * @param prefix   前缀
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain startsWith(String str, String prefix, Consumer<Business.Fabricator> consumer) {
        return begin().startsWith(str, prefix, consumer);
    }

    /**
     * 验证字符串是否以指定后缀结尾
     *
     * @param str    待验证字符串
     * @param suffix 后缀
     * @return 验证链实例
     */
    public static Chain endsWith(String str, String suffix) {
        return begin().endsWith(str, suffix);
    }

    /**
     * 验证字符串是否以指定后缀结尾，失败时使用指定错误码
     *
     * @param str    待验证字符串
     * @param suffix 后缀
     * @param code   错误码
     * @return 验证链实例
     */
    public static Chain endsWith(String str, String suffix, ResponseCode code) {
        return begin().endsWith(str, suffix, code);
    }

    /**
     * 验证字符串是否以指定后缀结尾，失败时使用自定义错误构建器
     *
     * @param str      待验证字符串
     * @param suffix   后缀
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain endsWith(String str, String suffix, Consumer<Business.Fabricator> consumer) {
        return begin().endsWith(str, suffix, consumer);
    }

    // ==================== 集合 ====================

    /**
     * 验证集合不能为空（不能为null且包含元素）
     *
     * @param col 待验证集合
     * @return 验证链实例
     */
    public static Chain notEmpty(Collection<?> col) {
        return begin().notEmpty(col);
    }

    /**
     * 验证集合不能为空，失败时使用指定错误码
     *
     * @param col  待验证集合
     * @param code 错误码
     * @return 验证链实例
     */
    public static Chain notEmpty(Collection<?> col, ResponseCode code) {
        return begin().notEmpty(col, code);
    }

    /**
     * 验证集合不能为空，失败时使用自定义错误构建器
     *
     * @param col      待验证集合
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain notEmpty(Collection<?> col, Consumer<Business.Fabricator> consumer) {
        return begin().notEmpty(col, consumer);
    }

    /**
     * 验证数组不能为空（不能为null且包含元素）
     *
     * @param array 待验证数组
     * @param <T>   数组元素类型
     * @return 验证链实例
     */
    public static <T> Chain notEmpty(T[] array) {
        return begin().notEmpty(array);
    }

    /**
     * 验证数组不能为空，失败时使用指定错误码
     *
     * @param array 待验证数组
     * @param code  错误码
     * @param <T>   数组元素类型
     * @return 验证链实例
     */
    public static <T> Chain notEmpty(T[] array, ResponseCode code) {
        return begin().notEmpty(array, code);
    }

    /**
     * 验证数组不能为空，失败时使用自定义错误构建器
     *
     * @param array    待验证数组
     * @param consumer 错误构建器消费者
     * @param <T>      数组元素类型
     * @return 验证链实例
     */
    public static <T> Chain notEmpty(T[] array, Consumer<Business.Fabricator> consumer) {
        return begin().notEmpty(array, consumer);
    }

    /**
     * 验证集合大小在指定范围内
     *
     * @param col 待验证集合
     * @param min 最小大小
     * @param max 最大大小
     * @return 验证链实例
     */
    public static Chain sizeBetween(Collection<?> col, int min, int max) {
        return begin().sizeBetween(col, min, max);
    }

    /**
     * 验证集合大小在指定范围内，失败时使用指定错误码
     *
     * @param col  待验证集合
     * @param min  最小大小
     * @param max  最大大小
     * @param code 错误码
     * @return 验证链实例
     */
    public static Chain sizeBetween(Collection<?> col, int min, int max, ResponseCode code) {
        return begin().sizeBetween(col, min, max, code);
    }

    /**
     * 验证集合大小在指定范围内，失败时使用自定义错误构建器
     *
     * @param col      待验证集合
     * @param min      最小大小
     * @param max      最大大小
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain sizeBetween(Collection<?> col, int min, int max, Consumer<Business.Fabricator> consumer) {
        return begin().sizeBetween(col, min, max, consumer);
    }

    /**
     * 验证数组大小在指定范围内
     *
     * @param array 待验证数组
     * @param min   最小大小
     * @param max   最大大小
     * @param <T>   数组元素类型
     * @return 验证链实例
     */
    public static <T> Chain sizeBetween(T[] array, int min, int max) {
        return begin().sizeBetween(array, min, max);
    }

    /**
     * 验证数组大小在指定范围内，失败时使用指定错误码
     *
     * @param array 待验证数组
     * @param min   最小大小
     * @param max   最大大小
     * @param code  错误码
     * @param <T>   数组元素类型
     * @return 验证链实例
     */
    public static <T> Chain sizeBetween(T[] array, int min, int max, ResponseCode code) {
        return begin().sizeBetween(array, min, max, code);
    }

    /**
     * 验证数组大小在指定范围内，失败时使用自定义错误构建器
     *
     * @param array    待验证数组
     * @param min      最小大小
     * @param max      最大大小
     * @param consumer 错误构建器消费者
     * @param <T>      数组元素类型
     * @return 验证链实例
     */
    public static <T> Chain sizeBetween(T[] array, int min, int max, Consumer<Business.Fabricator> consumer) {
        return begin().sizeBetween(array, min, max, consumer);
    }

    /**
     * 验证集合大小等于预期值
     *
     * @param col          待验证集合
     * @param expectedSize 预期大小
     * @return 验证链实例
     */
    public static Chain sizeEquals(Collection<?> col, int expectedSize) {
        return begin().sizeEquals(col, expectedSize);
    }

    /**
     * 验证集合大小等于预期值，失败时使用指定错误码
     *
     * @param col          待验证集合
     * @param expectedSize 预期大小
     * @param code         错误码
     * @return 验证链实例
     */
    public static Chain sizeEquals(Collection<?> col, int expectedSize, ResponseCode code) {
        return begin().sizeEquals(col, expectedSize, code);
    }

    /**
     * 验证集合大小等于预期值，失败时使用自定义错误构建器
     *
     * @param col          待验证集合
     * @param expectedSize 预期大小
     * @param consumer     错误构建器消费者
     * @return 验证链实例
     */
    public static Chain sizeEquals(Collection<?> col, int expectedSize, Consumer<Business.Fabricator> consumer) {
        return begin().sizeEquals(col, expectedSize, consumer);
    }

    /**
     * 验证数组大小等于预期值
     *
     * @param array        待验证数组
     * @param expectedSize 预期大小
     * @param <T>          数组元素类型
     * @return 验证链实例
     */
    public static <T> Chain sizeEquals(T[] array, int expectedSize) {
        return begin().sizeEquals(array, expectedSize);
    }

    /**
     * 验证数组大小等于预期值，失败时使用指定错误码
     *
     * @param array        待验证数组
     * @param expectedSize 预期大小
     * @param code         错误码
     * @param <T>          数组元素类型
     * @return 验证链实例
     */
    public static <T> Chain sizeEquals(T[] array, int expectedSize, ResponseCode code) {
        return begin().sizeEquals(array, expectedSize, code);
    }

    /**
     * 验证数组大小等于预期值，失败时使用自定义错误构建器
     *
     * @param array        待验证数组
     * @param expectedSize 预期大小
     * @param consumer     错误构建器消费者
     * @param <T>          数组元素类型
     * @return 验证链实例
     */
    public static <T> Chain sizeEquals(T[] array, int expectedSize, Consumer<Business.Fabricator> consumer) {
        return begin().sizeEquals(array, expectedSize, consumer);
    }

    /**
     * 验证集合包含指定元素
     *
     * @param col 待验证集合
     * @param o   指定元素
     * @return 验证链实例
     */
    public static Chain contains(Collection<?> col, Object o) {
        return begin().contains(col, o);
    }

    /**
     * 验证集合包含指定元素，失败时使用指定错误码
     *
     * @param col  待验证集合
     * @param o    指定元素
     * @param code 错误码
     * @return 验证链实例
     */
    public static Chain contains(Collection<?> col, Object o, ResponseCode code) {
        return begin().contains(col, o, code);
    }

    /**
     * 验证集合包含指定元素，失败时使用自定义错误构建器
     *
     * @param col      待验证集合
     * @param o        指定元素
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain contains(Collection<?> col, Object o, Consumer<Business.Fabricator> consumer) {
        return begin().contains(col, o, consumer);
    }

    /**
     * 验证数组包含指定元素
     *
     * @param array 待验证数组
     * @param o     指定元素
     * @param <T>   数组元素类型
     * @return 验证链实例
     */
    public static <T> Chain contains(T[] array, T o) {
        return begin().contains(array, o);
    }

    /**
     * 验证数组包含指定元素，失败时使用指定错误码
     *
     * @param array 待验证数组
     * @param o     指定元素
     * @param code  错误码
     * @param <T>   数组元素类型
     * @return 验证链实例
     */
    public static <T> Chain contains(T[] array, T o, ResponseCode code) {
        return begin().contains(array, o, code);
    }

    /**
     * 验证数组包含指定元素，失败时使用自定义错误构建器
     *
     * @param array    待验证数组
     * @param o        指定元素
     * @param consumer 错误构建器消费者
     * @param <T>      数组元素类型
     * @return 验证链实例
     */
    public static <T> Chain contains(T[] array, T o, Consumer<Business.Fabricator> consumer) {
        return begin().contains(array, o, consumer);
    }

    /**
     * 验证集合不包含指定元素
     *
     * @param col 待验证集合
     * @param o   指定元素
     * @return 验证链实例
     */
    public static Chain notContains(Collection<?> col, Object o) {
        return begin().notContains(col, o);
    }

    /**
     * 验证集合不包含指定元素，失败时使用指定错误码
     *
     * @param col  待验证集合
     * @param o    指定元素
     * @param code 错误码
     * @return 验证链实例
     */
    public static Chain notContains(Collection<?> col, Object o, ResponseCode code) {
        return begin().notContains(col, o, code);
    }

    /**
     * 验证集合不包含指定元素，失败时使用自定义错误构建器
     *
     * @param col      待验证集合
     * @param o        指定元素
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain notContains(Collection<?> col, Object o, Consumer<Business.Fabricator> consumer) {
        return begin().notContains(col, o, consumer);
    }

    /**
     * 验证数组不包含指定元素
     *
     * @param array 待验证数组
     * @param o     指定元素
     * @param <T>   数组元素类型
     * @return 验证链实例
     */
    public static <T> Chain notContains(T[] array, T o) {
        return begin().notContains(array, o);
    }

    /**
     * 验证数组不包含指定元素，失败时使用指定错误码
     *
     * @param array 待验证数组
     * @param o     指定元素
     * @param code  错误码
     * @param <T>   数组元素类型
     * @return 验证链实例
     */
    public static <T> Chain notContains(T[] array, T o, ResponseCode code) {
        return begin().notContains(array, o, code);
    }

    /**
     * 验证数组不包含指定元素，失败时使用自定义错误构建器
     *
     * @param array    待验证数组
     * @param o        指定元素
     * @param consumer 错误构建器消费者
     * @param <T>      数组元素类型
     * @return 验证链实例
     */
    public static <T> Chain notContains(T[] array, T o, Consumer<Business.Fabricator> consumer) {
        return begin().notContains(array, o, consumer);
    }

    // ==================== 数值 ====================

    /**
     * 验证数值必须为正数（大于0）
     *
     * @param value 待验证数值
     * @return 验证链实例
     */
    public static Chain positive(Number value) {
        return begin().positive(value);
    }

    /**
     * 验证数值必须为正数，失败时使用指定错误码
     *
     * @param value 待验证数值
     * @param code  错误码
     * @return 验证链实例
     */
    public static Chain positive(Number value, ResponseCode code) {
        return begin().positive(value, code);
    }

    /**
     * 验证数值必须为正数，失败时使用自定义错误构建器
     *
     * @param value    待验证数值
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain positive(Number value, Consumer<Business.Fabricator> consumer) {
        return begin().positive(value, consumer);
    }

    /**
     * 验证数值必须为正数（大于0），同 positive
     *
     * @param value 待验证数值
     * @return 验证链实例
     */
    public static Chain positiveNumber(Number value) {
        return begin().positiveNumber(value);
    }

    /**
     * 验证数值必须为正数，失败时使用指定错误码
     *
     * @param value 待验证数值
     * @param code  错误码
     * @return 验证链实例
     */
    public static Chain positiveNumber(Number value, ResponseCode code) {
        return begin().positiveNumber(value, code);
    }

    /**
     * 验证数值必须为正数，失败时使用自定义错误构建器
     *
     * @param value    待验证数值
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain positiveNumber(Number value, Consumer<Business.Fabricator> consumer) {
        return begin().positiveNumber(value, consumer);
    }

    /**
     * 验证数值在指定范围内（闭区间 [min, max]）
     *
     * @param value 待验证数值
     * @param min   最小值
     * @param max   最大值
     * @param <T>   数值类型
     * @return 验证链实例
     */
    public static <T extends Number & Comparable<T>> Chain inRange(T value, T min, T max) {
        return begin().inRange(value, min, max);
    }

    /**
     * 验证数值在指定范围内，失败时使用指定错误码
     *
     * @param value 待验证数值
     * @param min   最小值
     * @param max   最大值
     * @param code  错误码
     * @param <T>   数值类型
     * @return 验证链实例
     */
    public static <T extends Number & Comparable<T>> Chain inRange(T value, T min, T max, ResponseCode code) {
        return begin().inRange(value, min, max, code);
    }

    /**
     * 验证数值在指定范围内，失败时使用自定义错误构建器
     *
     * @param value    待验证数值
     * @param min      最小值
     * @param max      最大值
     * @param consumer 错误构建器消费者
     * @param <T>      数值类型
     * @return 验证链实例
     */
    public static <T extends Number & Comparable<T>> Chain inRange(T value, T min, T max, Consumer<Business.Fabricator> consumer) {
        return begin().inRange(value, min, max, consumer);
    }

    /**
     * 验证任意Number类型数值在指定范围内
     *
     * @param v   待验证数值
     * @param min 最小值
     * @param max 最大值
     * @return 验证链实例
     */
    public static Chain inRangeNumber(Number v, Number min, Number max) {
        return begin().inRangeNumber(v, min, max);
    }

    /**
     * 验证任意Number类型数值在指定范围内，失败时使用指定错误码
     *
     * @param v    待验证数值
     * @param min  最小值
     * @param max  最大值
     * @param code 错误码
     * @return 验证链实例
     */
    public static Chain inRangeNumber(Number v, Number min, Number max, ResponseCode code) {
        return begin().inRangeNumber(v, min, max, code);
    }

    /**
     * 验证任意Number类型数值在指定范围内，失败时使用自定义错误构建器
     *
     * @param v        待验证数值
     * @param min      最小值
     * @param max      最大值
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain inRangeNumber(Number v, Number min, Number max, Consumer<Business.Fabricator> consumer) {
        return begin().inRangeNumber(v, min, max, consumer);
    }

    /**
     * 验证数值必须为非负数（大于等于0）
     *
     * @param value 待验证数值
     * @param <T>   数值类型
     * @return 验证链实例
     */
    public static <T extends Number & Comparable<T>> Chain nonNegative(T value) {
        return begin().nonNegative(value);
    }

    /**
     * 验证数值必须为非负数，失败时使用指定错误码
     *
     * @param value 待验证数值
     * @param code  错误码
     * @param <T>   数值类型
     * @return 验证链实例
     */
    public static <T extends Number & Comparable<T>> Chain nonNegative(T value, ResponseCode code) {
        return begin().nonNegative(value, code);
    }

    /**
     * 验证数值必须为非负数，失败时使用自定义错误构建器
     *
     * @param value    待验证数值
     * @param consumer 错误构建器消费者
     * @param <T>      数值类型
     * @return 验证链实例
     */
    public static <T extends Number & Comparable<T>> Chain nonNegative(T value, Consumer<Business.Fabricator> consumer) {
        return begin().nonNegative(value, consumer);
    }

    // ==================== 日期 ====================

    /**
     * 验证日期是否在指定日期之后
     *
     * @param date1 待验证日期
     * @param date2 比较基准日期
     * @return 验证链实例
     */
    public static Chain after(Date date1, Date date2) {
        return begin().after(date1, date2);
    }

    /**
     * 验证日期是否在指定日期之后，失败时使用指定错误码
     *
     * @param date1 待验证日期
     * @param date2 比较基准日期
     * @param code  错误码
     * @return 验证链实例
     */
    public static Chain after(Date date1, Date date2, ResponseCode code) {
        return begin().after(date1, date2, code);
    }

    /**
     * 验证日期是否在指定日期之后，失败时使用自定义错误构建器
     *
     * @param date1    待验证日期
     * @param date2    比较基准日期
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain after(Date date1, Date date2, Consumer<Business.Fabricator> consumer) {
        return begin().after(date1, date2, consumer);
    }

    /**
     * 验证日期是否在指定日期之前
     *
     * @param date1 待验证日期
     * @param date2 比较基准日期
     * @return 验证链实例
     */
    public static Chain before(Date date1, Date date2) {
        return begin().before(date1, date2);
    }

    /**
     * 验证日期是否在指定日期之前，失败时使用指定错误码
     *
     * @param date1 待验证日期
     * @param date2 比较基准日期
     * @param code  错误码
     * @return 验证链实例
     */
    public static Chain before(Date date1, Date date2, ResponseCode code) {
        return begin().before(date1, date2, code);
    }

    /**
     * 验证日期是否在指定日期之前，失败时使用自定义错误构建器
     *
     * @param date1    待验证日期
     * @param date2    比较基准日期
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain before(Date date1, Date date2, Consumer<Business.Fabricator> consumer) {
        return begin().before(date1, date2, consumer);
    }

    // ==================== 枚举 ====================

    /**
     * 验证字符串值是否为指定枚举类的有效值
     *
     * @param enumType 枚举类类型
     * @param value    待验证字符串值
     * @param <E>      枚举类型
     * @return 验证链实例
     */
    public static <E extends Enum<E>> Chain enumValue(Class<E> enumType, String value) {
        return begin().enumValue(enumType, value);
    }

    /**
     * 验证字符串值是否为指定枚举类的有效值，失败时使用指定错误码
     *
     * @param enumType 枚举类类型
     * @param value    待验证字符串值
     * @param code     错误码
     * @param <E>      枚举类型
     * @return 验证链实例
     */
    public static <E extends Enum<E>> Chain enumValue(Class<E> enumType, String value, ResponseCode code) {
        return begin().enumValue(enumType, value, code);
    }

    /**
     * 验证字符串值是否为指定枚举类的有效值，失败时使用自定义错误构建器
     *
     * @param enumType 枚举类类型
     * @param value    待验证字符串值
     * @param consumer 错误构建器消费者
     * @param <E>      枚举类型
     * @return 验证链实例
     */
    public static <E extends Enum<E>> Chain enumValue(Class<E> enumType, String value, Consumer<Business.Fabricator> consumer) {
        return begin().enumValue(enumType, value, consumer);
    }

    // ==================== 对象同一性 ====================

    /**
     * 验证两个对象引用是否指向同一对象（==）
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 验证链实例
     */
    public static Chain same(Object obj1, Object obj2) {
        return begin().same(obj1, obj2);
    }

    /**
     * 验证两个对象引用是否指向同一对象，失败时使用指定错误码
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @param code 错误码
     * @return 验证链实例
     */
    public static Chain same(Object obj1, Object obj2, ResponseCode code) {
        return begin().same(obj1, obj2, code);
    }

    /**
     * 验证两个对象引用是否指向同一对象，失败时使用自定义错误构建器
     *
     * @param obj1     对象1
     * @param obj2     对象2
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain same(Object obj1, Object obj2, Consumer<Business.Fabricator> consumer) {
        return begin().same(obj1, obj2, consumer);
    }

    /**
     * 验证两个对象引用是否指向不同对象（!=）
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 验证链实例
     */
    public static Chain notSame(Object obj1, Object obj2) {
        return begin().notSame(obj1, obj2);
    }

    /**
     * 验证两个对象引用是否指向不同对象，失败时使用指定错误码
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @param code 错误码
     * @return 验证链实例
     */
    public static Chain notSame(Object obj1, Object obj2, ResponseCode code) {
        return begin().notSame(obj1, obj2, code);
    }

    /**
     * 验证两个对象引用是否指向不同对象，失败时使用自定义错误构建器
     *
     * @param obj1     对象1
     * @param obj2     对象2
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain notSame(Object obj1, Object obj2, Consumer<Business.Fabricator> consumer) {
        return begin().notSame(obj1, obj2, consumer);
    }

    /**
     * 验证两个对象是否相等（equals）
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 验证链实例
     */
    public static Chain equals(Object obj1, Object obj2) {
        return begin().equals(obj1, obj2);
    }

    /**
     * 验证两个对象是否相等，失败时使用指定错误码
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @param code 错误码
     * @return 验证链实例
     */
    public static Chain equals(Object obj1, Object obj2, ResponseCode code) {
        return begin().equals(obj1, obj2, code);
    }

    /**
     * 验证两个对象是否相等，失败时使用自定义错误构建器
     *
     * @param obj1     对象1
     * @param obj2     对象2
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain equals(Object obj1, Object obj2, Consumer<Business.Fabricator> consumer) {
        return begin().equals(obj1, obj2, consumer);
    }

    /**
     * 验证两个对象是否不相等（!equals）
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 验证链实例
     */
    public static Chain notEquals(Object obj1, Object obj2) {
        return begin().notEquals(obj1, obj2);
    }

    /**
     * 验证两个对象是否不相等，失败时使用指定错误码
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @param code 错误码
     * @return 验证链实例
     */
    public static Chain notEquals(Object obj1, Object obj2, ResponseCode code) {
        return begin().notEquals(obj1, obj2, code);
    }

    /**
     * 验证两个对象是否不相等，失败时使用自定义错误构建器
     *
     * @param obj1     对象1
     * @param obj2     对象2
     * @param consumer 错误构建器消费者
     * @return 验证链实例
     */
    public static Chain notEquals(Object obj1, Object obj2, Consumer<Business.Fabricator> consumer) {
        return begin().notEquals(obj1, obj2, consumer);
    }

}
