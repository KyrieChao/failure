package com.chao.failfast.internal.check;

import java.util.regex.Pattern;

/**
 * 字符串校验工具类
 */
public final class StringChecks {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private StringChecks() {}

    public static boolean blank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean notBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean lengthBetween(String str, int min, int max) {
        return str != null && str.length() >= min && str.length() <= max;
    }

    public static boolean match(String str, String regex) {
        return str != null && str.matches(regex);
    }

    public static boolean email(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 != null && str1.equalsIgnoreCase(str2);
    }

    public static boolean startsWith(String str, String prefix) {
        return str != null && str.startsWith(prefix);
    }

    public static boolean endsWith(String str, String suffix) {
        return str != null && str.endsWith(suffix);
    }
}
