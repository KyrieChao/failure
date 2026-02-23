package com.chao.failfast.internal.check;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * 数组校验工具类
 * 提供了一系列用于检查数组状态的方法，包括非空判断、长度检查、元素存在性检查等
 */
public final class ArrayChecks {

    // 私有构造方法，防止实例化工具类
    private ArrayChecks() {
    }

    /**
     * 检查数组是否不为空
     * @param array 要检查的数组
     * @return 如果数组不为null且长度大于0则返回true，否则返回false
     */
    public static <T> boolean notEmpty(T[] array) {
        return array != null && array.length > 0;
    }

    /**
     * 检查数组是否为空
     * @param array 要检查的数组
     * @return 如果数组为null或长度为0则返回true，否则返回false
     */
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 检查数组长度是否在指定范围内
     * @param array 要检查的数组
     * @param min 最小长度（包含）
     * @param max 最大长度（包含）
     * @return 如果数组长度在[min,max]范围内则返回true，否则返回false
     */
    public static <T> boolean sizeBetween(T[] array, int min, int max) {
        int len = (array == null) ? 0 : array.length;
        return len >= min && len <= max;
    }

    /**
     * 检查数组长度是否等于预期值
     * @param array 要检查的数组
     * @param expectedSize 预期的数组长度
     * @return 如果数组不为null且长度等于expectedSize则返回true，否则返回false
     */
    public static <T> boolean sizeEquals(T[] array, int expectedSize) {
        return array != null && array.length == expectedSize;
    }

    /**
     * 检查数组是否包含指定元素
     * @param array 要检查的数组
     * @param o 要查找的元素
     * @return 如果数组包含元素o则返回true，否则返回false
     */
    public static <T> boolean contains(T[] array, T o) {
        if (array == null) return false;
        for (T element : array) {
            if (Objects.equals(element, o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查数组是否不包含指定元素
     * @param array 要检查的数组
     * @param o 要排除的元素
     * @return 如果数组不包含元素o则返回true，否则返回false
     */
    public static <T> boolean notContains(T[] array, T o) {
        return !contains(array, o);
    }

    /**
     * 检查数组是否不包含null元素
     * @param array 要检查的数组
     * @return 如果数组为null或所有元素都不为null则返回true，否则返回false
     */
    public static <T> boolean hasNoNullElements(T[] array) {
        if (array == null) return true;
        for (T element : array) {
            if (element == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查数组是否所有元素都满足指定条件
     * @param array 要检查的数组
     * @param predicate 判断条件
     * @return 如果数组所有元素都满足predicate条件则返回true，否则返回false
     */
    public static <T> boolean allMatch(T[] array, Predicate<T> predicate) {
        if (array == null || predicate == null) return false;
        for (T t : array) {
            if (!predicate.test(t)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查数组是否存在至少一个元素满足指定条件
     * @param array 要检查的数组
     * @param predicate 判断条件
     * @return 如果数组存在至少一个元素满足predicate条件则返回true，否则返回false
     */
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
