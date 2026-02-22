package com.chao.failfast.internal.check;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DateChecks 工具类测试")
class DateChecksTest {

    @Nested
    @DisplayName("after 方法测试")
    class AfterTest {
        @Test
        @DisplayName("当第一个日期在第二个日期之后时应返回true")
        void shouldReturnTrueWhenFirstDateIsAfterSecondDate() {
            Date now = new Date();
            Date future = new Date(now.getTime() + 1000);
            assertThat(DateChecks.after(future, now)).isTrue();
        }

        @Test
        @DisplayName("当第一个日期在第二个日期之前时应返回false")
        void shouldReturnFalseWhenFirstDateIsBeforeSecondDate() {
            Date now = new Date();
            Date past = new Date(now.getTime() - 1000);
            assertThat(DateChecks.after(past, now)).isFalse();
        }

        @Test
        @DisplayName("当日期相等时应返回false")
        void shouldReturnFalseWhenDatesAreEqual() {
            Date now = new Date();
            assertThat(DateChecks.after(now, now)).isFalse();
        }

        @Test
        @DisplayName("当任一日期为null时应返回false")
        void shouldReturnFalseWhenAnyDateIsNull() {
            Date now = new Date();
            assertThat(DateChecks.after(null, now)).isFalse();
            assertThat(DateChecks.after(now, null)).isFalse();
        }
    }

    @Nested
    @DisplayName("before 方法测试")
    class BeforeTest {
        @Test
        @DisplayName("当第一个日期在第二个日期之前时应返回true")
        void shouldReturnTrueWhenFirstDateIsBeforeSecondDate() {
            Date now = new Date();
            Date past = new Date(now.getTime() - 1000);
            assertThat(DateChecks.before(past, now)).isTrue();
        }

        @Test
        @DisplayName("当第一个日期在第二个日期之后时应返回false")
        void shouldReturnFalseWhenFirstDateIsAfterSecondDate() {
            Date now = new Date();
            Date future = new Date(now.getTime() + 1000);
            assertThat(DateChecks.before(future, now)).isFalse();
        }

        @Test
        @DisplayName("当日期相等时应返回false")
        void shouldReturnFalseWhenDatesAreEqual() {
            Date now = new Date();
            assertThat(DateChecks.before(now, now)).isFalse();
        }

        @Test
        @DisplayName("当任一日期为null时应返回false")
        void shouldReturnFalseWhenAnyDateIsNull() {
            Date now = new Date();
            assertThat(DateChecks.before(null, now)).isFalse();
            assertThat(DateChecks.before(now, null)).isFalse();
        }
    }
}
