# API Reference

[English Version](./API_REFERENCE.en.md)

本文档详尽列出了 `Failure` 校验框架的核心 API、设计模式及最佳实践。基于 fluent-interface 风格设计，旨在提供高可读性、强类型安全且易于扩展的校验体验。

---

## 目录

1. [设计模式与核心入口](#1-设计模式与核心入口)
2. [校验方法详解](#2-校验方法详解)
3. [终结操作](#3-终结操作)
4. [最佳实践](#4-最佳实践)

---

## 1. 设计模式与核心入口

框架支持三种核心校验模式，分别适用于不同的业务场景：

### 1.1 Fail-Fast (快速失败模式)

**适用场景**: 参数防御性编程，一旦发现非法参数立即停止后续逻辑，避免资源浪费。

```java
// 一旦 notBlank 失败，立即抛出异常，不会执行后续校验
Failure.begin()
    .notBlank(username, UserCode.USERNAME_REQUIRED)
    .email(email, UserCode.EMAIL_INVALID)
    .fail();
```

**特点**:

- 遇到第一个错误立即停止
- 抛出 `BusinessException`
- 性能最优

---

### 1.2 Fail-Strict (全量收集模式)

**适用场景**: 批量导入、表单提交等需要一次性返回所有错误的场景。

```java
// 即使 username 为空，也会继续检查 email，最终收集所有错误
Failure.strict()
    .notBlank(username, UserCode.USERNAME_REQUIRED)
    .email(email, UserCode.EMAIL_INVALID)
    .failAll();
```

**特点**:

- 执行所有校验规则
- 抛出 `MultiBusinessException`（包含所有错误）
- 适合前端表单一次性展示所有错误

---

### 1.3 Contextual (上下文集成模式)

**适用场景**: 结合 `TypedValidator` 或 `FastValidator` 使用，将校验逻辑与业务逻辑解耦。

```java
// 错误实时报告给 ctx，不立即抛出
Failure.with(ctx)
    .notBlank(dto.getUsername(), UserCode.USERNAME_BLANK)
    .email(dto.getEmail(), UserCode.EMAIL_INVALID)
    .verify();
```

**特点**:

- 校验全程不抛异常
- 错误写入 `ValidationContext`
- 由调用方决定后续处理

---

## 2. 校验方法详解

所有校验方法均支持以下四种重载形式（以 `notNull` 为例）：

1. `notNull(obj)` - 使用默认错误信息
2. `notNull(obj, code)` - 指定 `ResponseCode`
3. `notNull(obj, code, detail)` - 指定 `ResponseCode` 和详细描述
4. `notNull(obj, Consumer<Business.Fabricator>)` - 使用 Lambda 构建复杂错误

```java
// 示例：四种重载方式
Failure.begin()
    .notNull(obj)                                    // 方式1
    .notNull(obj, UserCode.REQUIRED)                 // 方式2
    .notNull(obj, UserCode.REQUIRED, "不能为空")      // 方式3
    .notNull(obj, f -> f.responseCode(UserCode.REQUIRED).detail("自定义详情"))  // 方式4
    .fail();
```

---

### 2.1 通用对象校验 (Object)

| 方法                           | 描述                           |
| ------------------------------ | ------------------------------ |
| `notNull(obj)` / `exists(obj)` | 验证对象不为 null              |
| `isNull(obj)`                  | 验证对象必须为 null            |
| `instanceOf(obj, type)`        | 验证对象是指定类型的实例       |
| `notInstanceOf(obj, type)`     | 验证对象不是指定类型的实例     |
| `allNotNull(objs...)`          | 验证一组对象均不为 null        |
| `equals(obj1, obj2)`           | 验证对象相等 (`Object.equals`) |
| `notEquals(obj1, obj2)`        | 验证对象不等                   |
| `same(obj1, obj2)`             | 验证引用相同 (`==`)            |
| `notSame(obj1, obj2)`          | 验证引用不同 (`!=`)            |

---

### 2.2 字符串校验 (String)

| 方法                              | 描述                             |
| --------------------------------- | -------------------------------- |
| `notBlank(str)` / `notEmpty(str)` | 非 null 且去除首尾空格后长度 > 0 |
| `blank(str)`                      | 为 null、空串或仅包含空白字符    |
| `lengthBetween(str, min, max)`    | 字符长度在 [min, max] 之间       |
| `lengthMin(str, min)`             | 最小长度限制                     |
| `lengthMax(str, max)`             | 最大长度限制                     |
| `match(str, regex)`               | 符合正则表达式                   |
| `email(str)`                      | 邮箱格式校验                     |
| `mobile(str)`                     | 中国大陆手机号格式校验           |
| `url(str)`                        | URL 格式校验                     |
| `ipAddress(str)`                  | IPv4 地址格式校验                |
| `uuid(str)`                       | UUID 格式校验                    |
| `isNumeric(str)`                  | 仅包含数字                       |
| `isAlpha(str)`                    | 仅包含字母                       |
| `isAlphanumeric(str)`             | 仅包含字母或数字                 |
| `isLowerCase(str)`                | 全部小写                         |
| `isUpperCase(str)`                | 全部大写                         |
| `contains(str, sub)`              | 包含子串                         |
| `notContains(str, sub)`           | 不包含子串                       |
| `startsWith(str, prefix)`         | 以指定前缀开头                   |
| `endsWith(str, suffix)`           | 以指定后缀结尾                   |
| `equalsIgnoreCase(str1, str2)`    | 忽略大小写相等                   |

---

### 2.3 数值校验 (Number)

支持 `Integer`, `Long`, `Double`, `BigDecimal` 等所有 `Number` 子类。

| 方法                             | 描述                      |
| -------------------------------- | ------------------------- |
| `positive(num)`                  | 正数 (> 0)                |
| `nonNegative(num)`               | 非负数 (>= 0)             |
| `negative(num)`                  | 负数 (< 0)                |
| `notZero(num)`                   | 非零 (!= 0)               |
| `isZero(num)`                    | 等于零 (== 0)             |
| `greaterThan(val, threshold)`    | 大于 (>)                  |
| `greaterOrEqual(val, threshold)` | 大于等于 (>=)             |
| `lessThan(val, threshold)`       | 小于 (<)                  |
| `lessOrEqual(val, threshold)`    | 小于等于 (<=)             |
| `inRange(val, min, max)`         | 在区间 [min, max] 内      |
| `multipleOf(val, divisor)`       | 是 divisor 的倍数         |
| `decimalScale(decimal, scale)`   | BigDecimal 小数位精度检查 |

---

### 2.4 集合与容器校验

#### Collection / List / Set

| 方法                                | 描述                   |
| ----------------------------------- | ---------------------- |
| `notEmpty(collection)`              | 容器非 null 且包含元素 |
| `isEmpty(collection)`               | 容器为 null 或无元素   |
| `sizeBetween(collection, min, max)` | 元素数量在范围内       |
| `sizeEquals(collection, size)`      | 元素数量严格相等       |
| `sizeMin(collection, min)`          | 元素数量 >= min        |
| `sizeMax(collection, max)`          | 元素数量 <= max        |
| `contains(collection, element)`     | 包含指定元素           |
| `notContains(collection, element)`  | 不包含指定元素         |
| `hasNoNullElements(collection)`     | 容器内不包含 null 元素 |
| `allMatch(collection, predicate)`   | 所有元素均满足条件     |
| `anyMatch(collection, predicate)`   | 任一元素满足条件       |
| `noneMatch(collection, predicate)`  | 没有元素满足条件       |

#### Map

| 方法                         | 描述                     |
| ---------------------------- | ------------------------ |
| `notEmpty(map)`              | Map 非 null 且包含键值对 |
| `isEmpty(map)`               | Map 为 null 或无键值对   |
| `containsKey(map, key)`      | Map 包含指定 Key         |
| `containsValue(map, value)`  | Map 包含指定 Value       |
| `sizeBetween(map, min, max)` | 键值对数量在范围内       |

#### Array

| 方法                             | 描述                   |
| -------------------------------- | ---------------------- |
| `notEmpty(array)`                | 数组非 null 且长度 > 0 |
| `isEmpty(array)`                 | 数组为 null 或长度为 0 |
| `lengthBetween(array, min, max)` | 数组长度在范围内       |
| `contains(array, element)`       | 数组包含指定元素       |

---

### 2.5 日期时间校验 (Date/Time)

支持 `Date`, `LocalDate`, `LocalDateTime`, `Instant`, `ZonedDateTime`。

| 方法                        | 描述                       |
| --------------------------- | -------------------------- |
| `isPast(date)`              | 时间在当前时刻之前         |
| `isFuture(date)`            | 时间在当前时刻之后         |
| `isToday(date)`             | 日期是今天                 |
| `after(d1, d2)`             | d1 晚于 d2                 |
| `before(d1, d2)`            | d1 早于 d2                 |
| `between(date, start, end)` | 时间在区间 [start, end] 内 |
| `notBefore(date, boundary)` | 时间不早于 boundary        |
| `notAfter(date, boundary)`  | 时间不晚于 boundary        |

---

### 2.6 Optional 校验

| 方法                       | 描述                   |
| -------------------------- | ---------------------- |
| `isPresent(opt)`           | Optional 包含值        |
| `isEmpty(opt)`             | Optional 为空          |
| `ifPresent(opt, consumer)` | 如果有值，执行消费操作 |

---

### 2.7 枚举校验

| 方法                             | 描述                   |
| -------------------------------- | ---------------------- |
| `enumValue(enumClass, value)`    | 字符串是有效的枚举名称 |
| `enumConstant(value, enumClass)` | 枚举值属于指定类型     |
| `enumIn(value, constants...)`    | 枚举值在指定常量列表中 |

---

### 2.8 布尔与状态校验

| 方法                                 | 描述                       |
| ------------------------------------ | -------------------------- |
| `isTrue(bool)` / `state(bool, code)` | 布尔值为 true              |
| `isFalse(bool)`                      | 布尔值为 false             |
| `satisfies(obj, predicate)`          | 对象满足自定义 Lambda 条件 |
| `compare(f1, f2, comparator)`        | 使用比较器比较结果为 0     |

---

### 2.9 身份标识校验

| 方法            | 描述                      |
| --------------- | ------------------------- |
| `idCard(str)`   | 中国大陆身份证号校验      |
| `bankCard(str)` | 银行卡号校验（Luhn 算法） |

---

## 3. 终结操作 (Terminal Operations)

| 方法                     | 适用模式    | 描述                               |
| ------------------------ | ----------- | ---------------------------------- |
| `fail()`                 | `begin()`   | 执行校验，若有错误则抛出第一个异常 |
| `failAll()`              | `strict()`  | 执行校验，若有错误则抛出聚合异常   |
| `failNow(code, message)` | `begin()`   | **强制立即失败**，无条件抛出异常   |
| `verify()`               | `with(ctx)` | Contextual 模式的语义终结符        |
| `getCauses()`            | 所有        | 获取当前链中已收集的所有错误对象   |
| `isValid()`              | 所有        | 返回当前链是否通过校验             |
| `onFail(runnable)`       | `begin()`   | 校验失败时执行的回调函数           |

### 终结方法对比

```java
// Fail-Fast 模式
Failure.begin()
    .notBlank(username, UserCode.REQUIRED)
    .fail();                    // 有错误时抛出第一个异常

// Fail-Strict 模式  
Failure.strict()
    .notBlank(username, UserCode.REQUIRED)
    .email(email, UserCode.INVALID)
    .failAll();                 // 收集所有错误后统一抛出

// Contextual 模式
Failure.with(ctx)
    .notBlank(username, UserCode.REQUIRED)
    .verify();                  // 不抛异常，错误写入 ctx

// 强制失败
Failure.begin()
    .notNull(user, UserCode.NOT_FOUND)
    .failNow(UserCode.FORBIDDEN, "无权访问");  // 直接抛出，无视前面结果
```

---

## 4. 最佳实践

### 4.1 错误码管理

建议使用枚举管理错误码，实现 `ResponseCode` 接口：

```java
public enum UserCode implements ResponseCode {
    USERNAME_REQUIRED(40001, "用户名不能为空"),
    EMAIL_INVALID(40002, "邮箱格式不正确"),
    AGE_TOO_YOUNG(40003, "年龄必须 ≥ 18 岁");

    private final int code;
    private final String message;

    UserCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() { return code; }

    @Override
    public String getMessage() { return message; }

    @Override
    public String getDescription() { return message; }
}
```

---

### 4.2 集中式校验 (TypedValidator)

将校验逻辑从 Controller/Service 中抽离到独立的 Validator 类：

```java
@Component
public class UserRegisterValidator implements FastValidator<UserRegisterDTO> {

    @Override
    public void validate(UserRegisterDTO dto, ValidationContext ctx) {
        Failure.with(ctx)
            .notBlank(dto.getUsername(), UserCode.USERNAME_REQUIRED)
            .lengthBetween(dto.getUsername(), 4, 20, UserCode.USERNAME_LENGTH)
            .email(dto.getEmail(), UserCode.EMAIL_INVALID)
            .min(dto.getAge(), 18, UserCode.AGE_TOO_YOUNG)
            .verify();
    }

    @Override
    public Class<?> getSupportedType() {
        return UserRegisterDTO.class;
    }
}
```

Controller 中使用：

```java
@PostMapping("/register")
@Validate(value = UserRegisterValidator.class, fast = false)
public Result<?> register(@RequestBody UserRegisterDTO dto) {
    // AOP 自动执行校验
    userService.register(dto);
    return Result.success("注册成功");
}
```

---

### 4.3 复杂校验逻辑

对于需要依赖注入的复杂校验，使用 `FastValidator`：

```java
@Component
public class UserLoginValidator implements FastValidator<UserLoginDTO> {
    
    @Resource
    private UserService userService;

    @Override
    public void validate(UserLoginDTO dto, ValidationContext ctx) {
        // 基础格式校验
        Failure.with(ctx)
            .notBlank(dto.getEmail(), UserCode.EMAIL_BLANK)
            .notBlank(dto.getPassword(), UserCode.PASSWORD_BLANK)
            .email(dto.getEmail(), UserCode.EMAIL_INVALID)
            .verify();

        // 如果基础校验失败，提前返回
        if (ctx.isFailed()) return;

        // 业务逻辑校验
        User user = userService.findByEmail(dto.getEmail());
        Failure.with(ctx)
            .state(user != null, UserCode.USER_NOT_FOUND)
            .verify();
    }

    @Override
    public Class<?> getSupportedType() {
        return UserLoginDTO.class;
    }
}
```

---

### 4.4 异常处理

框架提供 `DefaultExceptionHandler`，如需自定义可继承 `FailFastExceptionHandler`：

```java
@RestControllerAdvice
public class CustomExceptionHandler extends FailFastExceptionHandler {
    
    @Override
    @ExceptionHandler(Business.class)
    public ResponseEntity<?> handleBusinessException(Business e) {
        // 自定义响应格式
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("errorCode", e.getResponseCode().getCode());
        body.put("errorMessage", e.getResponseCode().getMessage());
        body.put("detail", e.getDetail());
        return ResponseEntity.badRequest().body(body);
    }
}
```

---

### 4.5 函数式结果处理

使用 `Result<T>` 进行函数式链式处理：

```java
public Result<UserDTO> getUser(Long id) {
    return Result.ofNullable(id, UserCode.ID_REQUIRED)
        .map(userService::findById)
        .filter(Objects::nonNull, UserCode.USER_NOT_FOUND)
        .map(userConverter::toDTO)
        .peek(dto -> log.info("获取用户: {}", dto.getUsername()))
        .recover(error -> {
            log.error("获取用户失败: {}", error.getMessage());
            return UserDTO.guest();
        });
}
```

**Result API**:

| 方法                                | 说明                    |
| ----------------------------------- | ----------------------- |
| `ok(value)`                         | 创建成功结果            |
| `fail(code)` / `fail(code, detail)` | 创建失败结果            |
| `ofNullable(value, code)`           | 根据 null 创建结果      |
| `map(fn)`                           | 映射成功值              |
| `flatMap(fn)`                       | 扁平映射                |
| `filter(predicate, code)`           | 过滤成功值              |
| `peek(consumer)`                    | 消费成功值              |
| `peekError(consumer)`               | 消费错误                |
| `recover(fn)`                       | 从错误恢复              |
| `recoverWith(fn)`                   | 从错误恢复为新的 Result |
| `failNow()` / `failNow(default)`    | 获取值或抛出/返回默认值 |
| `combine(other, combiner)`          | 组合两个 Result         |

---

## 5. 配置参考

### application.yml 完整配置

```yaml
fail-fast:
  # 调试配置
  shadow-trace: true        # 异常中包含校验点的类名与行号
  verbose: true             # 多错误响应包含详细 errors 列表
  
  # 错误码映射
  code-mapping:
    http-status:
      # 精确映射
      40001: 400
      40100: 401
      40300: 403
      40400: 404
      50000: 500
    groups:
      # 范围映射（支持 40100..40199 语法）
      auth: ["40100..40199", "40300..40399"]
      business: ["40000..40099"]
      system: ["50000..59999"]
```

---

**更多示例**: [Failure-in-Action](https://github.com/KyrieChao/Failure-in-Action)