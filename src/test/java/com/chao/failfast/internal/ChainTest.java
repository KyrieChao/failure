package com.chao.failfast.internal;

import com.chao.failfast.Failure;
import com.chao.failfast.model.enums.TestResponseCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 单元测试 - 覆盖 Chain 类的各种变体
 */
@DisplayName("Chain 核心链全面测试")
public class ChainTest {

    // Helper to run a test and assert Business exception with specific code
    private void assertBusiness(Runnable runnable, ResponseCode expectedCode) {
        try {
            runnable.run();
            fail("Expected Business exception");
        } catch (Business e) {
            assertEquals(expectedCode.getCode(), e.getResponseCode().getCode());
        }
    }

    // Helper to run a test and assert success
    private void assertSuccess(Runnable runnable) {
        assertDoesNotThrow(runnable::run);
    }

    // ==================== 基础功能测试 (failNow, onFail, etc.) ====================
    @Test
    public void testBaseFeatures() {
        // failNow variants - must be triggered by failed state
        assertBusiness(() -> Failure.begin().isTrue(false).failNow(TestResponseCode.PARAM_ERROR), TestResponseCode.PARAM_ERROR);
        assertBusiness(() -> Failure.begin().isTrue(false).failNow(TestResponseCode.PARAM_ERROR, "msg"), TestResponseCode.PARAM_ERROR);
        assertBusiness(() -> Failure.begin().isTrue(false).failNow(TestResponseCode.PARAM_ERROR, "msg %s", "arg"), TestResponseCode.PARAM_ERROR);

        // failNow(Consumer)
        assertBusiness(() -> Failure.begin().isTrue(false).failNow(f -> f.responseCode(TestResponseCode.PARAM_ERROR)), TestResponseCode.PARAM_ERROR);

        // failNow(Supplier)
        assertBusiness(() -> Failure.begin().isTrue(false).failNow(() -> Business.of(TestResponseCode.PARAM_ERROR)), TestResponseCode.PARAM_ERROR);

        // onFail
        final boolean[] executed = {false};
        Failure.begin().isTrue(false, TestResponseCode.PARAM_ERROR).onFail(() -> executed[0] = true);
        assertTrue(executed[0]);

        executed[0] = false;
        Failure.begin().isTrue(true, TestResponseCode.PARAM_ERROR).onFail(() -> executed[0] = true);
        assertFalse(executed[0]);

        // onFailGet
        assertEquals("default", Failure.begin().isTrue(false, TestResponseCode.PARAM_ERROR).onFailGet(() -> "default").orElse("other"));
        assertFalse(Failure.begin().isTrue(true, TestResponseCode.PARAM_ERROR).onFailGet(() -> "default").isPresent());
    }

    // ==================== Object Variants ====================
    @Test
    public void testObjectVariants() {
        Object obj = new Object();
        // exists / notNull
        assertSuccess(() -> Failure.begin().exists(obj).failNow(TestResponseCode.PARAM_ERROR));
        assertSuccess(() -> Failure.begin().exists(obj, TestResponseCode.PARAM_ERROR));
        assertSuccess(() -> Failure.begin().exists(obj, TestResponseCode.PARAM_ERROR, "detail"));
        assertSuccess(() -> Failure.begin().exists(obj, f -> f.responseCode(TestResponseCode.PARAM_ERROR)));

        assertBusiness(() -> Failure.begin().exists(null).fail(), ResponseCode.of(500, "Validation failed")); // Default fail
        assertBusiness(() -> Failure.begin().exists(null, TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // isNull
        assertSuccess(() -> Failure.begin().isNull(null).failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().isNull(obj, TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);
    }

    // ==================== Boolean Variants ====================
    @Test
    public void testBooleanVariants() {
        // state / isTrue
        assertSuccess(() -> Failure.begin().state(true).failNow(TestResponseCode.PARAM_ERROR));
        assertSuccess(() -> Failure.begin().state(true, TestResponseCode.PARAM_ERROR).failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().state(false, TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // isFalse
        assertSuccess(() -> Failure.begin().isFalse(false).failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().isFalse(true, TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);
    }

    // ==================== String Variants ====================
    @Test
    public void testStringVariants() {
        // blank
        assertSuccess(() -> Failure.begin().blank("").failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().blank("a", TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // notBlank / notEmpty
        assertSuccess(() -> Failure.begin().notBlank("a").failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().notBlank("", TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // lengthBetween
        assertSuccess(() -> Failure.begin().lengthBetween("abc", 1, 3).failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().lengthBetween("abc", 4, 5, TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // match
        assertSuccess(() -> Failure.begin().match("123", "\\d+").failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().match("abc", "\\d+", TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // email
        assertSuccess(() -> Failure.begin().email("a@b.c").failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().email("invalid", TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // equalsIgnoreCase
        assertSuccess(() -> Failure.begin().equalsIgnoreCase("a", "A").failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().equalsIgnoreCase("a", "b", TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // startsWith
        assertSuccess(() -> Failure.begin().startsWith("abc", "a").failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().startsWith("abc", "b", TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // endsWith
        assertSuccess(() -> Failure.begin().endsWith("abc", "c").failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().endsWith("abc", "b", TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);
    }

    // ==================== Collection Variants ====================
    @Test
    public void testCollectionVariants() {
        List<String> list = List.of("A");
        // notEmpty
        assertSuccess(() -> Failure.begin().notEmpty(list).failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().notEmpty(Collections.emptyList(), TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // sizeBetween
        assertSuccess(() -> Failure.begin().sizeBetween(list, 1, 1).failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().sizeBetween(list, 2, 3, TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // sizeEquals
        assertSuccess(() -> Failure.begin().sizeEquals(list, 1).failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().sizeEquals(list, 2, TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // contains
        assertSuccess(() -> Failure.begin().contains(list, "A").failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().contains(list, "B", TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // notContains
        assertSuccess(() -> Failure.begin().notContains(list, "B").failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().notContains(list, "A", TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);
    }

    // ==================== Array Variants ====================
    @Test
    public void testArrayVariants() {
        String[] arr = {"A"};
        // notEmpty
        assertSuccess(() -> Failure.begin().notEmpty(arr).failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().notEmpty(new String[]{}, TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // sizeBetween
        assertSuccess(() -> Failure.begin().sizeBetween(arr, 1, 1).failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().sizeBetween(arr, 2, 3, TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // sizeEquals
        assertSuccess(() -> Failure.begin().sizeEquals(arr, 1).failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().sizeEquals(arr, 2, TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // contains
        assertSuccess(() -> Failure.begin().contains(arr, "A").failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().contains(arr, "B", TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // notContains
        assertSuccess(() -> Failure.begin().notContains(arr, "B").failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().notContains(arr, "A", TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);
    }

    // ==================== Number Variants ====================
    @Test
    public void testNumberVariants() {
        // positive
        assertSuccess(() -> Failure.begin().positive(1).failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().positive(-1, TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // positiveNumber (alias)
        assertSuccess(() -> Failure.begin().positiveNumber(1).failNow(TestResponseCode.PARAM_ERROR));

        // inRange
        assertSuccess(() -> Failure.begin().inRange(5, 1, 10).failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().inRange(0, 1, 10, TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // inRangeNumber
        assertSuccess(() -> Failure.begin().inRangeNumber(5, 1, 10).failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().inRangeNumber(0, 1, 10, TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // nonNegative
        assertSuccess(() -> Failure.begin().nonNegative(0).failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().nonNegative(-1, TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);
    }

    // ==================== Date Variants ====================
    @Test
    public void testDateVariants() {
        Date now = new Date();
        Date future = new Date(now.getTime() + 1000);
        // after
        assertSuccess(() -> Failure.begin().after(future, now).failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().after(now, future, TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // before
        assertSuccess(() -> Failure.begin().before(now, future).failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().before(future, now, TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);
    }

    // ==================== Enum Variants ====================
    enum E {A}

    @Test
    public void testEnumVariants() {
        assertSuccess(() -> Failure.begin().enumValue(E.class, "A").failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().enumValue(E.class, "B", TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);
    }

    // ==================== Identity Variants ====================
    @Test
    public void testIdentityVariants() {
        Object o1 = new Object();
        Object o2 = new Object();
        // same
        assertSuccess(() -> Failure.begin().same(o1, o1).failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().same(o1, o2, TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // notSame
        assertSuccess(() -> Failure.begin().notSame(o1, o2).failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().notSame(o1, o1, TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // equals
        assertSuccess(() -> Failure.begin().equals("a", "a").failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().equals("a", "b", TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);

        // notEquals
        assertSuccess(() -> Failure.begin().notEquals("a", "b").failNow(TestResponseCode.PARAM_ERROR));
        assertBusiness(() -> Failure.begin().notEquals("a", "a", TestResponseCode.PARAM_ERROR).fail(), TestResponseCode.PARAM_ERROR);
    }
}
