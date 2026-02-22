package com.chao.failfast.internal.check;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ArrayChecks 工具类测试")
class ArrayChecksTest {

    @Nested
    @DisplayName("notEmpty 方法测试")
    class NotEmptyTest {
        @Test
        @DisplayName("当数组非空时应返回true")
        void shouldReturnTrueWhenArrayIsNotEmpty() {
            assertThat(ArrayChecks.notEmpty(new String[]{"a"})).isTrue();
        }

        @Test
        @DisplayName("当数组为空或null时应返回false")
        void shouldReturnFalseWhenArrayIsEmptyOrNull() {
            assertThat(ArrayChecks.notEmpty(new String[]{})).isFalse();
            assertThat(ArrayChecks.notEmpty(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("sizeBetween 方法测试")
    class SizeBetweenTest {
        @Test
        @DisplayName("当数组大小在范围内时应返回true")
        void shouldReturnTrueWhenSizeIsInRange() {
            String[] arr = {"a", "b", "c"};
            assertThat(ArrayChecks.sizeBetween(arr, 1, 3)).isTrue();
            assertThat(ArrayChecks.sizeBetween(arr, 3, 5)).isTrue();
        }

        @Test
        @DisplayName("当数组大小不在范围内时应返回false")
        void shouldReturnFalseWhenSizeIsOutOfRange() {
            String[] arr = {"a", "b"};
            assertThat(ArrayChecks.sizeBetween(arr, 3, 5)).isFalse();
        }

        @Test
        @DisplayName("当数组为null时视作大小为0")
        void shouldTreatNullAsSizeZero() {
            assertThat(ArrayChecks.sizeBetween(null, 0, 0)).isTrue();
            assertThat(ArrayChecks.sizeBetween(null, 1, 5)).isFalse();
        }
    }

    @Nested
    @DisplayName("sizeEquals 方法测试")
    class SizeEqualsTest {
        @Test
        @DisplayName("当数组大小等于预期值时应返回true")
        void shouldReturnTrueWhenSizeEqualsExpected() {
            String[] arr = {"a", "b"};
            assertThat(ArrayChecks.sizeEquals(arr, 2)).isTrue();
        }

        @Test
        @DisplayName("当数组大小不等于预期值时应返回false")
        void shouldReturnFalseWhenSizeDoesNotEqualExpected() {
            String[] arr = {"a", "b"};
            assertThat(ArrayChecks.sizeEquals(arr, 3)).isFalse();
        }

        @Test
        @DisplayName("当数组为null时应返回false")
        void shouldReturnFalseWhenArrayIsNull() {
            assertThat(ArrayChecks.sizeEquals(null, 0)).isFalse();
        }
    }

    @Nested
    @DisplayName("contains 方法测试")
    class ContainsTest {
        @Test
        @DisplayName("当数组包含元素时应返回true")
        void shouldReturnTrueWhenArrayContainsElement() {
            String[] arr = {"a", "b"};
            assertThat(ArrayChecks.contains(arr, "a")).isTrue();
        }

        @Test
        @DisplayName("当数组不包含元素时应返回false")
        void shouldReturnFalseWhenArrayDoesNotContainElement() {
            String[] arr = {"a", "b"};
            assertThat(ArrayChecks.contains(arr, "c")).isFalse();
        }

        @Test
        @DisplayName("当数组为null时应返回false")
        void shouldReturnFalseWhenArrayIsNull() {
            assertThat(ArrayChecks.contains(null, "a")).isFalse();
        }
    }

    @Nested
    @DisplayName("notContains 方法测试")
    class NotContainsTest {
        @Test
        @DisplayName("当数组不包含元素时应返回true")
        void shouldReturnTrueWhenArrayDoesNotContainElement() {
            String[] arr = {"a", "b"};
            assertThat(ArrayChecks.notContains(arr, "c")).isTrue();
        }

        @Test
        @DisplayName("当数组包含元素时应返回false")
        void shouldReturnFalseWhenArrayContainsElement() {
            String[] arr = {"a", "b"};
            assertThat(ArrayChecks.notContains(arr, "a")).isFalse();
        }

        @Test
        @DisplayName("当数组为null时应返回true")
        void shouldReturnTrueWhenArrayIsNull() {
            assertThat(ArrayChecks.notContains(null, "a")).isTrue();
        }
    }
}
