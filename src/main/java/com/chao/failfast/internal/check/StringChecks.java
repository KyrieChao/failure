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

    public static boolean contains(String str, String substring) {
        return str != null && substring != null && str.contains(substring);
    }

    public static boolean notContains(String str, String substring) {
        return str == null || substring == null || !str.contains(substring);
    }

    public static boolean lengthMin(String str, int min) {
        return str != null && str.length() >= min;
    }

    public static boolean lengthMax(String str, int max) {
        return str != null && str.length() <= max;
    }

    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlpha(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphanumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isLowerCase(String str) {
        return str != null && str.equals(str.toLowerCase());
    }

    public static boolean isUpperCase(String str) {
        return str != null && str.equals(str.toUpperCase());
    }

    public static boolean mobile(String str) {
        return str != null && str.matches("^1[3-9]\\d{9}$");
    }

    public static boolean url(String str) {
        // Simple regex for URL validation
        return str != null && str.matches("^(http|https)://.*$");
    }

    public static boolean ipAddress(String str) {
        // Simple regex for IPv4
        return str != null && str.matches("^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$");
    }

    public static boolean uuid(String str) {
        return str != null && str.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    }
}
