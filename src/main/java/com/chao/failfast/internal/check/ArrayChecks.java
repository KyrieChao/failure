package com.chao.failfast.internal.check;

import java.util.Objects;

import java.util.function.Predicate;

/**
 * 数组校验工具类
 */
public final class ArrayChecks {

    private ArrayChecks() {}

    public static <T> boolean notEmpty(T[] array) {
        return array != null && array.length > 0;
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static <T> boolean sizeBetween(T[] array, int min, int max) {
        int len = (array == null) ? 0 : array.length;
        return len >= min && len <= max;
    }

    public static <T> boolean sizeEquals(T[] array, int expectedSize) {
        return array != null && array.length == expectedSize;
    }

    public static <T> boolean contains(T[] array, T o) {
        if (array == null) return false;
        for (T element : array) {
            if (element == o || (element != null && element.equals(o))) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean notContains(T[] array, T o) {
        return !contains(array, o);
    }

    public static <T> boolean hasNoNullElements(T[] array) {
        if (array == null) return true;
        for (T element : array) {
            if (element == null) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean allMatch(T[] array, Predicate<T> predicate) {
        if (array == null || predicate == null) return false;
        for (T t : array) {
            if (!predicate.test(t)) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean anyMatch(T[] array, Predicate<T> predicate) {
        if (array == null || predicate == null) return false;
        for (T t : array) {
            if (predicate.test(t)) {
                return true;
            }
        }
        return false;
    }
}
