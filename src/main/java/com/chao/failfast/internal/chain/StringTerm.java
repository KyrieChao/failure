package com.chao.failfast.internal.chain;

import com.chao.failfast.constant.FailureConst;
import com.chao.failfast.internal.check.StringChecks;
import com.chao.failfast.internal.core.ResponseCode;
import com.chao.failfast.internal.core.ViolationSpec;

import java.util.function.Consumer;

/**
 * 字符串校验接口
 */
public interface StringTerm<S extends ChainCore<S>> {

    S core();

    default S notBlank(String str, Consumer<ViolationSpec> spec) {
        return core().check(StringChecks.notBlank(str), spec);
    }

    default S notBlank(String str) {
        return notBlank(str, FailureConst.NO_OP);
    }

    default S notBlank(String str, ResponseCode code) {
        return notBlank(str, s -> s.responseCode(code));
    }

    default S notBlank(String str, ResponseCode code, String detail) {
        return notBlank(str, s -> s.responseCode(code).detail(detail));
    }

    // ========== notEmpty (alias) ==========

    default S notEmpty(String str, Consumer<ViolationSpec> spec) {
        return notBlank(str, spec);
    }

    default S notEmpty(String str) {
        return notBlank(str);
    }

    default S notEmpty(String str, ResponseCode code) {
        return notBlank(str, code);
    }

    default S notEmpty(String str, ResponseCode code, String detail) {
        return notBlank(str, code, detail);
    }

    // ========== blank ==========

    default S blank(String str, Consumer<ViolationSpec> spec) {
        return core().check(StringChecks.blank(str), spec);
    }

    default S blank(String str) {
        return blank(str, FailureConst.NO_OP);
    }

    default S blank(String str, ResponseCode code) {
        return blank(str, s -> s.responseCode(code));
    }

    default S blank(String str, ResponseCode code, String detail) {
        return blank(str, s -> s.responseCode(code).detail(detail));
    }

    // ========== lengthBetween ==========

    default S lengthBetween(String str, int min, int max, Consumer<ViolationSpec> spec) {
        if (!core().isAlive()) return core();
        return core().check(StringChecks.lengthBetween(str, min, max), spec);
    }

    default S lengthBetween(String str, int min, int max) {
        return lengthBetween(str, min, max, FailureConst.NO_OP);
    }

    default S lengthBetween(String str, int min, int max, ResponseCode code) {
        return lengthBetween(str, min, max, s -> s.responseCode(code));
    }

    default S lengthBetween(String str, int min, int max, ResponseCode code, String detail) {
        return lengthBetween(str, min, max, s -> s.responseCode(code).detail(detail));
    }

    // ========== lengthMin ==========

    default S lengthMin(String str, int min, Consumer<ViolationSpec> spec) {
        return core().check(StringChecks.lengthMin(str, min), spec);
    }

    default S lengthMin(String str, int min) {
        return lengthMin(str, min, FailureConst.NO_OP);
    }

    default S lengthMin(String str, int min, ResponseCode code) {
        return lengthMin(str, min, s -> s.responseCode(code));
    }

    default S lengthMin(String str, int min, ResponseCode code, String detail) {
        return lengthMin(str, min, s -> s.responseCode(code).detail(detail));
    }

    // ========== lengthMax ==========

    default S lengthMax(String str, int max, Consumer<ViolationSpec> spec) {
        return core().check(StringChecks.lengthMax(str, max), spec);
    }

    default S lengthMax(String str, int max) {
        return lengthMax(str, max, FailureConst.NO_OP);
    }

    default S lengthMax(String str, int max, ResponseCode code) {
        return lengthMax(str, max, s -> s.responseCode(code));
    }

    default S lengthMax(String str, int max, ResponseCode code, String detail) {
        return lengthMax(str, max, s -> s.responseCode(code).detail(detail));
    }

    // ========== match ==========

    default S match(String str, String regex, Consumer<ViolationSpec> spec) {
        if (!core().isAlive()) return core();
        return core().check(StringChecks.match(str, regex), spec);
    }

    default S match(String str, String regex) {
        return match(str, regex, FailureConst.NO_OP);
    }

    default S match(String str, String regex, ResponseCode code) {
        return match(str, regex, s -> s.responseCode(code));
    }

    default S match(String str, String regex, ResponseCode code, String detail) {
        return match(str, regex, s -> s.responseCode(code).detail(detail));
    }

    // ========== email ==========

    default S email(String email, Consumer<ViolationSpec> spec) {
        return core().check(StringChecks.email(email), spec);
    }

    default S email(String email) {
        return email(email, FailureConst.NO_OP);
    }

    default S email(String email, ResponseCode code) {
        return email(email, s -> s.responseCode(code));
    }

    default S email(String email, ResponseCode code, String detail) {
        return email(email, s -> s.responseCode(code).detail(detail));
    }

    // ========== mobile ==========

    default S mobile(String str, Consumer<ViolationSpec> spec) {
        return core().check(StringChecks.mobile(str), spec);
    }

    default S mobile(String str) {
        return mobile(str, FailureConst.NO_OP);
    }

    default S mobile(String str, ResponseCode code) {
        return mobile(str, s -> s.responseCode(code));
    }

    default S mobile(String str, ResponseCode code, String detail) {
        return mobile(str, s -> s.responseCode(code).detail(detail));
    }

    // ========== url ==========

    default S url(String str, Consumer<ViolationSpec> spec) {
        return core().check(StringChecks.url(str), spec);
    }

    default S url(String str) {
        return url(str, FailureConst.NO_OP);
    }

    default S url(String str, ResponseCode code) {
        return url(str, s -> s.responseCode(code));
    }

    default S url(String str, ResponseCode code, String detail) {
        return url(str, s -> s.responseCode(code).detail(detail));
    }

    // ========== ipAddress ==========

    default S ipAddress(String str, Consumer<ViolationSpec> spec) {
        return core().check(StringChecks.ipAddress(str), spec);
    }

    default S ipAddress(String str) {
        return ipAddress(str, FailureConst.NO_OP);
    }

    default S ipAddress(String str, ResponseCode code) {
        return ipAddress(str, s -> s.responseCode(code));
    }

    default S ipAddress(String str, ResponseCode code, String detail) {
        return ipAddress(str, s -> s.responseCode(code).detail(detail));
    }

    // ========== uuid ==========

    default S uuid(String str, Consumer<ViolationSpec> spec) {
        return core().check(StringChecks.uuid(str), spec);
    }

    default S uuid(String str) {
        return uuid(str, FailureConst.NO_OP);
    }

    default S uuid(String str, ResponseCode code) {
        return uuid(str, s -> s.responseCode(code));
    }

    default S uuid(String str, ResponseCode code, String detail) {
        return uuid(str, s -> s.responseCode(code).detail(detail));
    }

    // ========== isNumeric ==========

    default S isNumeric(String str, Consumer<ViolationSpec> spec) {
        return core().check(StringChecks.isNumeric(str), spec);
    }

    default S isNumeric(String str) {
        return isNumeric(str, FailureConst.NO_OP);
    }

    default S isNumeric(String str, ResponseCode code) {
        return isNumeric(str, s -> s.responseCode(code));
    }

    default S isNumeric(String str, ResponseCode code, String detail) {
        return isNumeric(str, s -> s.responseCode(code).detail(detail));
    }

    // ========== isAlpha ==========

    default S isAlpha(String str, Consumer<ViolationSpec> spec) {
        return core().check(StringChecks.isAlpha(str), spec);
    }

    default S isAlpha(String str) {
        return isAlpha(str, FailureConst.NO_OP);
    }

    default S isAlpha(String str, ResponseCode code) {
        return isAlpha(str, s -> s.responseCode(code));
    }

    default S isAlpha(String str, ResponseCode code, String detail) {
        return isAlpha(str, s -> s.responseCode(code).detail(detail));
    }

    // ========== isAlphanumeric ==========

    default S isAlphanumeric(String str, Consumer<ViolationSpec> spec) {
        return core().check(StringChecks.isAlphanumeric(str), spec);
    }

    default S isAlphanumeric(String str) {
        return isAlphanumeric(str, FailureConst.NO_OP);
    }

    default S isAlphanumeric(String str, ResponseCode code) {
        return isAlphanumeric(str, s -> s.responseCode(code));
    }

    default S isAlphanumeric(String str, ResponseCode code, String detail) {
        return isAlphanumeric(str, s -> s.responseCode(code).detail(detail));
    }

    // ========== startsWith ==========

    default S startsWith(String str, String prefix, Consumer<ViolationSpec> spec) {
        return core().check(StringChecks.startsWith(str, prefix), spec);
    }

    default S startsWith(String str, String prefix) {
        return startsWith(str, prefix, FailureConst.NO_OP);
    }

    default S startsWith(String str, String prefix, ResponseCode code) {
        return startsWith(str, prefix, s -> s.responseCode(code));
    }

    default S startsWith(String str, String prefix, ResponseCode code, String detail) {
        return startsWith(str, prefix, s -> s.responseCode(code).detail(detail));
    }

    // ========== endsWith ==========

    default S endsWith(String str, String suffix, Consumer<ViolationSpec> spec) {
        return core().check(StringChecks.endsWith(str, suffix), spec);
    }

    default S endsWith(String str, String suffix) {
        return endsWith(str, suffix, FailureConst.NO_OP);
    }

    default S endsWith(String str, String suffix, ResponseCode code) {
        return endsWith(str, suffix, s -> s.responseCode(code));
    }

    default S endsWith(String str, String suffix, ResponseCode code, String detail) {
        return endsWith(str, suffix, s -> s.responseCode(code).detail(detail));
    }

    // ========== contains ==========

    default S contains(String str, String substring, Consumer<ViolationSpec> spec) {
        return core().check(StringChecks.contains(str, substring), spec);
    }

    default S contains(String str, String substring) {
        return contains(str, substring, FailureConst.NO_OP);
    }

    default S contains(String str, String substring, ResponseCode code) {
        return contains(str, substring, s -> s.responseCode(code));
    }

    default S contains(String str, String substring, ResponseCode code, String detail) {
        return contains(str, substring, s -> s.responseCode(code).detail(detail));
    }

    // ========== notContains ==========

    default S notContains(String str, String substring, Consumer<ViolationSpec> spec) {
        return core().check(StringChecks.notContains(str, substring), spec);
    }

    default S notContains(String str, String substring) {
        return notContains(str, substring, FailureConst.NO_OP);
    }

    default S notContains(String str, String substring, ResponseCode code) {
        return notContains(str, substring, s -> s.responseCode(code));
    }

    default S notContains(String str, String substring, ResponseCode code, String detail) {
        return notContains(str, substring, s -> s.responseCode(code).detail(detail));
    }
    // 在 StringValidation 接口中添加：

// ========== isLowerCase ==========

    default S isLowerCase(String str, Consumer<ViolationSpec> spec) {
        return core().check(StringChecks.isLowerCase(str), spec);
    }

    default S isLowerCase(String str) {
        return isLowerCase(str, FailureConst.NO_OP);
    }

    default S isLowerCase(String str, ResponseCode code) {
        return isLowerCase(str, s -> s.responseCode(code));
    }

    default S isLowerCase(String str, ResponseCode code, String detail) {
        return isLowerCase(str, s -> s.responseCode(code).detail(detail));
    }

// ========== isUpperCase ==========

    default S isUpperCase(String str, Consumer<ViolationSpec> spec) {
        return core().check(StringChecks.isUpperCase(str), spec);
    }

    default S isUpperCase(String str) {
        return isUpperCase(str, FailureConst.NO_OP);
    }

    default S isUpperCase(String str, ResponseCode code) {
        return isUpperCase(str, s -> s.responseCode(code));
    }

    default S isUpperCase(String str, ResponseCode code, String detail) {
        return isUpperCase(str, s -> s.responseCode(code).detail(detail));
    }

    // ========== equalsIgnoreCase ==========

    default S equalsIgnoreCase(String str1, String str2, Consumer<ViolationSpec> spec) {
        return core().check(StringChecks.equalsIgnoreCase(str1, str2), spec);
    }

    default S equalsIgnoreCase(String str1, String str2) {
        return equalsIgnoreCase(str1, str2, FailureConst.NO_OP);
    }

    default S equalsIgnoreCase(String str1, String str2, ResponseCode code) {
        return equalsIgnoreCase(str1, str2, s -> s.responseCode(code));
    }

    default S equalsIgnoreCase(String str1, String str2, ResponseCode code, String detail) {
        return equalsIgnoreCase(str1, str2, s -> s.responseCode(code).detail(detail));
    }
}