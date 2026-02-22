package com.chao.failfast.internal.check;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StringChecks 工具类测试")
class StringChecksTest {

    @Nested
    @DisplayName("blank 方法测试")
    class BlankTest {
        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
        @DisplayName("当字符串为空或空白时应返回true")
        void shouldReturnTrueWhenStringIsBlank(String input) {
            assertThat(StringChecks.blank(input)).isTrue();
        }

        @Test
        @DisplayName("当字符串非空时应返回false")
        void shouldReturnFalseWhenStringIsNotBlank() {
            assertThat(StringChecks.blank("abc")).isFalse();
        }
    }

    @Nested
    @DisplayName("notBlank 方法测试")
    class NotBlankTest {
        @Test
        @DisplayName("当字符串非空时应返回true")
        void shouldReturnTrueWhenStringIsNotBlank() {
            assertThat(StringChecks.notBlank("abc")).isTrue();
            assertThat(StringChecks.notBlank(" a ")).isTrue();
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", " ", "  "})
        @DisplayName("当字符串为空或空白时应返回false")
        void shouldReturnFalseWhenStringIsBlank(String input) {
            assertThat(StringChecks.notBlank(input)).isFalse();
        }
    }

    @Nested
    @DisplayName("lengthBetween 方法测试")
    class LengthBetweenTest {
        @Test
        @DisplayName("当字符串长度在范围内时应返回true")
        void shouldReturnTrueWhenLengthIsInRange() {
            assertThat(StringChecks.lengthBetween("abc", 1, 3)).isTrue();
            assertThat(StringChecks.lengthBetween("abc", 3, 5)).isTrue();
        }

        @Test
        @DisplayName("当字符串长度不在范围内时应返回false")
        void shouldReturnFalseWhenLengthIsOutOfRange() {
            assertThat(StringChecks.lengthBetween("abc", 4, 5)).isFalse();
            assertThat(StringChecks.lengthBetween("abc", 1, 2)).isFalse();
        }

        @Test
        @DisplayName("当字符串为null时应返回false")
        void shouldReturnFalseWhenStringIsNull() {
            assertThat(StringChecks.lengthBetween(null, 1, 3)).isFalse();
        }
    }

    @Nested
    @DisplayName("match 方法测试")
    class MatchTest {
        @Test
        @DisplayName("当字符串匹配正则时应返回true")
        void shouldReturnTrueWhenStringMatchesRegex() {
            assertThat(StringChecks.match("123", "\\d+")).isTrue();
        }

        @Test
        @DisplayName("当字符串不匹配正则时应返回false")
        void shouldReturnFalseWhenStringDoesNotMatchRegex() {
            assertThat(StringChecks.match("abc", "\\d+")).isFalse();
        }

        @Test
        @DisplayName("当字符串为null时应返回false")
        void shouldReturnFalseWhenStringIsNull() {
            assertThat(StringChecks.match(null, "\\d+")).isFalse();
        }
    }

    @Nested
    @DisplayName("email 方法测试")
    class EmailTest {
        @ParameterizedTest
        @ValueSource(strings = {"test@example.com", "user.name@domain.co.jp"})
        @DisplayName("当邮箱格式正确时应返回true")
        void shouldReturnTrueWhenEmailIsValid(String email) {
            assertThat(StringChecks.email(email)).isTrue();
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"invalid-email", "test@", "@example.com"})
        @DisplayName("当邮箱格式错误时应返回false")
        void shouldReturnFalseWhenEmailIsInvalid(String email) {
            assertThat(StringChecks.email(email)).isFalse();
        }
    }

    @Nested
    @DisplayName("equalsIgnoreCase 方法测试")
    class EqualsIgnoreCaseTest {
        @Test
        @DisplayName("当字符串忽略大小写相等时应返回true")
        void shouldReturnTrueWhenStringsAreEqualIgnoreCase() {
            assertThat(StringChecks.equalsIgnoreCase("abc", "ABC")).isTrue();
        }

        @Test
        @DisplayName("当字符串不相等时应返回false")
        void shouldReturnFalseWhenStringsAreNotEqual() {
            assertThat(StringChecks.equalsIgnoreCase("abc", "def")).isFalse();
        }

        @Test
        @DisplayName("当任一字符串为null时应返回false")
        void shouldReturnFalseWhenEitherStringIsNull() {
            assertThat(StringChecks.equalsIgnoreCase(null, "abc")).isFalse();
            assertThat(StringChecks.equalsIgnoreCase("abc", null)).isFalse();
        }
    }

    @Nested
    @DisplayName("startsWith 方法测试")
    class StartsWithTest {
        @Test
        @DisplayName("当字符串以前缀开头时应返回true")
        void shouldReturnTrueWhenStringStartsWithPrefix() {
            assertThat(StringChecks.startsWith("abcdef", "abc")).isTrue();
        }

        @Test
        @DisplayName("当字符串不以前缀开头时应返回false")
        void shouldReturnFalseWhenStringDoesNotStartWithPrefix() {
            assertThat(StringChecks.startsWith("abcdef", "def")).isFalse();
        }
    }

    @Nested
    @DisplayName("endsWith 方法测试")
    class EndsWithTest {
        @Test
        @DisplayName("当字符串以后缀结尾时应返回true")
        void shouldReturnTrueWhenStringEndsWithSuffix() {
            assertThat(StringChecks.endsWith("abcdef", "def")).isTrue();
        }

        @Test
        @DisplayName("当字符串不以后缀结尾时应返回false")
        void shouldReturnFalseWhenStringDoesNotEndWithSuffix() {
            assertThat(StringChecks.endsWith("abcdef", "abc")).isFalse();
        }
    }
}
