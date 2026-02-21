package com.chao.failfast.internal.check;

import java.util.Collection;

/**
 * 集合校验工具类
 */
public final class CollectionChecks {

    private CollectionChecks() {}

    public static boolean notEmpty(Collection<?> col) {
        return col != null && !col.isEmpty();
    }

    public static boolean sizeBetween(Collection<?> col, int min, int max) {
        int size = (col == null) ? 0 : col.size();
        return size >= min && size <= max;
    }

    public static boolean sizeEquals(Collection<?> col, int expectedSize) {
        return col != null && col.size() == expectedSize;
    }

    public static boolean contains(Collection<?> col, Object o) {
        return col != null && col.contains(o);
    }

    public static boolean notContains(Collection<?> col, Object o) {
        return col == null || !col.contains(o);
    }
}
