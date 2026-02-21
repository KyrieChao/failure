package com.chao.failfast.internal.check;

import java.util.Objects;

/**
 * 数组校验工具类
 */
public final class ArrayChecks {

    private ArrayChecks() {}

    public static <T> boolean notEmpty(T[] array) {
        return array != null && array.length > 0;
    }

    public static <T> boolean sizeBetween(T[] array, int min, int max) {
        int size = (array == null) ? 0 : array.length;
        return size >= min && size <= max;
    }

    public static <T> boolean sizeEquals(T[] array, int expectedSize) {
        return array != null && array.length == expectedSize;
    }

    public static <T> boolean contains(T[] array, T o) {
        if (array == null) return false;
        for (T item : array) {
            if (Objects.equals(item, o)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean notContains(T[] array, T o) {
        return !contains(array, o);
    }
}
