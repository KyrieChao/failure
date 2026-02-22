package com.chao.failfast.internal.check;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EnumChecks 工具类测试")
class EnumChecksTest {

    enum TestEnum {
        VALUE1, VALUE2
    }

    @Nested
    @DisplayName("isValidEnum 方法测试")
    class IsValidEnumTest {
        @Test
        @DisplayName("当字符串是有效的枚举值时应返回true")
        void shouldReturnTrueWhenValueIsEnumValue() {
            assertThat(EnumChecks.isValidEnum(TestEnum.class, "VALUE1")).isTrue();
            assertThat(EnumChecks.isValidEnum(TestEnum.class, "VALUE2")).isTrue();
        }

        @Test
        @DisplayName("当字符串不是有效的枚举值时应返回false")
        void shouldReturnFalseWhenValueIsNotEnumValue() {
            assertThat(EnumChecks.isValidEnum(TestEnum.class, "INVALID")).isFalse();
        }

        @Test
        @DisplayName("当字符串为null时应返回false")
        void shouldReturnFalseWhenValueIsNull() {
            assertThat(EnumChecks.isValidEnum(TestEnum.class, null)).isFalse();
        }

        @Test
        @DisplayName("当枚举类为null时应返回false")
        void shouldReturnFalseWhenEnumTypeIsNull() {
            // valueOf throws NPE if enumType is null
            // But check implementation: Enum.valueOf(enumType, value)
            // If enumType is null, Enum.valueOf throws NPE.
            // EnumChecks catches NPE.
            assertThat(EnumChecks.isValidEnum(null, "VALUE1")).isFalse();
        }
    }
}
