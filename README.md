# Fail-Fast Spring Boot Starter

[![Java CI with Maven](https://github.com/KyrieChao/Failure/actions/workflows/ci.yml/badge.svg)](https://github.com/KyrieChao/Failure/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/KyrieChao/Failure/graph/badge.svg)](https://codecov.io/KyrieChao/Failure)
[![Release](https://jitpack.io/v/KyrieChao/Failure.svg)](https://jitpack.io/#KyrieChao/Failure)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

[English Version](./README.en.md)

本项目是对 Fail‑Fast 校验框架的增强实现与示例说明。本文档为中文版本；结构与示例代码风格参考上游项目：
- https://github.com/KyrieChao/Failure
- https://github.com/KyrieChao/Failure-in-Action

链接、占位符与依赖坐标保持与原项目一致。英文版请见顶部“English Version”链接。

---

### 项目简介

Fail‑Fast 是一个专为 Spring Boot 3.x 设计的轻量级、高性能参数校验与业务异常处理框架，遵循“Fail Fast, Fail Safe”设计哲学，提供：
- 链式 Fluent API（快速失败/全量收集）
- 注解驱动（`@Validate` + 自定义 `FastValidator`）
- 函数式结果处理（`Result<T>` 与 `Results` 工具）
- 与 Jakarta Bean Validation 的桥接适配
- 错误码到 HTTP 状态码的灵活映射与影子追踪（`shadow-trace`）

该实现完整覆盖核心模块：入口 [Failure](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/Failure.java)、校验链 [Chain](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/internal/Chain.java)、异常模型 [Business](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/internal/Business.java)、结果类型 [Result](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/result/Result.java) 与工具 [Results](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/result/Results.java)、注解与切面 [Validate](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/annotation/Validate.java) / [FastValidator](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/annotation/FastValidator.java) / [ValidationAspect](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/aspect/ValidationAspect.java)，以及自动配置与异常处理 [FailFastAutoConfiguration](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/config/FailFastAutoConfiguration.java) / [CodeMappingConfig](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/config/CodeMappingConfig.java) / [DefaultExceptionHandler](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/advice/DefaultExceptionHandler.java)。

### 快速开始

- Java 17+
- Spring Boot 3.2.x
- 最新版本：参考 JitPack 徽章或 Releases

添加 JitPack 仓库与依赖：

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
    </repositories>
```

```xml
<dependency>
    <groupId>com.github.KyrieChao</groupId>
    <artifactId>Failure</artifactId>
    <version>Tag</version> <!-- 建议使用最新 Release 版本，例如 1.2.1 -->
</dependency>
```

参考实战项目：https://github.com/KyrieChao/Failure-in-Action

### 核心特性

- 链式校验：对象/字符串/集合/数组/数值/日期时间/枚举/同一性/布尔/Map/Optional 等丰富断言
- 自定义断言：`satisfies` 与跨字段 `compare`
- 快速失败与全量收集：`Failure.begin()` 与 `Failure.strict()`；终结操作 `fail()` / `failAll()`
- 注解驱动：`@Validate(fast = true|false, value = {...})` + `FastValidator.ValidationContext`
- 函数式结果：`Result.ok/fail/ofNullable`、`map/flatMap/filter/recover/combine`；`Results.tryOf/tryRun/sequence/traverse` 等
- 异常处理：`DefaultExceptionHandler` + `FailFastExceptionHandler` 可扩展，支持 verbose 多错误响应
- 配置化映射：`fail-fast.code-mapping.http-status` 与分组 `groups`，优雅映射业务码到 HTTP 状态

### API 列表（按功能分组）

- 入口
  - Failure: `begin()`、`strict()`、`with(ValidationContext)`

- 终结/控制
  - Chain: `fail()`、`failAll()`、`onFail(Runnable)`、`onFailGet(Supplier)`、`failNow(...)`、`verify()`

- 自定义与比较
  - `satisfies(value, predicate, ...)`、`compare(f1, f2, comparator, ...)`

- 对象/同一性/布尔
  - 对象：`exists/notNull/isNull/instanceOf/notInstanceOf/allNotNull`
  - 同一性：`same/notSame/equals/notEquals`
  - 布尔：`state/isTrue/isFalse`

- 字符串
  - `blank/notBlank/notEmpty/lengthBetween/lengthMin/lengthMax`
  - `match/email/startsWith/endsWith/contains/notContains`
  - `isNumeric/isAlpha/isAlphanumeric/isLowerCase/isUpperCase`
  - `mobile/url/ipAddress/uuid`

- 集合/数组/Map/Optional
  - 集合：`notEmpty/sizeBetween/sizeEquals/contains/notContains/isEmpty/hasNoNullElements/allMatch/anyMatch`
  - 数组：同集合 API 语义
  - Map：`notEmpty/isEmpty/containsKey/notContainsKey/containsValue/sizeBetween/sizeEquals`
  - Optional：`isPresent/isEmpty`

- 数值
  - `positive/nonNegative/greaterThan/greaterOrEqual/lessThan/lessOrEqual`
  - `inRange/inRangeNumber/notZero/isZero/negative/multipleOf/decimalScale`

- 日期/时间（`Date`/`Instant`/`LocalDate`/`LocalDateTime`/`ZonedDateTime` 可用）
  - `after/before/afterOrEqual/beforeOrEqual/between/isPast/isFuture/isToday`

- 注解驱动
  - `@Validate`：属性 `value` 指定验证器，`fast` 控制快速失败
  - `FastValidator<T>`：`validate(T, ValidationContext)`；`ValidationContext` 提供 `reportError/stop/isFailed`
  - [ValidationAspect](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/aspect/ValidationAspect.java) 负责织入执行

- 函数式结果
  - Result: `ok/fail/ofNullable`、`map/flatMap/peek/peekError/filter/recover/recoverWith/combine`
  - Results: `tryOf/tryRun/sequence/sequenceAll/traverse/getOrNull/when`

- Bean Validation 集成
  - ValidationAdapter: `validate`(快失败)、`validateAll`(多错误)、`validateToList`、`isValid`

> 更详细方法签名与实现请直接参见源码：
> [Chain](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/internal/Chain.java)、[Result](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/result/Result.java)、[Results](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/result/Results.java)。

### 运行示例

1) 服务内部链式校验（快速失败）

```java
Failure.begin()
    .notBlank(dto.getUsername(), ResponseCode.of(40001, "用户名不能为空"))
    .email(dto.getEmail(), ResponseCode.of(40004, "邮箱格式错误"))
    .fail();
```

2) 注解 + 自定义验证器（全量收集）

```java
@Validate(value = {UserLoginValidator.class}, fast = false)
public User login(UserLoginDTO dto) { ... }
```

3) 函数式结果

```java
Result<User> r = Results.tryOf(() -> repo.findById(id), ResponseCode.of(50000, "DB Error"));
UserDTO dto = r.map(UserMapper::toDTO)
               .filter(UserDTO::active, ResponseCode.of(42200, "用户未激活"))
               .recover(err -> UserDTO.guest())
               .get();
```

4) Bean Validation 适配

```java
validationAdapter.validateAll(userDto); // 失败将抛出 Business 或 MultiBusiness
```

### 配置参数

```yaml
fail-fast:
  shadow-trace: true   # 在异常中包含方法与位置
  verbose: true        # 多错误响应包含 errors 列表
  code-mapping:
    http-status:
      40001: 400
      40100: 401
    groups:
      auth: [ "40100..40199" ]
      business: [ "40000..40099", 42200 ]
```

映射优先级：标准 HTTP 码 → 精确配置 → 前缀范围 → 大类兜底（4xxxx→400, 5xxxx→500）。

### 贡献指南

- Fork 仓库，创建特性分支
- 提交前执行 `mvn test`
- 确保新增/修改包含相应测试，遵循现有代码风格
- 通过 Pull Request 提交，关联 Issue（若有）

### 许可证声明

Apache License 2.0，参见 LICENSE。

### 作者信息与联系方式

- Author: KyrieChao
- GitHub: https://github.com/KyrieChao
- Issues: https://github.com/KyrieChao/Failure/issues

— 完 —
