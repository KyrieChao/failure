package com.chao.failfast.result;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.ResponseCode;
import com.chao.failfast.model.enums.TestResponseCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("Result 类测试")
class ResultTest {

    @Nested
    @DisplayName("创建方法测试")
    class CreationTest {
        @Test
        @DisplayName("ok 方法应创建成功结果")
        void okShouldCreateSuccessResult() {
            Result<String> result = Result.ok("test");
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.isFailure()).isFalse();
            assertThat(result.get()).isEqualTo("test");
            assertThat(catchThrowable(result::getError)).isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("fail 方法应创建失败结果")
        void failShouldCreateFailureResult() {
            Result<String> result = Result.fail(TestResponseCode.PARAM_ERROR);
            assertThat(result.isSuccess()).isFalse();
            assertThat(result.isFailure()).isTrue();
            assertThat(result.getError().getResponseCode().getCode()).isEqualTo(TestResponseCode.PARAM_ERROR.getCode());
            assertThat(catchThrowable(result::get)).isInstanceOf(IllegalStateException.class);
        }
        
        @Test
        @DisplayName("fail 方法应创建带详情的失败结果")
        void failWithDetailShouldCreateFailureResult() {
            Result<String> result = Result.fail(TestResponseCode.PARAM_ERROR, "detail");
            assertThat(result.isFailure()).isTrue();
            assertThat(result.getError().getDetail()).isEqualTo("detail");
        }

        @Test
        @DisplayName("ofNullable 当值不为null时应创建成功结果")
        void ofNullableShouldCreateSuccessResultWhenValueIsNotNull() {
            Result<String> result = Result.ofNullable("test", TestResponseCode.PARAM_ERROR);
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.get()).isEqualTo("test");
        }
        
        @Test
        @DisplayName("ofNullable 带详情 当值不为null时应创建成功结果")
        void ofNullableWithDetailShouldCreateSuccessResultWhenValueIsNotNull() {
            Result<String> result = Result.ofNullable("test", TestResponseCode.PARAM_ERROR, "detail");
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.get()).isEqualTo("test");
        }

        @Test
        @DisplayName("ofNullable 当值为null时应创建失败结果")
        void ofNullableShouldCreateFailureResultWhenValueIsNull() {
            Result<String> result = Result.ofNullable(null, TestResponseCode.PARAM_ERROR);
            assertThat(result.isFailure()).isTrue();
            assertThat(result.getError().getResponseCode().getCode()).isEqualTo(TestResponseCode.PARAM_ERROR.getCode());
        }
        
        @Test
        @DisplayName("ofNullable 带详情 当值为null时应创建失败结果")
        void ofNullableWithDetailShouldCreateFailureResultWhenValueIsNull() {
            Result<String> result = Result.ofNullable(null, TestResponseCode.PARAM_ERROR, "detail");
            assertThat(result.isFailure()).isTrue();
            assertThat(result.getError().getDetail()).isEqualTo("detail");
        }
    }

    @Nested
    @DisplayName("函数式操作测试")
    class FunctionalOpsTest {
        @Test
        @DisplayName("map 应转换成功值")
        void mapShouldTransformSuccessValue() {
            Result<String> result = Result.ok("123");
            Result<Integer> mapped = result.map(Integer::parseInt);
            assertThat(mapped.isSuccess()).isTrue();
            assertThat(mapped.get()).isEqualTo(123);
        }

        @Test
        @DisplayName("map 不应转换失败结果")
        void mapShouldNotTransformFailureResult() {
            Result<String> result = Result.fail(TestResponseCode.PARAM_ERROR);
            Result<Integer> mapped = result.map(Integer::parseInt);
            assertThat(mapped.isFailure()).isTrue();
            assertThat(mapped.getError().getResponseCode().getCode()).isEqualTo(TestResponseCode.PARAM_ERROR.getCode());
        }
        
        @Test
        @DisplayName("map 抛出异常时应捕获")
        void mapShouldCatchException() {
            Result<String> result = Result.ok("abc");
            assertThat(catchThrowable(() -> result.map(s -> { throw new RuntimeException("error"); })))
                .isInstanceOf(RuntimeException.class);
                
            // If Business exception is thrown in map?
             Result<String> result2 = Result.ok("abc");
             Result<String> mapped2 = result2.map(s -> { throw Business.of(TestResponseCode.PARAM_ERROR); });
             assertThat(mapped2.isFailure()).isTrue();
        }

        @Test
        @DisplayName("flatMap 应连接 Result")
        void flatMapShouldChainResults() {
            Result<String> result = Result.ok("123");
            Result<Integer> flatMapped = result.flatMap(s -> Result.ok(Integer.parseInt(s)));
            assertThat(flatMapped.isSuccess()).isTrue();
            assertThat(flatMapped.get()).isEqualTo(123);
        }
        
        @Test
        @DisplayName("flatMap 不应转换失败结果")
        void flatMapShouldNotTransformFailureResult() {
            Result<String> result = Result.fail(TestResponseCode.PARAM_ERROR);
            Result<Integer> flatMapped = result.flatMap(s -> Result.ok(Integer.parseInt(s)));
            assertThat(flatMapped.isFailure()).isTrue();
        }

        @Test
        @DisplayName("filter 当条件满足时应保持成功")
        void filterShouldKeepSuccessWhenPredicateIsTrue() {
            Result<Integer> result = Result.ok(10);
            Result<Integer> filtered = result.filter(i -> i > 5, TestResponseCode.PARAM_ERROR);
            assertThat(filtered.isSuccess()).isTrue();
        }
        
        @Test
        @DisplayName("filter 带详情 当条件满足时应保持成功")
        void filterWithDetailShouldKeepSuccessWhenPredicateIsTrue() {
            Result<Integer> result = Result.ok(10);
            Result<Integer> filtered = result.filter(i -> i > 5, TestResponseCode.PARAM_ERROR, "detail");
            assertThat(filtered.isSuccess()).isTrue();
        }

        @Test
        @DisplayName("filter 当条件不满足时应转为失败")
        void filterShouldFailWhenPredicateIsFalse() {
            Result<Integer> result = Result.ok(1);
            Result<Integer> filtered = result.filter(i -> i > 5, TestResponseCode.PARAM_ERROR);
            assertThat(filtered.isFailure()).isTrue();
            assertThat(filtered.getError().getResponseCode().getCode()).isEqualTo(TestResponseCode.PARAM_ERROR.getCode());
        }
        
        @Test
        @DisplayName("filter 带详情 当条件不满足时应转为失败")
        void filterWithDetailShouldFailWhenPredicateIsFalse() {
            Result<Integer> result = Result.ok(1);
            Result<Integer> filtered = result.filter(i -> i > 5, TestResponseCode.PARAM_ERROR, "detail");
            assertThat(filtered.isFailure()).isTrue();
            assertThat(filtered.getError().getDetail()).isEqualTo("detail");
        }
        
        @Test
        @DisplayName("filter 对失败结果无影响")
        void filterShouldNotAffectFailure() {
            Result<Integer> result = Result.fail(TestResponseCode.PARAM_ERROR);
            Result<Integer> filtered = result.filter(i -> i > 5, TestResponseCode.SYSTEM_ERROR);
            assertThat(filtered.isFailure()).isTrue();
            assertThat(filtered.getError().getResponseCode().getCode()).isEqualTo(TestResponseCode.PARAM_ERROR.getCode());
        }
        
        @Test
        @DisplayName("combine 组合两个成功结果")
        void combineShouldCombineSuccess() {
            Result<String> r1 = Result.ok("Hello");
            Result<String> r2 = Result.ok("World");
            Result<String> combined = r1.combine(r2, (s1, s2) -> s1 + " " + s2);
            assertThat(combined.isSuccess()).isTrue();
            assertThat(combined.get()).isEqualTo("Hello World");
        }
        
        @Test
        @DisplayName("combine 包含失败结果时返回失败")
        void combineShouldReturnFailure() {
            Result<String> r1 = Result.ok("Hello");
            Result<String> r2 = Result.fail(TestResponseCode.PARAM_ERROR);
            Result<String> combined = r1.combine(r2, (s1, s2) -> s1 + " " + s2);
            assertThat(combined.isFailure()).isTrue();
            
            Result<String> combined2 = r2.combine(r1, (s1, s2) -> s1 + " " + s2);
            assertThat(combined2.isFailure()).isTrue();
        }
    }

    @Nested
    @DisplayName("恢复与终结操作测试")
    class TerminalOpsTest {
        @Test
        @DisplayName("peek 应只执行副作用")
        void peekShouldNotChangeResult() {
            Result<String> result = Result.ok("test");
            final boolean[] executed = {false};
            Result<String> peeked = result.peek(s -> executed[0] = true);
            
            assertThat(peeked.get()).isEqualTo("test");
            assertThat(executed[0]).isTrue();
            
            // Failure case
            Result<String> fail = Result.fail(TestResponseCode.PARAM_ERROR);
            executed[0] = false;
            fail.peek(s -> executed[0] = true);
            assertThat(executed[0]).isFalse();
        }

        @Test
        @DisplayName("peekError 应只在错误时执行副作用")
        void peekErrorShouldNotChangeResult() {
            Result<String> result = Result.fail(TestResponseCode.PARAM_ERROR);
            final boolean[] executed = {false};
            Result<String> peeked = result.peekError(e -> executed[0] = true);
            
            assertThat(peeked.isFailure()).isTrue();
            assertThat(executed[0]).isTrue();
            
            // Success case
            Result<String> success = Result.ok("test");
            executed[0] = false;
            success.peekError(e -> executed[0] = true);
            assertThat(executed[0]).isFalse();
        }

        @Test
        @DisplayName("recoverWith 当失败时应执行恢复函数")
        void recoverWithShouldRecover() {
            Result<String> result = Result.fail(TestResponseCode.PARAM_ERROR);
            Result<String> recovered = result.recoverWith(e -> Result.ok("recovered"));
            
            assertThat(recovered.isSuccess()).isTrue();
            assertThat(recovered.get()).isEqualTo("recovered");
            
            // Success case
            Result<String> success = Result.ok("test");
            Result<String> recoveredSuccess = success.recoverWith(e -> Result.ok("recovered"));
            assertThat(recoveredSuccess.get()).isEqualTo("test");
        }

        @Test
        @DisplayName("failNow 当成功时应返回值")
        void failNowShouldReturnValueWhenSuccess() {
            Result<String> result = Result.ok("test");
            assertThat(result.failNow()).isEqualTo("test");
            assertThat(result.failNow("default")).isEqualTo("test");
            assertThat(result.failNow(e -> new RuntimeException())).isEqualTo("test");
        }

        @Test
        @DisplayName("failNow 当失败时应抛出异常")
        void failNowShouldThrowExceptionWhenFailure() {
            Result<String> result = Result.fail(TestResponseCode.PARAM_ERROR);
            assertThat(catchThrowable(() -> result.failNow())).isInstanceOf(Business.class);
            assertThat(result.failNow("default")).isEqualTo("default");
            assertThat(catchThrowable(() -> result.failNow(e -> new IllegalArgumentException())))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("onFailGet 当成功时应返回值")
        void onFailGetShouldReturnValueWhenSuccess() {
            Result<String> result = Result.ok("test");
            assertThat(result.onFailGet(() -> "default")).isEqualTo("test");
        }

        @Test
        @DisplayName("onFailGet 当失败时应返回默认值")
        void onFailGetShouldReturnDefaultValueWhenFailure() {
            Result<String> result = Result.fail(TestResponseCode.PARAM_ERROR);
            assertThat(result.onFailGet(() -> "default")).isEqualTo("default");
        }
        
        @Test
        @DisplayName("recover 当失败时应恢复")
        void recoverShouldRecoverFromFailure() {
            Result<String> result = Result.fail(TestResponseCode.PARAM_ERROR);
            Result<String> recovered = result.recover(e -> "recovered");
            assertThat(recovered.isSuccess()).isTrue();
            assertThat(recovered.get()).isEqualTo("recovered");
            
            // Success case
            Result<String> success = Result.ok("test");
            Result<String> recoveredSuccess = success.recover(e -> "recovered");
            assertThat(recoveredSuccess.get()).isEqualTo("test");
        }
    }
}
