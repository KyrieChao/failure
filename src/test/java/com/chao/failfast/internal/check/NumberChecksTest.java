package com.chao.failfast.internal.check;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("NumberChecks 工具类测试")
class NumberChecksTest {

    @Nested
    @DisplayName("positive 方法测试")
    class PositiveTest {
        @Test
        @DisplayName("当数值为正数时应返回true")
        void shouldReturnTrueWhenNumberIsPositive() {
            assertThat(NumberChecks.positive(1)).isTrue();
            assertThat(NumberChecks.positive(0.1)).isTrue();
        }

        @Test
        @DisplayName("当数值为0或负数时应返回false")
        void shouldReturnFalseWhenNumberIsNotPositive() {
            assertThat(NumberChecks.positive(0)).isFalse();
            assertThat(NumberChecks.positive(-1)).isFalse();
        }

        @Test
        @DisplayName("当数值为null时应返回false")
        void shouldReturnFalseWhenNumberIsNull() {
            assertThat(NumberChecks.positive(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("inRange 方法测试")
    class InRangeTest {
        @Test
        @DisplayName("当数值在范围内时应返回true")
        void shouldReturnTrueWhenNumberIsInRange() {
            assertThat(NumberChecks.inRange(5, 1, 10)).isTrue();
        }

        @Test
        @DisplayName("当数值不在范围内时应返回false")
        void shouldReturnFalseWhenNumberIsOutOfRange() {
            assertThat(NumberChecks.inRange(0, 1, 10)).isFalse();
            assertThat(NumberChecks.inRange(11, 1, 10)).isFalse();
        }

        @Test
        @DisplayName("当任一参数为null时应返回false")
        void shouldReturnFalseWhenAnyParameterIsNull() {
            assertThat(NumberChecks.inRange(null, 1, 10)).isFalse();
            assertThat(NumberChecks.inRange(5, null, 10)).isFalse();
            assertThat(NumberChecks.inRange(5, 1, null)).isFalse();
        }
    }

    @Nested
    @DisplayName("inRangeNumber 方法测试")
    class InRangeNumberTest {
        @Test
        @DisplayName("当数值在范围内时应返回true")
        void shouldReturnTrueWhenNumberIsInRange() {
            assertThat(NumberChecks.inRangeNumber(5, 1, 10)).isTrue();
            assertThat(NumberChecks.inRangeNumber(5.5, 1.1, 10.9)).isTrue();
        }

        @Test
        @DisplayName("当数值不在范围内时应返回false")
        void shouldReturnFalseWhenNumberIsOutOfRange() {
            assertThat(NumberChecks.inRangeNumber(0, 1, 10)).isFalse();
            assertThat(NumberChecks.inRangeNumber(11, 1, 10)).isFalse();
        }

        @Test
        @DisplayName("当任一参数为null时应返回false")
        void shouldReturnFalseWhenAnyParameterIsNull() {
            assertThat(NumberChecks.inRangeNumber(null, 1, 10)).isFalse();
            assertThat(NumberChecks.inRangeNumber(5, null, 10)).isFalse();
            assertThat(NumberChecks.inRangeNumber(5, 1, null)).isFalse();
        }
    }

    @Nested
    @DisplayName("nonNegative 方法测试")
    class NonNegativeTest {
        @Test
        @DisplayName("当数值为非负数时应返回true")
        void shouldReturnTrueWhenNumberIsNonNegative() {
            assertThat(NumberChecks.nonNegative(0)).isTrue();
            assertThat(NumberChecks.nonNegative(1)).isTrue();
        }

        @Test
        @DisplayName("当数值为负数时应返回false")
        void shouldReturnFalseWhenNumberIsNegative() {
            assertThat(NumberChecks.nonNegative(-1)).isFalse();
        }

        @Test
        @DisplayName("当数值为null时应返回false")
        void shouldReturnFalseWhenNumberIsNull() {
            assertThat(NumberChecks.nonNegative(null)).isFalse();
        }
    }
}
