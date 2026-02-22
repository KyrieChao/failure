# Fail-Fast Spring Boot Starter

[![Release](https://jitpack.io/v/KyrieChao/Failure.svg)](https://jitpack.io/#KyrieChao/Failure)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

**Fail-Fast** 是一个专为 Spring Boot 设计的轻量级、高性能参数校验与业务异常处理框架。它秉承 "Fail Fast, Fail Safe" 的设计哲学，提供流式 API（Fluent API）与注解驱动两种使用模式，旨在解决传统 `if-else` 参数校验代码冗余、错误码管理混乱以及异常处理不统一的痛点。

核心特性包括：
- **链式校验**：提供优雅的 Fluent API，支持 `fail-fast`（快速失败）与 `fail-safe`（全量收集）两种模式。
- **注解驱动**：无缝集成 Spring AOP，支持声明式校验与自定义验证器。
- **统一异常**：内置标准化的业务异常体系与全局异常处理器，自动映射 HTTP 状态码。
- **零侵入性**：作为 Starter 引入，开箱即用，与 Spring Validation (`@Valid`) 完美兼容。

---

## 快速开始 (Quick Start)

### 1. 引入依赖

#### 方式一：使用 JitPack (推荐)

无需等待 Maven Central 同步，直接使用 GitHub 最新版本。

第一步：在项目的 `pom.xml` 中添加 JitPack 仓库：

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

第二步：添加依赖：

```xml
<dependency>
    <groupId>com.github.KyrieChao</groupId>
    <artifactId>Failure</artifactId>
    <version>Tag</version> <!-- 将 Tag 替换为具体的版本号，如 1.0.0 -->
</dependency>
```

#### 方式二：Maven Central (规划中)

目前暂未发布到 Maven Central，请使用 JitPack 或本地安装。

```xml
<!-- 暂不可用 -->
<dependency>
    <groupId>com.chao</groupId>
    <artifactId>fail-fast-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 最小可运行示例

在 Service 或 Controller 中使用 `Failure.begin()` 开启链式校验：

```java
import com.chao.failfast.Failure;
import com.chao.failfast.internal.ResponseCode;

@Service
public class UserService {

    public void register(UserDTO user) {
        // 开启快速失败校验链
        Failure.begin()
            .notNull(user, ResponseCode.PARAM_ERROR)                  // 校验对象非空
            .notBlank(user.getUsername(), ResponseCode.NAME_EMPTY)    // 校验字符串非空
            .match(user.getPhone(), "^1[3-9]\\d{9}$", ResponseCode.PHONE_INVALID) // 正则校验
            .fail(); // 执行校验，遇错即抛出异常

        // 业务逻辑...
    }
}
```

---

## 核心特性 (Core Features)

### 1. 编程式链式校验 (Fluent Validation)

Fail-Fast 提供两种校验模式：

*   **快速失败 (Fail-Fast)**: 遇到第一个错误立即抛出异常，适用于阻断性校验。
*   **全量收集 (Fail-Safe/Strict)**: 执行所有校验规则，收集所有错误后统一抛出，适用于表单批量校验。

```java
// 模式一：快速失败（推荐）
Failure.begin()
        .exists(user, UserCode.NOT_EXIST)
        .notBlank(user.getUsername(), UserCode.USERNAME_BLANK, "demo")
        .email(user.getEmail(), UserCode.EMAIL_INVALID)
        .fail();

// 模式二：全量收集
Failure.strict()
        .exists(user, UserCode.NOT_EXIST)
        .exists(user, ResponseCode.of(40001, "user not found"))
        .notBlank(user.getUsername(), UserCode.USERNAME_BLANK)
        .email(user.getEmail(), UserCode.EMAIL_INVALID)
        .failAll();

// 模式三：分段校验
Failure.begin()
        .exists(user)
        .notBlank(user.getUsername())
        .failNow(ResponseCode.of(40001, "user object invalid"))
        .inRange(user.getAge(), 0, 120)
        .match(user.getPhone(), "^1[3-9]\\d{9}$")
        .failNow(ResponseCode.of(40002, "user detail invalid"));
```

### 2. 注解驱动校验 (Annotation Driven)

通过 `@Validate` 注解与 `FastValidator` 接口，实现业务校验逻辑的复用与解耦。

**定义验证器：**

```java
@Component
public class UserValidator extends TypedValidator<UserDTO> {
    @Override
    public void validate(UserDTO dto, FailureContext ctx) {
        if (userMapper.exists(dto.getUsername())) {
            ctx.reportError(ResponseCode.USER_EXISTS);
        }
    }
}
```

**使用注解：**

```java
@PostMapping("/users")
@Validate(value = UserValidator.class, fast = true) // fast=true 开启快速失败
public Result<Void> createUser(@RequestBody UserDTO user) {
    userService.create(user);
    return Result.ok();
}
```

### 3. 内置丰富断言库

Fail-Fast 提供了极其丰富的校验方法，涵盖对象、字符串、数值、集合、日期等常见场景。

**[点击查看完整 API 列表 (API Reference)](API_REFERENCE.md)**

部分常用方法示例：

| 分类 | 方法示例 | 说明 |
| :--- | :--- | :--- |
| **对象** | `notNull`, `isNull`, `equals` | 基础对象判空与相等性检查 |
| **字符串** | `notBlank`, `length`, `match`, `email` | 长度、正则、格式校验 |
| **数值** | `positive`, `negative`, `range` | 数值范围与正负检查 |
| **集合/数组** | `notEmpty`, `size`, `contains` | 集合大小与元素包含检查 |
| **日期** | `future`, `past`, `between` | 日期时间先后判断 |
| **布尔** | `isTrue`, `isFalse` | 状态断言 |

### 4. 函数式结果处理 (Functional Result Handling)

除了抛出异常，Fail-Fast 还提供了 `Result<T>` 和 `Results` 工具类，支持函数式编程风格的错误处理，避免异常作为控制流。

**基础用法：**

```java
// 返回成功
Result<String> success = Result.ok("data");

// 返回失败
Result<String> failure = Result.fail(ResponseCode.PARAM_ERROR);

// 链式处理
String value = success.map(String::toUpperCase)
                     .recover(err -> "default")
                     .get();
```

**高级用法 (Results 工具类)：**

```java
// 1. 包装可能抛出异常的代码
Result<User> result = Results.tryOf(() -> userService.findUser(id), ResponseCode.DB_ERROR);

// 2. 批量处理（Fail-Fast 模式）
// 如果所有操作成功，返回 List<T>；如果有任意失败，返回第一个错误
Result<List<User>> batchResult = Results.sequence(
    userService.createUser(u1),
    userService.createUser(u2)
);

// 3. 批量处理（Fail-Safe 模式）
// 收集所有结果，如果存在失败，返回包含所有错误的 MultiBusiness 异常
Result<List<User>> safeResult = Results.sequenceAll(
    userService.createUser(u1),
    userService.createUser(u2)
);
```

---

## 状态码映射 (Code Mapping)

Fail-Fast 支持灵活的业务错误码到 HTTP 状态码的映射机制。通过 `CodeMappingConfig`，您可以定义不同层级的映射规则。

**映射优先级：**

1.  **精确匹配**：配置中明确指定的映射（如 `40001 -> 400`）。
2.  **范围匹配**：根据错误码前缀匹配（如 `401xx` 映射为 `401`）。
3.  **大类匹配**：
    *   `40000 - 49999` -> `400 Bad Request`
    *   `50000 - 59999` -> `500 Internal Server Error`
4.  **默认回退**：其他情况默认为 `500`。

## 配置详解 (Configuration)

在 `application.yml` 中可进行如下配置：

```yaml
fail-fast:
  # 是否开启影子追踪（在异常日志中打印触发校验的代码位置，方便调试）
  shadow-trace: true
  code-mapping:
    http-status:
      40001: 400  # BAD_REQUEST
      40101: 401  # UNAUTHORIZED
      40301: 403  # FORBIDDEN
      40401: 404  # NOT_FOUND
      42201: 422  # UNPROCESSABLE_ENTITY
      42901: 429  # TOO_MANY_REQUESTS
      50001: 500  # INTERNAL_SERVER_ERROR
    groups:
      auth: [ "40100..40199" ]
      user: [ "40400..40499" ]
      product: [ 40400,40499 ]
      order: [ "40000", "40001","40400..40499" ]
      system: "50000..59999"
```

---

## 异常码说明 (Error Codes)

框架使用 `ResponseCode` 接口定义错误码，推荐在项目中通过枚举实现该接口以统一管理错误码。

| 错误码 (Code) | 描述 (Message) | 建议 HTTP 状态码 |
| :--- | :--- | :--- |
| `20000` | 成功 | 200 |
| `40000` | 请求参数错误 | 400 |
| `40100` | 未授权 | 401 |
| `40300` | 禁止访问 | 403 |
| `50000` | 系统内部错误 | 500 |

---

## 版本兼容性 (Compatibility)

| Fail-Fast Version | Java Version | Spring Boot Version |
| :--- | :--- | :--- |
| 1.0.0 | 17+ | 3.2.x |

---

## 常见问题 (FAQ)

**Q: `Failure.begin()` 和 `Failure.strict()` 有什么区别？**

A: `begin()` 是快速失败模式，一旦某个校验不通过，立即抛出 `Business` 异常，后续校验不再执行；`strict()` 是严格模式，会执行完所有校验链，将所有错误收集到 `MultiBusiness` 异常中抛出，适合需要一次性返回所有错误字段的场景。

**Q: 如何自定义异常处理逻辑？**

A: 框架提供了默认的 `GlobalExceptionHandler`。如果您需要自定义，可以实现 `FailFastExceptionHandler` 接口，或者定义自己的 `@RestControllerAdvice` 类，并使用 `@Order` 注解确保优先级高于默认处理器。

**Q: 支持分组校验吗？**

A: 支持。在自定义验证器 `FastValidator` 中，您可以根据 `FailureContext` 中的上下文信息来实现分组逻辑，或者简单地定义多个不同的验证器类。

---

**Fail-Fast** is open source software released under the [Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0.html).

## 参与贡献 (Contributing)

欢迎提交 Issue 和 Pull Request！详细指南请参考 [CONTRIBUTING.md](CONTRIBUTING.md)。

## 版本历史 (Changelog)

查看 [CHANGELOG.md](CHANGELOG.md) 获取详细的版本变更记录。
