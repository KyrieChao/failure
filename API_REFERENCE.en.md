# API Reference

[中文版本](./API_REFERENCE.md)

This document provides a comprehensive list of core APIs, design patterns, and best practices for the `Failure` validation framework. Designed with a fluent-interface style, it aims to provide a high-readability, type-safe, and easily extensible validation experience.

---

## 1. Design Patterns and Core Entry Points

The framework supports three core validation modes, suitable for different business scenarios:

### 1.1 Fail-Fast (Immediate Failure Mode)
**Scenario**: Defensive programming for parameters. Stops subsequent logic immediately upon finding an invalid parameter to avoid resource waste.

```java
// Throws exception immediately if notBlank fails, subsequent checks will not be executed
Failure.begin()
    .notBlank(username, UserCode.USERNAME_REQUIRED)
    .email(email, UserCode.EMAIL_INVALID)
    .fail(); // Terminal operation, throws the first encountered exception
```

### 1.2 Fail-Strict (Collect All Mode)
**Scenario**: Batch import, form submission, etc., where all errors need to be returned at once.

```java
// Even if username is blank, it continues to check email, collecting all errors
Failure.strict()
    .notBlank(username, UserCode.USERNAME_REQUIRED)
    .email(email, UserCode.EMAIL_INVALID)
    .failAll(); // Terminal operation, throws MultiBusiness exception containing all errors if any exist
```

### 1.3 Contextual (Context Integration Mode)
**Scenario**: Used with `TypedValidator` or `ValidationContext` to decouple validation logic from business logic. Refer to [Failure-in-Action](https://github.com/KyrieChao/Failure-in-Action) best practices.

```java
// Errors are reported directly to ctx, not thrown immediately (depends on ctx config)
// Supports complex business flow control
Failure.with(ctx)
    .notBlank(dto.getUsername(), UserCode.USERNAME_BLANK)
    .email(dto.getEmail(), UserCode.EMAIL_INVALID)
    .verify(); // Semantic terminal, actual errors are written to context in real-time
```

---

## 2. Validation Methods Detailed

All validation methods support the following four overload forms (using `notNull` as an example):
1. `notNull(obj)` - Use default error message
2. `notNull(obj, code)` - Specify `ResponseCode`
3. `notNull(obj, code, detail)` - Specify `ResponseCode` and detailed description
4. `notNull(Object obj, Consumer<Business.Fabricator> consumer)` - Use Lambda to build complex error messages

### 2.1 General Object Validation (Object)

| Method | Description |
|:---|:---|
| `notNull(obj)` / `exists` | Verify object is not null |
| `isNull(obj)` | Verify object must be null |
| `instanceOf(obj, type)` | Verify object is an instance of specified type |
| `notInstanceOf(obj, type)` | Verify object is not an instance of specified type |
| `allNotNull(objs...)` | Verify a group of objects are all not null |
| `equals(obj1, obj2)` | Verify objects are equal (`Object.equals`) |
| `notEquals(obj1, obj2)` | Verify objects are not equal |
| `same(obj1, obj2)` | Verify references are the same (`==`) |
| `notSame(obj1, obj2)` | Verify references are different (`!=`) |

### 2.2 String Validation (String)

| Method | Description |
|:---|:---|
| `notBlank(str)` / `notEmpty` | Not null and length > 0 after trimming whitespace |
| `blank(str)` | Null, empty string, or contains only whitespace characters |
| `lengthBetween(str, min, max)` | Character length is between [min, max] |
| `lengthMin(str, min)` | Minimum length limit |
| `lengthMax(str, max)` | Maximum length limit |
| `match(str, regex)` | Matches regular expression |
| `email(str)` | Email format validation |
| `mobile(str)` | Mobile phone number format validation (Mainland China) |
| `url(str)` | URL format validation |
| `ipAddress(str)` | IPv4 address format validation |
| `uuid(str)` | UUID format validation |
| `isNumeric(str)` | Contains only numbers |
| `isAlpha(str)` | Contains only letters |
| `isAlphanumeric(str)` | Contains only letters or numbers |
| `contains(str, sub)` | Contains substring |
| `startsWith(str, prefix)` | Starts with specified prefix |
| `endsWith(str, suffix)` | Ends with specified suffix |

### 2.3 Number Validation (Number)

Supports `Integer`, `Long`, `Double`, `BigDecimal` and all `Number` subclasses.

| Method | Description |
|:---|:---|
| `positive(num)` | Positive number (> 0) |
| `nonNegative(num)` | Non-negative number (>= 0) |
| `negative(num)` | Negative number (< 0) |
| `notZero(num)` | Not zero (!= 0) |
| `isZero(num)` | Equal to zero (== 0) |
| `greaterThan(val, threshold)` | Greater than (>) |
| `greaterOrEqual(val, threshold)` | Greater than or equal (>=) |
| `lessThan(val, threshold)` | Less than (<) |
| `lessOrEqual(val, threshold)` | Less than or equal (<=) |
| `inRange(val, min, max)` | Within range [min, max] (Comparable) |
| `multipleOf(val, divisor)` | Is a multiple of divisor |
| `decimalScale(decimal, scale)` | BigDecimal scale/precision check |

### 2.4 Collection and Container Validation (Collection/Map/Array)

| Method | Description |
|:---|:---|
| `notEmpty(container)` | Container is not null and contains elements |
| `isEmpty(container)` | Container is null or has no elements |
| `sizeBetween(container, min, max)` | Number of elements is within range |
| `sizeEquals(container, size)` | Number of elements strictly equals size |
| `contains(container, element)` | Contains specified element |
| `notContains(container, element)` | Does not contain specified element |
| `hasNoNullElements(container)` | Container does not contain null elements |
| `allMatch(col, predicate)` | All elements satisfy the condition |
| `anyMatch(col, predicate)` | Any element satisfies the condition |
| `containsKey(map, key)` | Map contains specified Key |
| `containsValue(map, value)` | Map contains specified Value |

### 2.5 Date/Time Validation (Date/Time)

Supports `Date`, `LocalDate`, `LocalDateTime`, `Instant`, `ZonedDateTime`.

| Method | Description |
|:---|:---|
| `isPast(date)` | Time is before the current moment |
| `isFuture(date)` | Time is after the current moment |
| `isToday(date)` | Date is today |
| `after(d1, d2)` | d1 is later than d2 |
| `before(d1, d2)` | d1 is earlier than d2 |
| `between(date, start, end)` | Time is within range [start, end] |

### 2.6 Optional and Special Types

| Method | Description |
|:---|:---|
| `isPresent(opt)` | Optional contains a value |
| `isEmpty(opt)` | Optional is empty |
| `enumValue(class, val)` | String is a valid enum name |
| `enumConstant(enum, class)` | Enum value belongs to specified type |

### 2.7 Custom and Logic Validation

| Method | Description |
|:---|:---|
| `isTrue(bool)` / `state` | Boolean value is true |
| `isFalse(bool)` | Boolean value is false |
| `satisfies(obj, predicate)` | Object satisfies custom Lambda condition |
| `compare(f1, f2, comparator)` | Result of comparator is 0 |

---

## 3. Terminal Operations

| Method | Applicable Mode | Description |
|:---|:---|:---|
| `fail()` | `begin()` | Executes validation, throws first exception (BusinessException) if error exists |
| `failAll()` | `strict()` | Executes validation, throws aggregated exception (MultiBusinessException) if errors exist |
| `verify()` | `with(ctx)` | Semantic terminal. In Context mode errors are reported in real-time, this method is used to close the chain call |
| `getCauses()` | All | Gets all error objects collected in the current chain |
| `isValid()` | All | Returns whether the current chain passed validation (true/false) |
| `onFail(runnable)` | All | Callback function executed when validation fails |

---

## 4. Best Practices

### 4.1 Centralized Validation (TypedValidator)
Referencing the design of [Failure-in-Action](https://github.com/KyrieChao/Failure-in-Action), it is recommended to extract validation logic from Controller/Service into independent Validator classes.

```java
// Recommended: Register rules in Validator
register(UserRegisterDTO.class, (dto, ctx) -> {
    Failure.with(ctx)
        .notBlank(dto.getUsername(), UserCode.USERNAME_BLANK)
        .email(dto.getEmail(), UserCode.EMAIL_INVALID)
        .verify();
});
```

### 4.2 Error Code Management
It is recommended to use Enums (e.g., `UserCode`) to manage error codes instead of hardcoded strings. Error codes should implement the `ResponseCode` interface.

### 4.3 Exception Handling
Cooperate with a Global Exception Handler to capture `BusinessException` and implement a unified API response format.

---

## 5. Version History

**v1.2.1 (Doc Updated)**
- **New Design Patterns Section**: Clearly distinguished Fail-Fast, Fail-Strict, and Contextual modes.
- **Best Practices**: Adopted `TypedValidator` pattern from `Failure-in-Action` as a recommended practice.
- **API List Update**:
  - Added `verify()` method description (Context mode specific).
  - Added `isPast`/`isFuture` and other time validation methods.
  - Added `Optional` and `Enum` validation support.
- **Structure Optimization**: Reclassified validation methods by data type to improve readability.
- **Differential Adjustment**: Retained original table format but enhanced description of `Failure.with(ctx)` context mode to align with the latest practical usage.
