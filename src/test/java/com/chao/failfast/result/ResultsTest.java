package com.chao.failfast.result;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.MultiBusiness;
import com.chao.failfast.model.enums.TestResponseCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Results 工具类测试")
class ResultsTest {

    @Nested
    @DisplayName("tryOf 方法测试")
    class TryOfTest {
        @Test
        @DisplayName("当Supplier无异常时应返回成功Result")
        void shouldReturnSuccessWhenSupplierSucceeds() {
            Result<String> result = Results.tryOf(() -> "test", TestResponseCode.SYSTEM_ERROR);
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.get()).isEqualTo("test");
        }

        @Test
        @DisplayName("当Supplier抛出Business异常时应返回对应的失败Result")
        void shouldReturnFailureWhenSupplierThrowsBusiness() {
            Result<String> result = Results.tryOf(() -> {
                throw Business.of(TestResponseCode.PARAM_ERROR);
            }, TestResponseCode.SYSTEM_ERROR);

            assertThat(result.isFailure()).isTrue();
            assertThat(result.getError().getResponseCode().getCode()).isEqualTo(TestResponseCode.PARAM_ERROR.getCode());
        }

        @Test
        @DisplayName("当Supplier抛出其他异常时应返回指定错误码的失败Result")
        void shouldReturnFailureWhenSupplierThrowsOtherException() {
            Result<String> result = Results.tryOf(() -> {
                throw new RuntimeException("runtime error");
            }, TestResponseCode.SYSTEM_ERROR);

            assertThat(result.isFailure()).isTrue();
            assertThat(result.getError().getResponseCode().getCode()).isEqualTo(TestResponseCode.SYSTEM_ERROR.getCode());
            assertThat(result.getError().getDetail()).isEqualTo("runtime error");
        }
    }

    @Nested
    @DisplayName("sequence 方法测试")
    class SequenceTest {
        @Test
        @DisplayName("当所有Result都成功时应返回成功列表")
        void shouldReturnSuccessListWhenAllResultsAreSuccess() {
            Result<String> r1 = Result.ok("a");
            Result<String> r2 = Result.ok("b");
            Result<List<String>> result = Results.sequence(r1, r2);

            assertThat(result.isSuccess()).isTrue();
            assertThat(result.get()).containsExactly("a", "b");
        }

        @Test
        @DisplayName("当存在失败Result时应返回第一个失败")
        void shouldReturnFirstFailureWhenAnyResultIsFailure() {
            Result<String> r1 = Result.ok("a");
            Result<String> r2 = Result.fail(TestResponseCode.PARAM_ERROR);
            Result<List<String>> result = Results.sequence(r1, r2);

            assertThat(result.isFailure()).isTrue();
            assertThat(result.getError().getResponseCode().getCode()).isEqualTo(TestResponseCode.PARAM_ERROR.getCode());
        }
    }

    @Nested
    @DisplayName("sequenceAll 方法测试")
    class SequenceAllTest {
        @Test
        @DisplayName("应当收集所有成功结果")
        void shouldCollectAllSuccess() {
            Result<String> r1 = Result.ok("a");
            Result<String> r2 = Result.ok("b");
            Result<List<String>> result = Results.sequenceAll(r1, r2);

            assertThat(result.isSuccess()).isTrue();
            assertThat(result.get()).containsExactly("a", "b");
        }

        @Test
        @DisplayName("应当收集所有错误")
        void shouldCollectAllErrors() {
            Result<String> r1 = Result.ok("a");
            Result<String> r2 = Result.fail(TestResponseCode.PARAM_ERROR);
            Result<String> r3 = Result.fail(TestResponseCode.SYSTEM_ERROR);

            Result<List<String>> result = Results.sequenceAll(r1, r2, r3);

            assertThat(result.isFailure()).isTrue();
            assertThat(result.getError()).isInstanceOf(MultiBusiness.class);
            MultiBusiness mb = (MultiBusiness) result.getError();
            assertThat(mb.getErrors()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("tryRun 方法测试")
    class TryRunTest {
        @Test
        @DisplayName("当执行成功时应返回OK")
        void shouldReturnOkWhenSuccess() {
            Result<Void> result = Results.tryRun(() -> {
            }, TestResponseCode.SYSTEM_ERROR);
            assertThat(result.isSuccess()).isTrue();
        }

        @Test
        @DisplayName("当抛出异常时应返回Fail")
        void shouldReturnFailWhenException() {
            Result<Void> result = Results.tryRun(() -> {
                throw new RuntimeException("error");
            }, TestResponseCode.SYSTEM_ERROR);

            assertThat(result.isFailure()).isTrue();
            assertThat(result.getError().getResponseCode().getCode()).isEqualTo(TestResponseCode.SYSTEM_ERROR.getCode());
        }
    }

    @Nested
    @DisplayName("when 方法测试")
    class WhenTest {
        @Test
        @DisplayName("当条件满足时应执行Supplier")
        void shouldExecuteWhenTrue() {
            Result<String> result = Results.when(true, () -> Result.ok("executed"));
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.get()).isEqualTo("executed");
        }

        @Test
        @DisplayName("当条件不满足时应返回Null Result")
        void shouldReturnNullWhenFalse() {
            Result<String> result = Results.when(false, () -> Result.ok("executed"));
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.get()).isNull();
        }
    }

    @Nested
    @DisplayName("getOrNull 方法测试")
    class GetOrNullTest {
        @Test
        @DisplayName("当Result成功时应返回值")
        void shouldReturnValueWhenResultIsSuccess() {
            Result<String> result = Result.ok("test");
            assertThat(Results.getOrNull(result)).isEqualTo("test");
        }

        @Test
        @DisplayName("当Result失败时应返回null")
        void shouldReturnNullWhenResultIsFailure() {
            Result<String> result = Result.fail(TestResponseCode.PARAM_ERROR);
            assertThat(Results.getOrNull(result)).isNull();
        }
    }
}
