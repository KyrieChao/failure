package com.chao.failfast.internal.check;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CollectionChecks 工具类测试")
class CollectionChecksTest {

    @Nested
    @DisplayName("notEmpty 方法测试")
    class NotEmptyTest {
        @Test
        @DisplayName("当集合非空时应返回true")
        void shouldReturnTrueWhenCollectionIsNotEmpty() {
            assertThat(CollectionChecks.notEmpty(Collections.singletonList("a"))).isTrue();
        }

        @Test
        @DisplayName("当集合为空或null时应返回false")
        void shouldReturnFalseWhenCollectionIsEmptyOrNull() {
            assertThat(CollectionChecks.notEmpty(Collections.emptyList())).isFalse();
            assertThat(CollectionChecks.notEmpty(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("sizeBetween 方法测试")
    class SizeBetweenTest {
        @Test
        @DisplayName("当集合大小在范围内时应返回true")
        void shouldReturnTrueWhenSizeIsInRange() {
            List<String> list = Arrays.asList("a", "b", "c");
            assertThat(CollectionChecks.sizeBetween(list, 1, 3)).isTrue();
            assertThat(CollectionChecks.sizeBetween(list, 3, 5)).isTrue();
        }

        @Test
        @DisplayName("当集合大小不在范围内时应返回false")
        void shouldReturnFalseWhenSizeIsOutOfRange() {
            List<String> list = Arrays.asList("a", "b");
            assertThat(CollectionChecks.sizeBetween(list, 3, 5)).isFalse();
        }

        @Test
        @DisplayName("当集合为null时视作大小为0")
        void shouldTreatNullAsSizeZero() {
            assertThat(CollectionChecks.sizeBetween(null, 0, 0)).isTrue();
            assertThat(CollectionChecks.sizeBetween(null, 1, 5)).isFalse();
        }
    }

    @Nested
    @DisplayName("sizeEquals 方法测试")
    class SizeEqualsTest {
        @Test
        @DisplayName("当集合大小等于预期值时应返回true")
        void shouldReturnTrueWhenSizeEqualsExpected() {
            List<String> list = Arrays.asList("a", "b");
            assertThat(CollectionChecks.sizeEquals(list, 2)).isTrue();
        }

        @Test
        @DisplayName("当集合大小不等于预期值时应返回false")
        void shouldReturnFalseWhenSizeDoesNotEqualExpected() {
            List<String> list = Arrays.asList("a", "b");
            assertThat(CollectionChecks.sizeEquals(list, 3)).isFalse();
        }

        @Test
        @DisplayName("当集合为null时应返回false")
        void shouldReturnFalseWhenCollectionIsNull() {
            assertThat(CollectionChecks.sizeEquals(null, 0)).isFalse();
        }
    }

    @Nested
    @DisplayName("contains 方法测试")
    class ContainsTest {
        @Test
        @DisplayName("当集合包含元素时应返回true")
        void shouldReturnTrueWhenCollectionContainsElement() {
            List<String> list = Arrays.asList("a", "b");
            assertThat(CollectionChecks.contains(list, "a")).isTrue();
        }

        @Test
        @DisplayName("当集合不包含元素时应返回false")
        void shouldReturnFalseWhenCollectionDoesNotContainElement() {
            List<String> list = Arrays.asList("a", "b");
            assertThat(CollectionChecks.contains(list, "c")).isFalse();
        }

        @Test
        @DisplayName("当集合为null时应返回false")
        void shouldReturnFalseWhenCollectionIsNull() {
            assertThat(CollectionChecks.contains(null, "a")).isFalse();
        }
    }

    @Nested
    @DisplayName("notContains 方法测试")
    class NotContainsTest {
        @Test
        @DisplayName("当集合不包含元素时应返回true")
        void shouldReturnTrueWhenCollectionDoesNotContainElement() {
            List<String> list = Arrays.asList("a", "b");
            assertThat(CollectionChecks.notContains(list, "c")).isTrue();
        }

        @Test
        @DisplayName("当集合包含元素时应返回false")
        void shouldReturnFalseWhenCollectionContainsElement() {
            List<String> list = Arrays.asList("a", "b");
            assertThat(CollectionChecks.notContains(list, "a")).isFalse();
        }

        @Test
        @DisplayName("当集合为null时应返回true")
        void shouldReturnTrueWhenCollectionIsNull() {
            assertThat(CollectionChecks.notContains(null, "a")).isTrue();
        }
    }
}
