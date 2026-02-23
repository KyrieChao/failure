package com.chao.failfast.internal.check;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ObjectChecks 工具类测试")
class ObjectChecksTest {

    @Nested
    @DisplayName("exists 方法测试")
    class ExistsTest {
        @Test
        @DisplayName("当对象不为null时应返回true")
        void shouldReturnTrueWhenObjectIsNotNull() {
            assertThat(ObjectChecks.exists(new Object())).isTrue();
            assertThat(ObjectChecks.exists("test")).isTrue();
            assertThat(ObjectChecks.exists(123)).isTrue();
        }

        @Test
        @DisplayName("当对象为null时应返回false")
        void shouldReturnFalseWhenObjectIsNull() {
            assertThat(ObjectChecks.exists(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("isNull 方法测试")
    class IsNullTest {
        @Test
        @DisplayName("当对象为null时应返回true")
        void shouldReturnTrueWhenObjectIsNull() {
            assertThat(ObjectChecks.isNull(null)).isTrue();
        }

        @Test
        @DisplayName("当对象不为null时应返回false")
        void shouldReturnFalseWhenObjectIsNotNull() {
            assertThat(ObjectChecks.isNull(new Object())).isFalse();
            assertThat(ObjectChecks.isNull("test")).isFalse();
        }
    }
    @Nested
    @DisplayName("instanceOf 方法测试")
    class InstanceOfTest {
        @Test
        @DisplayName("当对象是指定类型实例时应返回true")
        void shouldReturnTrueWhenObjectIsInstanceOfType() {
            assertThat(ObjectChecks.instanceOf("test", String.class)).isTrue();
            assertThat(ObjectChecks.instanceOf(123, Integer.class)).isTrue();
            assertThat(ObjectChecks.instanceOf(123, Number.class)).isTrue();  // 子类实例
        }

        @Test
        @DisplayName("当对象不是指定类型实例时应返回false")
        void shouldReturnFalseWhenObjectIsNotInstanceOfType() {
            assertThat(ObjectChecks.instanceOf("test", Integer.class)).isFalse();
            assertThat(ObjectChecks.instanceOf(null, String.class)).isFalse();  // obj为null
        }

        @Test
        @DisplayName("当type为null时应返回false")
        void shouldReturnFalseWhenTypeIsNull() {
            assertThat(ObjectChecks.instanceOf("test", null)).isFalse();
        }
    }

    @Nested
    @DisplayName("notInstanceOf 方法测试")
    class NotInstanceOfTest {
        @Test
        @DisplayName("当对象不是指定类型实例时应返回true")
        void shouldReturnTrueWhenObjectIsNotInstanceOfType() {
            assertThat(ObjectChecks.notInstanceOf("test", Integer.class)).isTrue();
            assertThat(ObjectChecks.notInstanceOf(null, String.class)).isTrue();  // obj为null
        }

        @Test
        @DisplayName("当对象是指定类型实例时应返回false")
        void shouldReturnFalseWhenObjectIsInstanceOfType() {
            assertThat(ObjectChecks.notInstanceOf("test", String.class)).isFalse();
            assertThat(ObjectChecks.notInstanceOf(123, Number.class)).isFalse();
        }

        @Test
        @DisplayName("当type为null时应返回false")
        void shouldReturnFalseWhenTypeIsNull() {
            assertThat(ObjectChecks.notInstanceOf("test", null)).isFalse();
        }
    }

    @Nested
    @DisplayName("allNotNull 方法测试")
    class AllNotNullTest {
        @Test
        @DisplayName("当所有对象都不为null时应返回true")
        void shouldReturnTrueWhenAllObjectsAreNotNull() {
            assertThat(ObjectChecks.allNotNull("a", "b", "c")).isTrue();
            assertThat(ObjectChecks.allNotNull(1, 2, 3)).isTrue();
            assertThat(ObjectChecks.allNotNull("test")).isTrue();  // 单个参数
            assertThat(ObjectChecks.allNotNull()).isTrue();  // 空参数
        }

        @Test
        @DisplayName("当任一对象为null时应返回false")
        void shouldReturnFalseWhenAnyObjectIsNull() {
            assertThat(ObjectChecks.allNotNull("a", null, "c")).isFalse();
            assertThat(ObjectChecks.allNotNull(null, "b", "c")).isFalse();
            assertThat(ObjectChecks.allNotNull("a", "b", null)).isFalse();
        }

        @Test
        @DisplayName("当参数数组本身为null时应返回false")
        void shouldReturnFalseWhenArrayIsNull() {
            assertThat(ObjectChecks.allNotNull((Object[]) null)).isFalse();
        }

        @Test
        @DisplayName("当只有单个null参数时应返回false")
        void shouldReturnFalseWhenSingleNullArgument() {
            assertThat(ObjectChecks.allNotNull((Object) null)).isFalse();
        }
    }
}
