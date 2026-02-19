package com.chao.failfast.internal.chain;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.ResponseCode;

import java.util.function.Consumer;

/**
 * 字符串校验链
 * 提供字符串相关的校验功能，如空值、长度、正则、格式等
 */
public abstract class StringChain<C extends StringChain<C>> extends BooleanChain<C> {

    protected StringChain(boolean failFast) {
        super(failFast);
    }

    /**
     * 验证字符串必须为空白（null、空串或全空格）
     *
     * @param str 待验证字符串
     * @return 当前链实例
     */
    public C blank(String str) {
        return check(str == null || str.trim().isEmpty());
    }

    /**
     * 验证字符串必须为空白，失败时使用指定错误码
     *
     * @param str  待验证字符串
     * @param code 错误码
     * @return 当前链实例
     */
    public C blank(String str, ResponseCode code) {
        return check(str == null || str.trim().isEmpty(), code);
    }

    /**
     * 验证字符串必须为空白，失败时使用自定义错误构建器
     *
     * @param str      待验证字符串
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C blank(String str, Consumer<Business.Fabricator> consumer) {
        return check(str == null || str.trim().isEmpty(), consumer);
    }

    /**
     * 验证字符串不能为 null 且不能全为空格
     *
     * @param str 待验证字符串
     * @return 当前链实例
     */
    public C notBlank(String str) {
        return check(str != null && !str.trim().isEmpty());
    }

    /**
     * 验证字符串不能为 null 且不能全为空格，失败时使用指定错误码
     *
     * @param str  待验证字符串
     * @param code 错误码
     * @return 当前链实例
     */
    public C notBlank(String str, ResponseCode code) {
        return check(str != null && !str.trim().isEmpty(), code);
    }

    /**
     * 验证字符串不能为 null 且不能全为空格，失败时使用自定义错误构建器
     *
     * @param str      待验证字符串
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C notBlank(String str, Consumer<Business.Fabricator> consumer) {
        return check(str != null && !str.trim().isEmpty(), consumer);
    }

    /**
     * 验证字符串不能为 null 且不能为空串
     *
     * @param str 待验证字符串
     * @return 当前链实例
     */
    public C notEmpty(String str) {
        return notBlank(str);
    }

    /**
     * 验证字符串不能为 null 且不能为空串，失败时使用指定错误码
     *
     * @param str  待验证字符串
     * @param code 错误码
     * @return 当前链实例
     */
    public C notEmpty(String str, ResponseCode code) {
        return notBlank(str, code);
    }

    /**
     * 验证字符串不能为 null 且不能为空串，失败时使用自定义错误构建器
     *
     * @param str      待验证字符串
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C notEmpty(String str, Consumer<Business.Fabricator> consumer) {
        return notBlank(str, consumer);
    }

    /**
     * 验证字符串长度在指定范围内
     *
     * @param str 待验证字符串
     * @param min 最小长度
     * @param max 最大长度
     * @return 当前链实例
     */
    public C lengthBetween(String str, int min, int max) {
        if (!alive) return self();
        boolean ok = str != null && str.length() >= min && str.length() <= max;
        return check(ok);
    }

    /**
     * 验证字符串长度在指定范围内，失败时使用指定错误码
     *
     * @param str  待验证字符串
     * @param min  最小长度
     * @param max  最大长度
     * @param code 错误码
     * @return 当前链实例
     */
    public C lengthBetween(String str, int min, int max, ResponseCode code) {
        if (shouldSkip()) return self();
        boolean ok = str != null && str.length() >= min && str.length() <= max;
        return check(ok, code);
    }

    /**
     * 验证字符串长度在指定范围内，失败时使用自定义错误构建器
     *
     * @param str      待验证字符串
     * @param min      最小长度
     * @param max      最大长度
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C lengthBetween(String str, int min, int max, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return self();
        boolean ok = str != null && str.length() >= min && str.length() <= max;
        return check(ok, consumer);
    }

    /**
     * 验证字符串是否匹配指定正则表达式
     *
     * @param str   待验证字符串
     * @param regex 正则表达式
     * @return 当前链实例
     */
    public C match(String str, String regex) {
        if (!alive) return self();
        boolean ok = str != null && str.matches(regex);
        return check(ok);
    }

    /**
     * 验证字符串是否匹配指定正则表达式，失败时使用指定错误码
     *
     * @param str   待验证字符串
     * @param regex 正则表达式
     * @param code  错误码
     * @return 当前链实例
     */
    public C match(String str, String regex, ResponseCode code) {
        if (shouldSkip()) return self();
        boolean ok = str != null && str.matches(regex);
        return check(ok, code);
    }

    /**
     * 验证字符串是否匹配指定正则表达式，失败时使用自定义错误构建器
     *
     * @param str      待验证字符串
     * @param regex    正则表达式
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C match(String str, String regex, Consumer<Business.Fabricator> consumer) {
        if (shouldSkip()) return self();
        boolean ok = str != null && str.matches(regex);
        return check(ok, consumer);
    }

    /**
     * 验证字符串是否为有效的邮箱格式
     *
     * @param email 待验证邮箱字符串
     * @return 当前链实例
     */
    public C email(String email) {
        return match(email, "^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /**
     * 验证字符串是否为有效的邮箱格式，失败时使用指定错误码
     *
     * @param email 待验证邮箱字符串
     * @param code  错误码
     * @return 当前链实例
     */
    public C email(String email, ResponseCode code) {
        return match(email, "^[A-Za-z0-9+_.-]+@(.+)$", code);
    }

    /**
     * 验证字符串是否为有效的邮箱格式，失败时使用自定义错误构建器
     *
     * @param email    待验证邮箱字符串
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C email(String email, Consumer<Business.Fabricator> consumer) {
        return match(email, "^[A-Za-z0-9+_.-]+@(.+)$", consumer);
    }

    /**
     * 验证两个字符串是否相等（忽略大小写）
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 当前链实例
     */
    public C equalsIgnoreCase(String str1, String str2) {
        return check(str1 != null && str1.equalsIgnoreCase(str2));
    }

    /**
     * 验证两个字符串是否相等（忽略大小写），失败时使用指定错误码
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @param code 错误码
     * @return 当前链实例
     */
    public C equalsIgnoreCase(String str1, String str2, ResponseCode code) {
        return check(str1 != null && str1.equalsIgnoreCase(str2), code);
    }

    /**
     * 验证两个字符串是否相等（忽略大小写），失败时使用自定义错误构建器
     *
     * @param str1     字符串1
     * @param str2     字符串2
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C equalsIgnoreCase(String str1, String str2, Consumer<Business.Fabricator> consumer) {
        return check(str1 != null && str1.equalsIgnoreCase(str2), consumer);
    }

    /**
     * 验证字符串是否以指定前缀开头
     *
     * @param str    待验证字符串
     * @param prefix 前缀
     * @return 当前链实例
     */
    public C startsWith(String str, String prefix) {
        return check(str != null && str.startsWith(prefix));
    }

    /**
     * 验证字符串是否以指定前缀开头，失败时使用指定错误码
     *
     * @param str    待验证字符串
     * @param prefix 前缀
     * @param code   错误码
     * @return 当前链实例
     */
    public C startsWith(String str, String prefix, ResponseCode code) {
        return check(str != null && str.startsWith(prefix), code);
    }

    /**
     * 验证字符串是否以指定前缀开头，失败时使用自定义错误构建器
     *
     * @param str      待验证字符串
     * @param prefix   前缀
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C startsWith(String str, String prefix, Consumer<Business.Fabricator> consumer) {
        return check(str != null && str.startsWith(prefix), consumer);
    }

    /**
     * 验证字符串是否以指定后缀结尾
     *
     * @param str    待验证字符串
     * @param suffix 后缀
     * @return 当前链实例
     */
    public C endsWith(String str, String suffix) {
        return check(str != null && str.endsWith(suffix));
    }

    /**
     * 验证字符串是否以指定后缀结尾，失败时使用指定错误码
     *
     * @param str    待验证字符串
     * @param suffix 后缀
     * @param code   错误码
     * @return 当前链实例
     */
    public C endsWith(String str, String suffix, ResponseCode code) {
        return check(str != null && str.endsWith(suffix), code);
    }

    /**
     * 验证字符串是否以指定后缀结尾，失败时使用自定义错误构建器
     *
     * @param str      待验证字符串
     * @param suffix   后缀
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C endsWith(String str, String suffix, Consumer<Business.Fabricator> consumer) {
        return check(str != null && str.endsWith(suffix), consumer);
    }
}
