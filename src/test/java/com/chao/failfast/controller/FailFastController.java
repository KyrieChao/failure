package com.chao.failfast.controller;

import com.chao.failfast.Failure;
import com.chao.failfast.annotation.ToImprove;
import com.chao.failfast.annotation.Validate;
import com.chao.failfast.integration.ValidationAdapter;
import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.ResponseCode;
import com.chao.failfast.model.entity.Usage;
import com.chao.failfast.model.entity.User;
import com.chao.failfast.model.entity.User2;
import com.chao.failfast.model.enums.UsageCode;
import com.chao.failfast.model.enums.UserCode;
import com.chao.failfast.result.Result;
import com.chao.failfast.validator.CustomValidators;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Fail-Fast 框架官方使用模板（推荐阅读）
 * <p>
 * 本类按推荐程度和使用频率整理了最常用的 4 种用法：
 * 1. 编程式链式校验（日常最推荐）
 * 2. 注解式校验（最简洁）
 * 3. 编程式手动校验（灵活场景）
 * 4. 完全手动处理错误列表（高级场景）
 */
@RestController
@RequestMapping("/failfast")
@Validated
public class FailFastController {

    @Resource
    private ValidationAdapter validationAdapter;

    // =================================================================================
    // 1. 编程式链式校验（日常最推荐）
    // =================================================================================

    /**
     * 快速失败模式（Fail-Fast）—— 日常最推荐的写法
     * 遇到第一个错误立即抛出异常，终止执行
     */
    @PostMapping("/chain/fast")
    public Result<User> chainFast(@RequestBody User user) {
//        Failure.begin()
//                .exists(user, UserCode.Not_Exist)
//                .notBlank(user.getUsername(), UserCode.Username_Blank, "demo")
//                .positive(user.getAge(), UserCode.Age_Not_Positive)
//                .inRange(user.getAge(), 0, 120, UserCode.Age_Out_Of_Range)
//                .match(user.getPhone(), "^1[3-9]\\d{9}$", UserCode.Phone_Not_Match)
//                .email(user.getEmail(), UserCode.Email_Not_Match)
//                .fail();           // 推荐写法
        Failure.begin()
                .exists(user)
                .notBlank(user.getUsername())
                .positive(user.getAge())
                .failNow(ResponseCode.of(40001, "user object not found", "error"))
                .inRange(user.getAge(), 0, 120)
                .match(user.getPhone(), "^1[3-9]\\d{9}$")
                .email(user.getEmail())
                .failNow(ResponseCode.of(40002, "user object not found2", "error2"));
        // 不推荐 写法（不详细）
        return Result.ok(user);
    }

    /**
     * 严格模式（收集所有错误）
     * 适合需要一次性返回所有校验错误的场景
     */
    @PostMapping("/chain/strict")
    public Result<User> chainStrict(@RequestBody User user) {
        Failure.strict()
                .exists(user, UserCode.Not_Exist)
                .exists(user, ResponseCode.of(40001, "user not found", "user not found"))
                .notBlank(user.getUsername(), UserCode.Username_Blank)
                .positive(user.getAge(), UserCode.Age_Not_Positive)
                .inRange(user.getAge(), 0, 120, UserCode.Age_Out_Of_Range)
                .match(user.getPhone(), "^1[3-9]\\d{9}$", UserCode.Phone_Not_Match)
                .email(user.getEmail(), UserCode.Email_Not_Match)
                .failAll();

        return Result.ok(user);
    }

    // =================================================================================
    // 2. 注解式校验（最简洁，推荐简单场景）
    // =================================================================================

    /**
     * 使用 Spring 标准 @Valid（最常用注解式写法）
     */
    @PostMapping("/annotation/valid")
    public Result<User2> annotationValid(@RequestBody @Valid User2 user) {
        return Result.ok(user);
    }

    /**
     * 使用 @Validate + 自定义验证器（复杂业务推荐）
     * fast = false 表示收集所有错误
     */
    @PostMapping("/annotation/custom")
    @Validate(value = {CustomValidators.class}, fast = false)
    public Result<User2> annotationCustom(@RequestBody User2 user) {
        return Result.ok(user);
    }

    // =================================================================================
    // 3. 编程式手动校验（需要灵活控制时使用）
    // =================================================================================

    /**
     * 使用 ValidationAdapter - 快速失败模式
     */
    @PostMapping("/adapter/fast")
    public Result<User> adapterFast(@RequestBody User request) {
        validationAdapter.validate(request);   // 失败立即抛异常
        return Result.ok(request);
    }

    /**
     * 使用 ValidationAdapter - 收集所有错误
     */
    @PostMapping("/adapter/all")
    public Result<User> adapterAll(@RequestBody User request) {
        validationAdapter.validateAll(request);   // 收集所有错误后抛出
        return Result.ok(request);
    }

    /**
     * 完全手动处理校验结果（不抛异常）
     * 适合需要自定义处理错误的场景
     */
    @PostMapping("/manual")
    public Result<String> manual(@RequestBody Usage dto) {
        List<Business> errors = validationAdapter.validateToList(dto);

        if (!errors.isEmpty()) {
            return Result.fail(UsageCode.PARAM_ERROR, "检测到 " + errors.size() + " 个错误");
        }

        return Result.ok("Manual Check Passed");
    }
}
