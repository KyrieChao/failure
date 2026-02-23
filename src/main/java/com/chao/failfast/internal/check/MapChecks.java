package com.chao.failfast.internal.check;

import java.util.Map;

/**
 * Map校验工具类
 * 提供了一系列用于检查Map对象状态和内容的静态方法
 */
public final class MapChecks {
    // 私有构造方法，防止实例化工具类
    private MapChecks() {}

    /**
     * 检查Map是否不为空
     * @param map 要检查的Map对象
     * @return 如果Map不为null且不为空则返回true，否则返回false
     */
    public static boolean notEmpty(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    /**
     * 检查Map是否为空
     * @param map 要检查的Map对象
     * @return 如果Map为null或为空则返回true，否则返回false
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 检查Map是否包含指定的键
     * @param map 要检查的Map对象
     * @param key 要检查的键
     * @return 如果Map不为null且包含指定的键则返回true，否则返回false
     */
    public static boolean containsKey(Map<?, ?> map, Object key) {
        return map != null && map.containsKey(key);
    }

    /**
     * 检查Map是否不包含指定的键
     * @param map 要检查的Map对象
     * @param key 要检查的键
     * @return 如果Map为null或不包含指定的键则返回true，否则返回false
     */
    public static boolean notContainsKey(Map<?, ?> map, Object key) {
        return map == null || !map.containsKey(key);
    }

    /**
     * 检查Map是否包含指定的值
     * @param map 要检查的Map对象
     * @param value 要检查的值
     * @return 如果Map不为null且包含指定的值则返回true，否则返回false
     */
    public static boolean containsValue(Map<?, ?> map, Object value) {
        return map != null && map.containsValue(value);
    }

    /**
     * 检查Map的大小是否在指定范围内
     * @param map 要检查的Map对象
     * @param min 最小大小（包含）
     * @param max 最大大小（包含）
     * @return 如果Map的大小在[min,max]范围内则返回true，否则返回false
     */
    public static boolean sizeBetween(Map<?, ?> map, int min, int max) {
        int size = (map == null) ? 0 : map.size();
        return size >= min && size <= max;
    }

    /**
     * 检查Map的大小是否等于指定值
     * @param map 要检查的Map对象
     * @param size 期望的大小
     * @return 如果Map不为null且大小等于指定值则返回true，否则返回false
     */
    public static boolean sizeEquals(Map<?, ?> map, int size) {
        return map != null && map.size() == size;
    }
}
