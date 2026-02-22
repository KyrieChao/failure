package com.chao.failfast.controller;

import com.chao.failfast.model.enums.UsageCode;
import com.chao.failfast.result.Result;
import com.chao.failfast.result.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Fail-Fast 框架 Result / Results 使用演示（官方模板）
 * <p>
 * 本类按推荐程度和使用频率整理了 Result 和 Results 的核心用法
 * 每个接口只演示一类功能，建议按顺序阅读
 */
@RestController
@RequestMapping("/result")
public class ResultController {

    private static final Logger log = LoggerFactory.getLogger(ResultController.class);

    // =================================================================================
    // 1. Result 基础创建与状态（最基础）
    // =================================================================================

    /**
     * Result 的创建方式和基本用法
     */
    @GetMapping("/basic")
    public Result<Map<String, Object>> basic(@RequestParam(required = false) String input) {
        Map<String, Object> demo = new LinkedHashMap<>();

        demo.put("ok", Result.ok("操作成功"));
        demo.put("fail_simple", Result.fail(UsageCode.PARAM_ERROR));
        demo.put("fail_with_detail", Result.fail(UsageCode.PARAM_ERROR, "用户名不能为空"));

        demo.put("ofNullable_success", Result.ofNullable(input, UsageCode.PARAM_REQUIRED, "输入不能为空"));
        demo.put("ofNullable_fail", Result.ofNullable(null, UsageCode.PARAM_REQUIRED, "输入不能为空"));

        return Result.ok(demo);
    }

    // =================================================================================
    // 2. 函数式操作（map / flatMap / peek）
    // =================================================================================

    /**
     * Result 的函数式编程操作
     */
    @GetMapping("/functional")
    public Result<Map<String, Object>> functional(@RequestParam(required = false) String input) {
        Result<String> base = Result.ofNullable(input, UsageCode.PARAM_REQUIRED, "输入不能为空");

        Map<String, Object> demo = new LinkedHashMap<>();

        demo.put("map", base.map(String::length));
        demo.put("flatMap", base.flatMap(s -> Result.ok(s.toUpperCase())));

        // peek 只做副作用，不影响结果
        base.peek(val -> log.debug("peek 值: {}", val))
                .peekError(err -> log.warn("peek 错误: {}", err.getDetail()));

        return Result.ok(demo);
    }

    // =================================================================================
    // 3. 过滤与错误恢复
    // =================================================================================

    /**
     * filter、recover、recoverWith 等恢复操作
     */
    @GetMapping("/filter-recover")
    public Result<Map<String, Object>> filterRecover(@RequestParam(required = false) String input) {
        Result<String> base = Result.ofNullable(input, UsageCode.PARAM_REQUIRED, "输入不能为空");

        Map<String, Object> demo = new LinkedHashMap<>();

        demo.put("filter", base.filter(s -> s.length() > 5, UsageCode.PARAM_ERROR, "字符串太短"));

        demo.put("recover", base.recover(err -> "默认兜底值"));
        demo.put("recoverWith", base.recoverWith(err -> Result.ok("从错误中恢复")));

        return Result.ok(demo);
    }

    // =================================================================================
    // 4. Results 工具类（批量操作 / 异常包装）
    // =================================================================================

    /**
     * Results 工具类的常用方法
     */
    @GetMapping("/tools")
    public Result<Map<String, Object>> tools() {
        Map<String, Object> demo = new LinkedHashMap<>();

        // tryOf / tryRun
        demo.put("tryOf", Results.tryOf(() -> "正常执行", UsageCode.SYSTEM_ERROR));
        demo.put("tryRun", Results.tryRun(() -> log.debug("执行副作用"), UsageCode.SYSTEM_ERROR));

        // sequence / sequenceAll
        demo.put("sequence", Results.sequence(
                Result.ok("A"), Result.ok("B"), Result.fail(UsageCode.PARAM_ERROR)
        ));

        demo.put("sequenceAll", Results.sequenceAll(
                Result.ok("A"),
                Result.fail(UsageCode.PARAM_ERROR, "错误1"),
                Result.fail(UsageCode.PARAM_ERROR, "错误2")
        ));

        // traverse（安全写法）
        List<String> numbers = Arrays.asList("1", "2", "abc");
        Result<List<Integer>> parsed = Results.traverse(numbers, s ->
                Results.tryOf(() -> Integer.parseInt(s),
                        UsageCode.PARAM_ERROR, "解析失败: " + s)
        );
        demo.put("traverse", parsed);

        return Result.ok(demo);
    }
}
