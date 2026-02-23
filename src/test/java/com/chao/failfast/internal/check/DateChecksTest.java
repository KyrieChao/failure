package com.chao.failfast.internal.check;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DateChecks 工具类测试")
class DateChecksTest {

    @Nested
    @DisplayName("Date after/before 方法测试")
    class DateTest {
        @Test
        @DisplayName("after 方法测试")
        void testAfter() {
            Date now = new Date();
            Date future = new Date(now.getTime() + 1000);
            assertThat(DateChecks.after(future, now)).isTrue();
            assertThat(DateChecks.after(now, future)).isFalse();
            assertThat(DateChecks.after(now, now)).isFalse();
            assertThat(DateChecks.after(null, now)).isFalse();
            assertThat(DateChecks.after(now, null)).isFalse();
        }

        @Test
        @DisplayName("before 方法测试")
        void testBefore() {
            Date now = new Date();
            Date past = new Date(now.getTime() - 1000);
            assertThat(DateChecks.before(past, now)).isTrue();
            assertThat(DateChecks.before(now, past)).isFalse();
            assertThat(DateChecks.before(now, now)).isFalse();
            assertThat(DateChecks.before(null, now)).isFalse();
            assertThat(DateChecks.before(now, null)).isFalse();
        }
        
        @Test
        @DisplayName("isPast/isFuture 方法测试")
        void testIsPastIsFuture() {
            Date now = new Date();
            Date past = new Date(now.getTime() - 10000);
            Date future = new Date(now.getTime() + 10000);
            
            assertThat(DateChecks.isPast(past)).isTrue();
            assertThat(DateChecks.isPast(future)).isFalse();
            assertThat(DateChecks.isPast((Date)null)).isFalse();
            
            assertThat(DateChecks.isFuture(future)).isTrue();
            assertThat(DateChecks.isFuture(past)).isFalse();
            assertThat(DateChecks.isFuture((Date)null)).isFalse();
        }
    }

    @Nested
    @DisplayName("Comparable Generic 方法测试")
    class ComparableTest {
        @Test
        void testAfter() {
            assertThat(DateChecks.after(2, 1)).isTrue();
            assertThat(DateChecks.after(1, 2)).isFalse();
            assertThat(DateChecks.after(null, 1)).isFalse();
        }

        @Test
        void testAfterOrEqual() {
            assertThat(DateChecks.afterOrEqual(2, 1)).isTrue();
            assertThat(DateChecks.afterOrEqual(1, 1)).isTrue();
            assertThat(DateChecks.afterOrEqual(1, 2)).isFalse();
            assertThat(DateChecks.afterOrEqual(null, 1)).isFalse();
        }

        @Test
        void testBefore() {
            assertThat(DateChecks.before(1, 2)).isTrue();
            assertThat(DateChecks.before(2, 1)).isFalse();
            assertThat(DateChecks.before(null, 2)).isFalse();
        }

        @Test
        void testBeforeOrEqual() {
            assertThat(DateChecks.beforeOrEqual(1, 2)).isTrue();
            assertThat(DateChecks.beforeOrEqual(1, 1)).isTrue();
            assertThat(DateChecks.beforeOrEqual(2, 1)).isFalse();
            assertThat(DateChecks.beforeOrEqual(null, 1)).isFalse();
        }

        @Test
        void testBetween() {
            assertThat(DateChecks.between(2, 1, 3)).isTrue();
            assertThat(DateChecks.between(1, 1, 3)).isTrue();
            assertThat(DateChecks.between(3, 1, 3)).isTrue();
            assertThat(DateChecks.between(0, 1, 3)).isFalse();
            assertThat(DateChecks.between(4, 1, 3)).isFalse();
            assertThat(DateChecks.between(null, 1, 3)).isFalse();
        }
    }

    @Nested
    @DisplayName("Java 8 Time API 测试")
    class Java8TimeTest {
        
        @Test
        @DisplayName("LocalDate isPast/isFuture/isToday")
        void testLocalDate() {
            LocalDate now = LocalDate.now();
            LocalDate past = now.minusDays(1);
            LocalDate future = now.plusDays(1);
            
            assertThat(DateChecks.isPast(past)).isTrue();
            assertThat(DateChecks.isPast(future)).isFalse();
            assertThat(DateChecks.isPast((ChronoLocalDate)null)).isFalse();
            
            assertThat(DateChecks.isFuture(future)).isTrue();
            assertThat(DateChecks.isFuture(past)).isFalse();
            assertThat(DateChecks.isFuture((ChronoLocalDate)null)).isFalse();
            
            assertThat(DateChecks.isToday(now)).isTrue();
            assertThat(DateChecks.isToday(past)).isFalse();
            assertThat(DateChecks.isToday(null)).isFalse();
        }
        
        @Test
        @DisplayName("LocalDateTime isPast/isFuture")
        void testLocalDateTime() {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime past = now.minusDays(1);
            LocalDateTime future = now.plusDays(1);
            
            assertThat(DateChecks.isPast(past)).isTrue();
            assertThat(DateChecks.isPast(future)).isFalse();
            assertThat(DateChecks.isPast((ChronoLocalDateTime<?>)null)).isFalse();
            
            assertThat(DateChecks.isFuture(future)).isTrue();
            assertThat(DateChecks.isFuture(past)).isFalse();
            assertThat(DateChecks.isFuture((ChronoLocalDateTime<?>)null)).isFalse();
        }
        
        @Test
        @DisplayName("Instant isPast/isFuture")
        void testInstant() {
            Instant now = Instant.now();
            Instant past = now.minusSeconds(100);
            Instant future = now.plusSeconds(100);
            
            assertThat(DateChecks.isPast(past)).isTrue();
            assertThat(DateChecks.isPast(future)).isFalse();
            assertThat(DateChecks.isPast((Instant)null)).isFalse();
            
            assertThat(DateChecks.isFuture(future)).isTrue();
            assertThat(DateChecks.isFuture(past)).isFalse();
            assertThat(DateChecks.isFuture((Instant)null)).isFalse();
        }
        
        @Test
        @DisplayName("ZonedDateTime isPast/isFuture")
        void testZonedDateTime() {
            ZonedDateTime now = ZonedDateTime.now();
            ZonedDateTime past = now.minusDays(1);
            ZonedDateTime future = now.plusDays(1);
            
            assertThat(DateChecks.isPast(past)).isTrue();
            assertThat(DateChecks.isPast(future)).isFalse();
            assertThat(DateChecks.isPast((ChronoZonedDateTime<?>)null)).isFalse();
            
            assertThat(DateChecks.isFuture(future)).isTrue();
            assertThat(DateChecks.isFuture(past)).isFalse();
            assertThat(DateChecks.isFuture((ChronoZonedDateTime<?>)null)).isFalse();
        }
    }
}
