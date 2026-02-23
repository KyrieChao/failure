package com.chao.failfast.internal.check;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * 集合校验工具类
 * 提供了一系列用于校验集合状态的方法，包括判断集合是否为空、大小是否在指定范围内、是否包含特定元素等
 */
public final class CollectionChecks {

    // 私有构造方法，防止实例化工具类
    private CollectionChecks() {}

    /**
     * 检查集合是否不为空
     * @param col 要检查的集合
     * @return 如果集合不为null且不为空则返回true，否则返回false
     */
    public static boolean notEmpty(Collection<?> col) {
        return col != null && !col.isEmpty();
    }
    
    /**
     * 检查集合是否为空
     * @param col 要检查的集合
     * @return 如果集合为null或为空则返回true，否则返回false
     */
    public static boolean isEmpty(Collection<?> col) {
        return col == null || col.isEmpty();
    }

    /**
     * 检查集合大小是否在指定范围内
     * @param col 要检查的集合
     * @param min 最小大小（包含）
     * @param max 最大大小（包含）
     * @return 如果集合大小在[min,max]范围内则返回true，否则返回false
     */
    public static boolean sizeBetween(Collection<?> col, int min, int max) {
        int size = (col == null) ? 0 : col.size();
        return size >= min && size <= max;
    }

    /**
     * 检查集合大小是否等于预期值
     * @param col 要检查的集合
     * @param expectedSize 预期的大小
     * @return 如果集合不为null且大小等于预期值则返回true，否则返回false
     */
    public static boolean sizeEquals(Collection<?> col, int expectedSize) {
        return col != null && col.size() == expectedSize;
    }

    /**
     * 检查集合是否包含指定元素
     * @param col 要检查的集合
     * @param o 要查找的元素
     * @return 如果集合不为null且包含指定元素则返回true，否则返回false
     */
    public static boolean contains(Collection<?> col, Object o) {
        return col != null && col.contains(o);
    }

    /**
     * 检查集合是否不包含指定元素
     * @param col 要检查的集合
     * @param o 要排除的元素
     * @return 如果集合为null或不包含指定元素则返回true，否则返回false
     */
    public static boolean notContains(Collection<?> col, Object o) {
        return col == null || !col.contains(o);
    }

    /**
     * 检查集合中是否不包含null元素
     * @param col 要检查的集合
     * @return 如果集合为null或没有null元素则返回true，否则返回false
     */
    public static boolean hasNoNullElements(Collection<?> col) {
        if (col == null) return true;
        for (Object element : col) {
            if (element == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查集合中所有元素是否都满足指定条件
     * @param col 要检查的集合
     * @param predicate 判断条件
     * @return 如果集合和条件都不为null且所有元素都满足条件则返回true，否则返回false
     */
    public static <T> boolean allMatch(Collection<T> col, Predicate<T> predicate) {
        if (col == null || predicate == null) return false;
        for (T t : col) {
            if (!predicate.test(t)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查集合中是否存在至少一个元素满足指定条件
     * @param col 要检查的集合
     * @param predicate 判断条件
     * @return 如果集合和条件都不为null且至少有一个元素满足条件则返回true，否则返回false
     */
    public static <T> boolean anyMatch(Collection<T> col, Predicate<T> predicate) {
        if (col == null || predicate == null) return false;
        for (T t : col) {
            if (predicate.test(t)) {
                return true;
            }
        }
        return false;
    }
}
