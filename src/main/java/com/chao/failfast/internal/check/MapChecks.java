package com.chao.failfast.internal.check;

import java.util.Map;

/**
 * Map校验工具类
 */
public final class MapChecks {
    private MapChecks() {}

    public static boolean notEmpty(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean containsKey(Map<?, ?> map, Object key) {
        return map != null && map.containsKey(key);
    }

    public static boolean notContainsKey(Map<?, ?> map, Object key) {
        return map == null || !map.containsKey(key);
    }

    public static boolean containsValue(Map<?, ?> map, Object value) {
        return map != null && map.containsValue(value);
    }

    public static boolean sizeBetween(Map<?, ?> map, int min, int max) {
        int size = (map == null) ? 0 : map.size();
        return size >= min && size <= max;
    }

    public static boolean sizeEquals(Map<?, ?> map, int size) {
        return map != null && map.size() == size;
    }
}
