# API Reference

[English Version](./API_REFERENCE.en.md)

本文档详尽列出了 `Failure` 校验框架的核心 API、设计模式及最佳实践。基于 fluent-interface 风格设计，旨在提供高可读性、强类型安全且易于扩展的校验体验。

---

## 1. 设计模式与核心入口

框架支持三种核心校验模式，分别适用于不同的业务场景：

### 1.1 Fail-Fast (快速失败模式)
**适用场景**：参数防御性编程，一旦发现非法参数立即停止后续逻辑，避免资源浪费。

```java
// 一旦 notBlank 失败，立即抛出异常，不会执行 subsequent checks
Failure.begin()
    .notBlank(username, UserCode.USERNAME_REQUIRED)
    .email(email, UserCode.EMAIL_INVALID)
    .fail(); // 终结操作，抛出第一个遇到的异常
```

### 1.2 Fail-Strict (全量收集模式)
**适用场景**：批量导入、表单提交等需要一次性返回所有错误的场景。

```java
// 即使 username 为空，也会继续检查 email，最终收集所有错误
Failure.strict()
    .notBlank(username, UserCode.USERNAME_REQUIRED)
    .email(email, UserCode.EMAIL_INVALID)
    .failAll(); // 终结操作，若有错误则抛出包含所有错误的 MultiBusiness 异常
```

### 1.3 Contextual (上下文集成模式)
**适用场景**：结合 `TypedValidator` 或 `ValidationContext` 使用，将校验逻辑与业务逻辑解耦。参考 [Failure-in-Action](https://github.com/KyrieChao/Failure-in-Action) 最佳实践。

```java
// 错误直接报告给 ctx，不立即抛出（取决于 ctx 配置），支持复杂的业务流控
Failure.with(ctx)
    .notBlank(dto.getUsername(), UserCode.USERNAME_BLANK)
    .email(dto.getEmail(), UserCode.EMAIL_INVALID)
    .verify(); // 语义上的终结，实际错误已实时写入 context
```

---

## 2. 校验方法详解

所有校验方法均支持以下四种重载形式（以 `notNull` 为例）：
1. `notNull(obj)` - 使用默认错误信息
2. `notNull(obj, code)` - 指定 `ResponseCode`
3. `notNull(obj, code, detail)` - 指定 `ResponseCode` 和详细描述
4. `notNull(Object obj, Consumer<Business.Fabricator> consumer)` - 使用 Lambda 构建复杂错误信息

### 2.1 通用对象校验 (Object)

| 方法 | 描述 |
|:---|:---|
| `notNull(obj)` / `exists` | 验证对象不为 null |
| `isNull(obj)` | 验证对象必须为 null |
| `instanceOf(obj, type)` | 验证对象是指定类型的实例 |
| `notInstanceOf(obj, type)` | 验证对象不是指定类型的实例 |
| `allNotNull(objs...)` | 验证一组对象均不为 null |
| `equals(obj1, obj2)` | 验证对象相等 (`Object.equals`) |
| `notEquals(obj1, obj2)` | 验证对象不等 |
| `same(obj1, obj2)` | 验证引用相同 (`==`) |
| `notSame(obj1, obj2)` | 验证引用不同 (`!=`) |

### 2.2 字符串校验 (String)

| 方法 | 描述 |
|:---|:---|
| `notBlank(str)` / `notEmpty` | 非 null 且去除首尾空格后长度 > 0 |
| `blank(str)` | 为 null、空串或仅包含空白字符 |
| `lengthBetween(str, min, max)` | 字符长度在 [min, max] 之间 |
| `lengthMin(str, min)` | 最小长度限制 |
| `lengthMax(str, max)` | 最大长度限制 |
| `match(str, regex)` | 符合正则表达式 |
| `email(str)` | 邮箱格式校验 |
| `mobile(str)` | 中国大陆手机号格式校验 |
| `url(str)` | URL 格式校验 |
| `ipAddress(str)` | IPv4 地址格式校验 |
| `uuid(str)` | UUID 格式校验 |
| `isNumeric(str)` | 仅包含数字 |
| `isAlpha(str)` | 仅包含字母 |
| `isAlphanumeric(str)` | 仅包含字母或数字 |
| `contains(str, sub)` | 包含子串 |
| `startsWith(str, prefix)` | 以指定前缀开头 |
| `endsWith(str, suffix)` | 以指定后缀结尾 |

### 2.3 数值校验 (Number)

支持 `Integer`, `Long`, `Double`, `BigDecimal` 等所有 `Number` 子类。

| 方法 | 描述 |
|:---|:---|
| `positive(num)` | 正数 (> 0) |
| `nonNegative(num)` | 非负数 (>= 0) |
| `negative(num)` | 负数 (< 0) |
| `notZero(num)` | 非零 (!= 0) |
| `isZero(num)` | 等于零 (== 0) |
| `greaterThan(val, threshold)` | 大于 (>) |
| `greaterOrEqual(val, threshold)` | 大于等于 (>=) |
| `lessThan(val, threshold)` | 小于 (<) |
| `lessOrEqual(val, threshold)` | 小于等于 (<=) |
| `inRange(val, min, max)` | 在区间 [min, max] 内 (Comparable) |
| `multipleOf(val, divisor)` | 是 divisor 的倍数 |
| `decimalScale(decimal, scale)` | BigDecimal 小数位精度检查 |

### 2.4 集合与容器校验 (Collection/Map/Array)

| 方法 | 描述 |
|:---|:---|
| `notEmpty(container)` | 容器非 null 且包含元素 |
| `isEmpty(container)` | 容器为 null 或无元素 |
| `sizeBetween(container, min, max)` | 元素数量在范围内 |
| `sizeEquals(container, size)` | 元素数量严格相等 |
| `contains(container, element)` | 包含指定元素 |
| `notContains(container, element)` | 不包含指定元素 |
| `hasNoNullElements(container)` | 容器内不包含 null 元素 |
| `allMatch(col, predicate)` | 所有元素均满足条件 |
| `anyMatch(col, predicate)` | 任一元素满足条件 |
| `containsKey(map, key)` | Map 包含指定 Key |
| `containsValue(map, value)` | Map 包含指定 Value |

### 2.5 日期时间校验 (Date/Time)

支持 `Date`, `LocalDate`, `LocalDateTime`, `Instant`, `ZonedDateTime`。

| 方法 | 描述 |
|:---|:---|
| `isPast(date)` | 时间在当前时刻之前 |
| `isFuture(date)` | 时间在当前时刻之后 |
| `isToday(date)` | 日期是今天 |
| `after(d1, d2)` | d1 晚于 d2 |
| `before(d1, d2)` | d1 早于 d2 |
| `between(date, start, end)` | 时间在区间 [start, end] 内 |

### 2.6 Optional 与特殊类型

| 方法 | 描述 |
|:---|:---|
| `isPresent(opt)` | Optional 包含值 |
| `isEmpty(opt)` | Optional 为空 |
| `enumValue(class, val)` | 字符串是有效的枚举名称 |
| `enumConstant(enum, class)` | 枚举值属于指定类型 |

### 2.7 自定义与逻辑校验

| 方法 | 描述 |
|:---|:---|
| `isTrue(bool)` / `state` | 布尔值为 true |
| `isFalse(bool)` | 布尔值为 false |
| `satisfies(obj, predicate)` | 对象满足自定义 Lambda 条件 |
| `compare(f1, f2, comparator)` | 使用比较器比较结果为 0 |

---

## 3. 终结操作 (Terminal Operations)

| 方法 | 适用模式 | 描述 |
|:---|:---|:---|
| `fail()` | `begin()` | 执行校验，若有错误则抛出第一个异常 (BusinessException) |
| `failAll()` | `strict()` | 执行校验，若有错误则抛出聚合异常 (MultiBusinessException) |
| `verify()` | `with(ctx)` | 语义终结符。在 Context 模式下错误已实时上报，此方法用于闭合链式调用 |
| `getCauses()` | 所有 | 获取当前链中已收集的所有错误对象 |
| `isValid()` | 所有 | 返回当前链是否通过校验 (true/false) |
| `onFail(runnable)` | 所有 | 校验失败时执行的回调函数 |

---

## 4. 最佳实践 (Best Practices)

### 4.1 集中式校验 (TypedValidator)
参考 [Failure-in-Action](https://github.com/KyrieChao/Failure-in-Action) 的设计，建议将校验逻辑从 Controller/Service 中抽离到独立的 Validator 类中。

```java
// 推荐做法：在 Validator 中注册规则
register(UserRegisterDTO.class, (dto, ctx) -> {
    Failure.with(ctx)
        .notBlank(dto.getUsername(), UserCode.USERNAME_BLANK)
        .email(dto.getEmail(), UserCode.EMAIL_INVALID)
        .verify();
});
```

### 4.2 错误码管理
建议使用枚举（如 `UserCode`）管理错误码，而不是硬编码字符串。错误码应实现 `ResponseCode` 接口。

### 4.3 异常处理
配合全局异常处理器（Global Exception Handler）捕获 `BusinessException`，实现统一的 API 响应格式。

---

## 5. 版本变更记录

**v1.2.1 (Doc Updated)**
- **新增设计模式章节**：明确区分 Fail-Fast、Fail-Strict 和 Contextual 三种模式的用法。
- **引入最佳实践**：采纳 `Failure-in-Action` 中的 `TypedValidator` 模式作为推荐实践。
- **API 列表更新**：
  - 补充 `verify()` 方法说明 (Context 模式专用)。
  - 补充 `isPast`/`isFuture` 等时间校验方法。
  - 补充 `Optional` 及 `Enum` 校验支持。
- **结构优化**：按数据类型对校验方法进行重新分类，提升查阅效率。
- **差异化调整**：保留了原文档的表格形式，但增强了对 `Failure.with(ctx)` 上下文模式的描述，以对齐最新实战项目的用法。
