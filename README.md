# Fail-Fast Spring Boot Starter

一个优雅、高性能的 Spring Boot 参数验证与业务异常处理框架。专为提升开发体验而设计，支持链式调用、注解驱动以及标准的 Bean Validation 集成。

## ✨ 核心特性

- **链式 API (Chain API)**: 流畅的构建者模式，支持“快速失败”和“收集所有错误”两种模式。
- **丰富的验证类型**: 支持对象、字符串、集合、数组、数值、日期、枚举等多种类型的验证。
- **注解驱动 (Annotation)**: 提供 `@Validate` 注解，支持自定义验证器 (Validator) 和分组校验 (Groups)。
- **Bean Validation 集成**: 完美兼容 JSR-303/380 (Hibernate Validator)，可无缝集成现有 DTO 校验。
- **智能日志 (Smart Logging)**: 自动清洗堆栈信息，精准定位业务代码行，告别冗长的异常堆栈干扰。
- **统一异常处理**: 内置全局异常处理器，自动将业务异常转换为标准 JSON 响应。

## 🛠️ 环境要求

- Java 17+
- Spring Boot 3.2.0+

## 📦 安装说明

在你的 Maven 项目的 `pom.xml` 中添加以下依赖：

```xml
<dependency>
    <groupId>com.chao</groupId>
    <artifactId>fail-fast-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## ⚙️ 快速配置

在 `application.yml` 中进行基础配置（可选）：

```yaml
fail-fast:
  shadow-trace: false    # 是否在日志中打印方法名，生产环境建议关闭，默认 false

  # 错误码映射配置（可选）
  code-mapping:
    http-status:
      40401: 404         # 将业务错误码 40401 映射为 HTTP 404 状态码
    groups:
      auth: [40100, 40199] # 定义错误码组
```

## 🚀 使用指南

### 1. 定义错误码

首先，实现 `ResponseCode` 接口定义你的业务错误码：

```java
public enum ErrorCode implements ResponseCode {
    PARAM_ERROR(40001, "参数错误", "请求参数校验失败"),
    USER_NOT_FOUND(40401, "用户不存在", "请检查用户ID"),
    SYSTEM_ERROR(50000, "系统异常", "请稍后重试");
    
    private final int code;
    private final String message;
    private final String description;
    
    // 构造函数、Getter 省略...
}
```

### 2. 编程式校验 (Chain API)

适用于复杂的业务逻辑校验，支持高度定制。

**场景 1：快速失败 (Fail-Fast)**
默认模式。验证链中任何一步失败都会标记为失败，后续验证将被跳过，最后调用 `failAll()` 抛出异常。

```java
public void register(UserDTO user) {
    // 从 Failure 类的静态方法开始验证链
    Failure.notBlank(user.getUsername(), ErrorCode.PARAM_ERROR)
        // 数值范围检查，支持自定义错误详情
        .inRange(user.getAge(), 18, 60, f -> f.code(ErrorCode.PARAM_ERROR).detail("年龄限制18-60岁"))
        // 自定义断言
        .isTrue(checkUnique(user.getUsername()), f -> f.code(ErrorCode.PARAM_ERROR).detail("用户名已存在"))
        // 数组/集合验证
        .notEmpty(user.getRoles(), ErrorCode.PARAM_ERROR)
        // 遇到错误抛出 Business 异常
        .failAll();
}
```

**场景 2：收集模式 (Collect-All)**
使用 `Failure.strict()` 开启收集模式。验证失败不会中断后续检查，最后一次性抛出包含所有错误的异常。

```java
public void batchCheck(UserDTO user) {
    Failure.strict() // 开启 strict 模式
        .email(user.getEmail(), ErrorCode.PARAM_ERROR)
        .match(user.getPhone(), "^1[3-9]\\d{9}$", f -> f.detail("手机号格式错误"))
        // 数组包含性检查
        .contains(user.getTags(), "VIP", f -> f.detail("必须包含VIP标签"))
        // 如果有错误，抛出 MultiBusiness 异常（包含所有错误信息）
        .failAll();
}
```

### 3. 声明式校验 (注解驱动)

适用于 Controller 层的方法参数校验，代码简洁。

**简单注解使用：**

```java
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/{id}")
    @Validate // 开启 Fail-Fast 切面支持
    public Result<User> getUser(
            // 使用标准 Bean Validation 注解
            @NotNull(message = "ID不能为空") Long id,
            @NotBlank(message = "名称不能为空") String name
    ) {
        return Result.ok(userService.get(id));
    }
}
```

**自定义 Validator：**

```java
@PostMapping("/create")
@Validate(value = UserValidator.class) // 指定自定义验证器逻辑
public Result<Void> createUser(@RequestBody UserDTO user) {
    return Result.ok();
}

// 实现 Validator 接口
public static class UserValidator implements Validator<UserDTO> {
    @Override
    public void validate(UserDTO user, ValidationContext context) {
        // 在这里可以使用 Chain API 进行复杂校验
        // 如果校验失败，context 会自动收集错误
        Failure.notNull(user, ErrorCode.PARAM_ERROR)
               .isTrue(user.getAge() >= 18, f -> f.detail("未成年人禁止注册"))
               .failAll(); // 注意：Validator 中也可以选择不手动抛出，而是通过 context.addError 添加错误
    }
}
```

### 4. Bean Validation 集成

完美兼容 Hibernate Validator (JSR-303)，无需改变现有的 DTO 定义。

**DTO 定义：**
```java
public class UserDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @Min(value = 18, message = "年龄必须大于18")
    private Integer age;
    
    @NotNull
    @Size(min = 1)
    private String[] hobbies; // 支持数组校验
}
```

**Controller 使用：**
结合 `@Validate` 和 `@Valid` (或不加 @Valid，框架会自动处理)，自动将校验结果转换为 `Business` 异常。

```java
@PostMapping("/user")
@Validate // 切面拦截
public Result<UserDTO> createUser(@RequestBody UserDTO user) {
    // 如果 DTO 校验失败，会自动抛出 Business 异常，无需手动处理 BindingResult
    return Result.ok(user);
}
```

**手动调用 (Service 层)：**
注入 `ValidationAdapter` 进行手动校验。

```java
@Service
public class UserService {
    @Autowired
    private ValidationAdapter validationAdapter;
    
    public void process(UserDTO user) {
        // 执行校验，如果有错误直接抛出 Business 异常
        validationAdapter.validate(user);
        
        // 或者校验并返回 boolean
        if (!validationAdapter.isValid(user)) {
             // ...
        }
    }
}
```

## 📝 响应格式示例

### 单个错误响应
```json
{
  "code": 40001,
  "message": "参数错误",
  "description": "年龄限制18-60岁",
  "timestamp": 1708092000000
}
```

### 批量错误响应 (MultiBusiness)
```json
{
  "code": 400,
  "message": "Multiple validation errors",
  "errorCount": 2,
  "errors": [
    { "code": 40001, "message": "邮箱格式错误" },
    { "code": 40001, "message": "手机号格式错误" }
  ],
  "timestamp": 1708092000000
}
```

## 📄 许可证

MIT License
