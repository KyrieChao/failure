# API Reference

[中文版本](./API_REFERENCE.md)

This document provides a comprehensive list of core APIs, design patterns, and best practices for the `Failure` validation framework. Designed with a fluent-interface style, it aims to provide a high-readability, type-safe, and easily extensible validation experience.

---

## Table of Contents

1. [Design Patterns and Core Entry Points](#1-design-patterns-and-core-entry-points)
2. [Validation Methods Detailed](#2-validation-methods-detailed)
3. [Terminal Operations](#3-terminal-operations)
4. [Best Practices](#4-best-practices)

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
    .fail();
```

**Features**:
- Stops immediately on the first error
- Throws `BusinessException`
- Best performance

---

### 1.2 Fail-Strict (Collect All Mode)

**Scenario**: Batch import, form submission, etc., where all errors need to be returned at once.

```java
// Even if username is blank, it continues to check email, collecting all errors
Failure.strict()
    .notBlank(username, UserCode.USERNAME_REQUIRED)
    .email(email, UserCode.EMAIL_INVALID)
    .failAll();
```

**Features**:
- Executes all validation rules
- Throws `MultiBusinessException` (contains all errors)
- Suitable for frontend forms displaying all errors at once

---

### 1.3 Contextual (Context Integration Mode)

**Scenario**: Used with `TypedValidator` or `FastValidator` to decouple validation logic from business logic.

```java
// Errors are reported directly to ctx, not thrown immediately
Failure.with(ctx)
    .notBlank(dto.getUsername(), UserCode.USERNAME_BLANK)
    .email(dto.getEmail(), UserCode.EMAIL_INVALID)
    .verify();
```

**Features**:
- Does not throw exceptions during validation
- Errors are written to `ValidationContext`
- Caller decides subsequent processing

---

## 2. Validation Methods Detailed

All validation methods support the following four overload forms (using `notNull` as an example):

1. `notNull(obj)` - Use default error message
2. `notNull(obj, code)` - Specify `ResponseCode`
3. `notNull(obj, code, detail)` - Specify `ResponseCode` and detailed description
4. `notNull(obj, Consumer<Business.Fabricator>)` - Use Lambda to build complex error

```java
// Example: Four overload forms
Failure.begin()
    .notNull(obj)                                    // Form 1
    .notNull(obj, UserCode.REQUIRED)                 // Form 2
    .notNull(obj, UserCode.REQUIRED, "Cannot be null") // Form 3
    .notNull(obj, f -> f.responseCode(UserCode.REQUIRED).detail("Custom detail"))  // Form 4
    .fail();
```

---

### 2.1 General Object Validation (Object)

| Method | Description |
| :--- | :--- |
| `notNull(obj)` / `exists(obj)` | Verify object is not null |
| `isNull(obj)` | Verify object must be null |
| `instanceOf(obj, type)` | Verify object is an instance of specified type |
| `notInstanceOf(obj, type)` | Verify object is not an instance of specified type |
| `allNotNull(objs...)` | Verify a group of objects are all not null |
| `equals(obj1, obj2)` | Verify objects are equal (`Object.equals`) |
| `notEquals(obj1, obj2)` | Verify objects are not equal |
| `same(obj1, obj2)` | Verify references are the same (`==`) |
| `notSame(obj1, obj2)` | Verify references are different (`!=`) |

---

### 2.2 String Validation (String)

| Method | Description |
| :--- | :--- |
| `notBlank(str)` / `notEmpty(str)` | Not null and length > 0 after trimming whitespace |
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
| `isLowerCase(str)` | All characters are lower case |
| `isUpperCase(str)` | All characters are upper case |
| `contains(str, sub)` | Contains substring |
| `notContains(str, sub)` | Does not contain substring |
| `startsWith(str, prefix)` | Starts with specified prefix |
| `endsWith(str, suffix)` | Ends with specified suffix |
| `equalsIgnoreCase(str1, str2)` | Equal ignoring case |

---

### 2.3 Number Validation (Number)

Supports `Integer`, `Long`, `Double`, `BigDecimal` and all `Number` subclasses.

| Method | Description |
| :--- | :--- |
| `positive(num)` | Positive number (> 0) |
| `nonNegative(num)` | Non-negative number (>= 0) |
| `negative(num)` | Negative number (< 0) |
| `notZero(num)` | Not zero (!= 0) |
| `isZero(num)` | Equal to zero (== 0) |
| `greaterThan(val, threshold)` | Greater than (>) |
| `greaterOrEqual(val, threshold)` | Greater than or equal (>=) |
| `lessThan(val, threshold)` | Less than (<) |
| `lessOrEqual(val, threshold)` | Less than or equal (<=) |
| `inRange(val, min, max)` | Within range [min, max] |
| `multipleOf(val, divisor)` | Is a multiple of divisor |
| `decimalScale(decimal, scale)` | BigDecimal scale/precision check |

---

### 2.4 Collection and Container Validation

#### Collection / List / Set

| Method | Description |
| :--- | :--- |
| `notEmpty(collection)` | Container is not null and contains elements |
| `isEmpty(collection)` | Container is null or has no elements |
| `sizeBetween(collection, min, max)` | Number of elements is within range |
| `sizeEquals(collection, size)` | Number of elements strictly equals size |
| `sizeMin(collection, min)` | Number of elements >= min |
| `sizeMax(collection, max)` | Number of elements <= max |
| `contains(collection, element)` | Contains specified element |
| `notContains(collection, element)` | Does not contain specified element |
| `hasNoNullElements(collection)` | Container does not contain null elements |
| `allMatch(collection, predicate)` | All elements satisfy the condition |
| `anyMatch(collection, predicate)` | Any element satisfies the condition |
| `noneMatch(collection, predicate)` | No element satisfies the condition |

#### Map

| Method | Description |
| :--- | :--- |
| `notEmpty(map)` | Map is not null and contains key-value pairs |
| `isEmpty(map)` | Map is null or has no key-value pairs |
| `containsKey(map, key)` | Map contains specified Key |
| `containsValue(map, value)` | Map contains specified Value |
| `sizeBetween(map, min, max)` | Number of key-value pairs is within range |

#### Array

| Method | Description |
| :--- | :--- |
| `notEmpty(array)` | Array is not null and length > 0 |
| `isEmpty(array)` | Array is null or length is 0 |
| `lengthBetween(array, min, max)` | Array length is within range |
| `contains(array, element)` | Array contains specified element |

---

### 2.5 Date/Time Validation (Date/Time)

Supports `Date`, `LocalDate`, `LocalDateTime`, `Instant`, `ZonedDateTime`.

| Method | Description |
| :--- | :--- |
| `isPast(date)` | Time is before the current moment |
| `isFuture(date)` | Time is after the current moment |
| `isToday(date)` | Date is today |
| `after(d1, d2)` | d1 is later than d2 |
| `before(d1, d2)` | d1 is earlier than d2 |
| `between(date, start, end)` | Time is within range [start, end] |
| `notBefore(date, boundary)` | Time is not before boundary |
| `notAfter(date, boundary)` | Time is not after boundary |

---

### 2.6 Optional Validation

| Method | Description |
| :--- | :--- |
| `isPresent(opt)` | Optional contains a value |
| `isEmpty(opt)` | Optional is empty |
| `ifPresent(opt, consumer)` | If value is present, execute consumer |

---

### 2.7 Enum Validation

| Method | Description |
| :--- | :--- |
| `enumValue(enumClass, value)` | String is a valid enum name |
| `enumConstant(value, enumClass)` | Enum value belongs to specified type |
| `enumIn(value, constants...)` | Enum value is in the specified constant list |

---

### 2.8 Boolean and State Validation

| Method | Description |
| :--- | :--- |
| `isTrue(bool)` / `state(bool, code)` | Boolean value is true |
| `isFalse(bool)` | Boolean value is false |
| `satisfies(obj, predicate)` | Object satisfies custom Lambda condition |
| `compare(f1, f2, comparator)` | Result of comparator is 0 |

---

### 2.9 Identity Validation

| Method | Description |
| :--- | :--- |
| `idCard(str)` | Mainland China ID card number validation |
| `bankCard(str)` | Bank card number validation (Luhn algorithm) |

---

## 3. Terminal Operations

| Method | Applicable Mode | Description |
| :--- | :--- | :--- |
| `fail()` | `begin()` | Executes validation, throws first exception if error exists |
| `failAll()` | `strict()` | Executes validation, throws aggregated exception if errors exist |
| `failNow(code, message)` | `begin()` | **Force Immediate Failure**, unconditionally throws exception |
| `verify()` | `with(ctx)` | Semantic terminal for Contextual mode |
| `getCauses()` | All | Gets all error objects collected in the current chain |
| `isValid()` | All | Returns whether the current chain passed validation |
| `onFail(runnable)` | `begin()` | Callback function executed when validation fails |

### Terminal Methods Comparison

```java
// Fail-Fast Mode
Failure.begin()
    .notBlank(username, UserCode.REQUIRED)
    .fail();                    // Throws first exception on error

// Fail-Strict Mode
Failure.strict()
    .notBlank(username, UserCode.REQUIRED)
    .email(email, UserCode.INVALID)
    .failAll();                 // Collects all errors and throws them together

// Contextual Mode
Failure.with(ctx)
    .notBlank(username, UserCode.REQUIRED)
    .verify();                  // No exception thrown, errors written to ctx

// Force Failure
Failure.begin()
    .notNull(user, UserCode.NOT_FOUND)
    .failNow(UserCode.FORBIDDEN, "Access Denied");  // Throws immediately, ignoring previous results
```

---

## 4. Best Practices

### 4.1 Error Code Management

It is recommended to use Enums to manage error codes, implementing the `ResponseCode` interface:

```java
public enum UserCode implements ResponseCode {
    USERNAME_REQUIRED(40001, "Username cannot be empty"),
    EMAIL_INVALID(40002, "Invalid email format"),
    AGE_TOO_YOUNG(40003, "Must be at least 18 years old");

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

### 4.2 Centralized Validation (TypedValidator)

Extract validation logic from Controller/Service into independent Validator classes:

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

Use in Controller:

```java
@PostMapping("/register")
@Validate(value = UserRegisterValidator.class, fast = false)
public Result<?> register(@RequestBody UserRegisterDTO dto) {
    // AOP automatically executes validation
    userService.register(dto);
    return Result.success("Registration successful");
}
```

---

### 4.3 Complex Validation Logic

For complex validations requiring dependency injection, use `FastValidator`:

```java
@Component
public class UserLoginValidator implements FastValidator<UserLoginDTO> {
    
    @Resource
    private UserService userService;

    @Override
    public void validate(UserLoginDTO dto, ValidationContext ctx) {
        // Basic format validation
        Failure.with(ctx)
            .notBlank(dto.getEmail(), UserCode.EMAIL_BLANK)
            .notBlank(dto.getPassword(), UserCode.PASSWORD_BLANK)
            .email(dto.getEmail(), UserCode.EMAIL_INVALID)
            .verify();

        // If basic validation fails, return early
        if (ctx.isFailed()) return;

        // Business logic validation
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

### 4.4 Exception Handling

The framework provides `DefaultExceptionHandler`. To customize, inherit `FailFastExceptionHandler`:

```java
@RestControllerAdvice
public class CustomExceptionHandler extends FailFastExceptionHandler {
    
    @Override
    @ExceptionHandler(Business.class)
    public ResponseEntity<?> handleBusinessException(Business e) {
        // Custom response format
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

### 4.5 Functional Result Processing

Use `Result<T>` for functional chain processing:

```java
public Result<UserDTO> getUser(Long id) {
    return Result.ofNullable(id, UserCode.ID_REQUIRED)
        .map(userService::findById)
        .filter(Objects::nonNull, UserCode.USER_NOT_FOUND)
        .map(userConverter::toDTO)
        .peek(dto -> log.info("Get user: {}", dto.getUsername()))
        .recover(error -> {
            log.error("Get user failed: {}", error.getMessage());
            return UserDTO.guest();
        });
}
```

**Result API**:

| Method | Description |
| :--- | :--- |
| `ok(value)` | Create success result |
| `fail(code)` / `fail(code, detail)` | Create failure result |
| `ofNullable(value, code)` | Create result from nullable value |
| `map(fn)` | Map success value |
| `flatMap(fn)` | Flat map |
| `filter(predicate, code)` | Filter success value |
| `peek(consumer)` | Consume success value |
| `peekError(consumer)` | Consume error |
| `recover(fn)` | Recover from error |
| `recoverWith(fn)` | Recover from error to new Result |
| `failNow()` / `failNow(default)` | Get value or throw/return default |
| `combine(other, combiner)` | Combine two Results |

---

## 5. Configuration Reference

### application.yml Complete Configuration

```yaml
fail-fast:
  # Debug configuration
  shadow-trace: true        # Include class name and line number of validation point in exception
  verbose: true             # Include detailed errors list in multi-error response
  
  # Error code mapping
  code-mapping:
    http-status:
      # Exact mapping
      40001: 400
      40100: 401
      40300: 403
      40400: 404
      50000: 500
    groups:
      # Range mapping (supports 40100..40199 syntax)
      auth: ["40100..40199", "40300..40399"]
      business: ["40000..40099"]
      system: ["50000..59999"]
```

---

**More Examples**: [Failure-in-Action](https://github.com/KyrieChao/Failure-in-Action)
