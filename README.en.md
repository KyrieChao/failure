# Fail-Fast Spring Boot Starter

[![Java CI with Maven](https://github.com/KyrieChao/Failure/actions/workflows/ci.yml/badge.svg)](https://github.com/KyrieChao/Failure/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/KyrieChao/Failure/branch/main/graph/badge.svg)](https://codecov.io/gh/KyrieChao/Failure)
[![Release](https://jitpack.io/v/KyrieChao/Failure.svg)](https://jitpack.io/#KyrieChao/Failure)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

[中文版本](./README.md)

### Overview

Fail‑Fast is a lightweight and high‑performance validation and business‑exception framework for Spring Boot 3.x. It embraces the “Fail Fast, Fail Safe” philosophy and provides:
- Fluent validation chain with fail‑fast or fail‑safe modes
- Annotation‑driven validation via `@Validate` and custom `FastValidator`
- Functional result type `Result<T>` with utility `Results`
- Jakarta Bean Validation integration
- Flexible mapping from business codes to HTTP status, plus shadow tracing

This implementation covers the full stack: entry [Failure](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/Failure.java), validation chain [Chain](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/internal/Chain.java), exception model [Business](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/internal/Business.java), result types [Result](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/result/Result.java) and [Results](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/result/Results.java), annotations/aspect [Validate](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/annotation/Validate.java) / [FastValidator](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/annotation/FastValidator.java) / [ValidationAspect](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/aspect/ValidationAspect.java), and auto‑configuration/exception handling [FailFastAutoConfiguration](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/config/FailFastAutoConfiguration.java) / [CodeMappingConfig](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/config/CodeMappingConfig.java) / [DefaultExceptionHandler](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/advice/DefaultExceptionHandler.java).

### Quick Start

- Java 17+
- Spring Boot 3.2.x
- See JitPack badge mutate pipeline or Releases for latest version

Add JitPack repository and dependency:

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
    <version>Tag</version> <!-- e.g. 1.2.1 -->
</dependency>
```

Hands‑on demo: https://github.com/KyrieChao/Failure-in-Action

### Core Features

- Fluent checks for Object/String/Collection/Array/Number/Date‑Time/Enum/Identity/Boolean/Map/Optional
- Custom predicates via `satisfies` and cross‑field `compare`
- Fail‑fast or fail‑safe with terminal ops `fail()` / `failAll()`
- Annotation‑driven validation with `@Validate` and pluggable `FastValidator`
- Functional results: `Result.ok/fail/ofNullable`, `map/flatMap/filter/recover/combine`; utilities in `Results`
- Robust exception handling with verbose multi‑error responses
- Configurable mapping from business codes to HTTP statuses

### API Summary

- Entry
  - Failure: `begin()`, `strict()`, `with(ValidationContext)`

- Terminal/Control
  - Chain: `fail()`, `failAll()`, `onFail(...)`, `onFailGet(...)`, `failNow(...)`, `verify()`

- Customization
  - `satisfies(value, predicate, ...)`, `compare(field1, field2, comparator, ...)`

- Objects/Identity/Boolean
  - Objects: `exists/notNull/isNull/instanceOf/notInstanceOf/allNotNull`
  - Identity: `same/notSame/equals/notEquals`
  - Boolean: `state/isTrue/isFalse`

- Strings
  - `blank/notBlank/notEmpty/lengthBetween/lengthMin/lengthMax`
  - `match/email/startsWith/endsWith/contains/notContains`
  - `isNumeric/isAlpha/isAlphanumeric/isLowerCase/isUpperCase`
  - `mobile/url/ipAddress/uuid`

- Collections/Arrays/Map/Optional
  - Collections: `notEmpty/sizeBetween/sizeEquals/contains/notContains/isEmpty/hasNoNullElements/allMatch/anyMatch`
  - Arrays: same semantics as collections
  - Map: `notEmpty/isEmpty/containsKey/notContainsKey/containsValue/sizeBetween/sizeEquals`
  - Optional: `isPresent/isEmpty`

- Numbers
  - `positive/nonNegative/greaterThan/greaterOrEqual/lessThan/lessOrEqual`
  - `inRange/inRangeNumber/notZero/isZero/negative/multipleOf/decimalScale`

- Date/Time (Date, Instant, LocalDate, LocalDateTime, ZonedDateTime)
  - `after/before/afterOrEqual/beforeOrEqual/between/isPast/isFuture/isToday`

- Annotation‑Driven
  - `@Validate(value = { validators... }, fast = true|false)`
  - `FastValidator<T>.validate(T, ValidationContext)`; context offers `reportError/stop/isFailed`

- Functional Result
  - Result: `ok/fail/ofNullable`, `map/flatMap/peek/peekError/filter/recover/recoverWith/combine`
  - Results: `tryOf/tryRun/sequence/sequenceAll/traverse/getOrNull/when`

- Bean Validation Integration
  - ValidationAdapter: `validate`, `validateAll`, `validateToList`, `isValid`

See sources for full signatures and examples: [Chain](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/internal/Chain.java), [Result](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/result/Result.java), [Results](file:///d:/Work/WorkIDEA/SpringBoot/mvn/fail-fast-improved/src/main/java/com/chao/failfast/result/Results.java).

### Configuration

```yaml
fail-fast:
  shadow-trace: true
  verbose: true
  code-mapping:
    http-status:
      40001: 400
      40100: 401
    groups:
      auth: [ "40100..40199" ]
      business: [ "40000..40099", 42200 ]
```

Priority: Standard HTTP code → exact mapping → range prefix → family fallback.

### Example Usage

```java
Failure.strict()
    .lengthBetween(username, 4, 20, ResponseCode.of(40002, "Invalid username length"))
    .email(email, ResponseCode.of(40004, "Invalid email"))
    .failAll(); // collect and throw MultiBusiness if any
```

```java
Result<UserDTO> r = Results.tryOf(() -> service.register(cmd), ResponseCode.of(50000, "Internal Error"));
return r.map(UserMapper::toDTO).failNow();
```

### Contributing

- Fork, create a feature branch
- Run `mvn test` before PR
- Add tests for new code and follow current style
- Open a Pull Request and link related issues

### License

Apache License 2.0. See LICENSE.

### Authors & Contact

- Author: KyrieChao
- GitHub: https://github.com/KyrieChao
- Issues: https://github.com/KyrieChao/Failure/issues

— End —
