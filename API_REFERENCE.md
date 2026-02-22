# API Reference

本文档列出了 `Failure.begin()` / `Failure.strict()` 链式调用支持的所有校验方法。

所有方法均支持以下三种重载形式（以 `notNull` 为例）：

1. `notNull(obj)` - 使用默认错误码
2. `notNull(obj, code)` - 指定错误码
3. `notNull(obj, code, detail)` - 指定错误码和详细描述
4. `notNull(Object obj, Consumer<Business.Fabricator> consumer)` - 使用自定义错误构建器

---

## 1. 数校验 (Number/BigDecimal)

| 方法                                                | 作用                           |
|:--------------------------------------------------|:-----------------------------|
| `positive(Number value)`                          | 正数 (> 0)                     |
| `nonNegative(Number value)`                       | 非负数 (>= 0)                   |
| `greaterThan(T value, T threshold)`               | 大于 (>)                       |
| `greaterOrEqual(T value, T threshold)`            | 大于等于 (>=)                    |
| `lessThan(T value, T threshold)`                  | 小于 (<)                       |
| `lessOrEqual(T value, T threshold)`               | 小于等于 (<=)                    |
| `notZero(Number value)`                           | 非零 (!= 0)                    |
| `isZero(Number value)`                            | 等于零 (== 0)                   |
| `negative(Number value)`                          | 负数 (< 0)                     |
| `multipleOf(Number value, Number divisor)`        | 是否为某数的倍数                     |
| `decimalScale(BigDecimal value, int scale)`       | BigDecimal 精度检查              |
| `inRange(T value, T min, T max)`                  | 范围检查 [min, max] (Comparable) |
| `inRangeNumber(Number v, Number min, Number max)` | 范围检查 [min, max] (Number)     |

## 2. 字符串校验 (String)

| 方法                                            | 作用              |
|:----------------------------------------------|:----------------|
| `blank(String str)`                           | 为空或仅包含空白        |
| `notBlank(String str)` / `notEmpty`           | 非空且非空白          |
| `lengthBetween(String str, int min, int max)` | 长度范围 [min, max] |
| `lengthMin(String str, int min)`              | 最小长度 (>= min)   |
| `lengthMax(String str, int max)`              | 最大长度 (<= max)   |
| `match(String str, String regex)`             | 正则匹配            |
| `email(String email)`                         | 邮箱格式            |
| `mobile(String str)`                          | 手机号格式 (CN)      |
| `url(String str)`                             | URL 格式          |
| `ipAddress(String str)`                       | IPv4 地址格式       |
| `uuid(String str)`                            | UUID 格式         |
| `contains(String str, String substring)`      | 包含子串            |
| `notContains(String str, String substring)`   | 不包含子串           |
| `isNumeric(String str)`                       | 纯数字             |
| `isAlpha(String str)`                         | 纯字母             |
| `isAlphanumeric(String str)`                  | 字母或数字           |
| `isLowerCase(String str)`                     | 全小写             |
| `isUpperCase(String str)`                     | 全大写             |
| `startsWith(String str, String prefix)`       | 前缀匹配            |
| `endsWith(String str, String suffix)`         | 后缀匹配            |
| `equalsIgnoreCase(String str1, String str2)`  | 忽略大小写相等         |

## 3. 日期时间校验 (Java 8+ Time API)

支持 `Date`, `LocalDate`, `LocalDateTime`, `Instant`, `ZonedDateTime` 等。

| 方法                                 | 作用                |
|:-----------------------------------|:------------------|
| `after(T t1, T t2)`                | t1 晚于 t2 (>)      |
| `afterOrEqual(T t1, T t2)`         | t1 晚于或等于 t2 (>=)  |
| `before(T t1, T t2)`               | t1 早于 t2 (<)      |
| `beforeOrEqual(T t1, T t2)`        | t1 早于或等于 t2 (<=)  |
| `between(T value, T start, T end)` | 时间区间 [start, end] |
| `isPast(T value)`                  | 早于当前时间            |
| `isFuture(T value)`                | 晚于当前时间            |
| `isToday(LocalDate value)`         | 是否为今天             |

## 4. Map 校验

| 方法                                       | 作用         |
|:-----------------------------------------|:-----------|
| `notEmpty(Map map)`                      | 非 null 且非空 |
| `isEmpty(Map map)`                       | 为 null 或空  |
| `containsKey(Map map, Object key)`       | 包含指定 Key   |
| `notContainsKey(Map map, Object key)`    | 不包含指定 Key  |
| `containsValue(Map map, Object value)`   | 包含指定 Value |
| `sizeBetween(Map map, int min, int max)` | Map 大小范围   |
| `sizeEquals(Map map, int size)`          | Map 大小相等   |

## 5. 集合/数组增强 (Collection/Array)

| 方法                                        | 作用          |
|:------------------------------------------|:------------|
| `notEmpty(Collection/Array)`              | 非 null 且非空  |
| `isEmpty(Collection/Array)`               | 为 null 或空   |
| `sizeBetween(Collection/Array, min, max)` | 大小范围        |
| `sizeEquals(Collection/Array, size)`      | 大小相等        |
| `contains(Collection/Array, element)`     | 包含元素        |
| `notContains(Collection/Array, element)`  | 不包含元素       |
| `hasNoNullElements(Collection/Array)`     | 不包含 null 元素 |
| `allMatch(Collection/Array, Predicate)`   | 所有元素满足条件    |
| `anyMatch(Collection/Array, Predicate)`   | 任意元素满足条件    |

## 6. Optional 支持

| 方法                        | 作用           |
|:--------------------------|:-------------|
| `isPresent(Optional opt)` | 有值 (Present) |
| `isEmpty(Optional opt)`   | 为空 (Empty)   |

## 7. 对象/类型校验 (Object)

| 方法                                      | 作用             |
|:----------------------------------------|:---------------|
| `exists(Object obj)` / `notNull`        | 非 null         |
| `isNull(Object obj)`                    | 为 null         |
| `instanceOf(Object obj, Class type)`    | 是指定类型的实例       |
| `notInstanceOf(Object obj, Class type)` | 不是指定类型的实例      |
| `allNotNull(Object... objs)`            | 所有对象均非 null    |
| `same(Object obj1, Object obj2)`        | 引用相同 (==)      |
| `notSame(Object obj1, Object obj2)`     | 引用不同 (!=)      |
| `equals(Object obj1, Object obj2)`      | 对象相等 (equals)  |
| `notEquals(Object obj1, Object obj2)`   | 对象不等 (!equals) |

## 8. 枚举校验 (Enum)

| 方法                                        | 作用           |
|:------------------------------------------|:-------------|
| `enumValue(Class enumType, String value)` | 字符串是有效的枚举值名称 |
| `enumConstant(Enum value, Class type)`    | 枚举常量属于指定类型   |

## 9. 自定义条件

| 方法                                           | 作用                 |
|:---------------------------------------------|:-------------------|
| `satisfies(T value, Predicate<T> condition)` | 满足自定义 Predicate 条件 |

## 10. 跨字段/状态校验

| 方法                                          | 作用                        |
|:--------------------------------------------|:--------------------------|
| `state(boolean condition)` / `isTrue`       | 布尔状态为 True                |
| `isFalse(boolean condition)`                | 布尔状态为 False               |
| `compare(T field1, T field2, Comparator c)` | 使用比较器比较两个字段 (result == 0) |
