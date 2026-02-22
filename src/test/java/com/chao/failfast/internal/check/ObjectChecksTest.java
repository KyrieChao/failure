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
}
