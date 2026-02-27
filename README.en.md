# Fail-Fast Spring Boot Starter

[![Java CI with Maven](https://github.com/KyrieChao/Failure/actions/workflows/ci.yml/badge.svg)](https://github.com/KyrieChao/Failure/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/KyrieChao/Failure/branch/main/graph/badge.svg)](https://codecov.io/gh/KyrieChao/Failure)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![Java 17+](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/technologies/downloads/)
[![Spring Boot 3](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Release](https://jitpack.io/v/KyrieChao/Failure.svg)](https://jitpack.io/#KyrieChao/Failure)

[中文版本](./README.md)

Fail-Fast is a lightweight, high-performance validation and business-exception framework designed for Spring Boot 3.x. Following the "Fail Fast, Fail Safe" philosophy, it eliminates boilerplate code and provides a type-safe, fluent validation experience.

🔗 **Live Demo**: [Failure-in-Action](https://github.com/KyrieChao/Failure-in-Action)

---

## 🚀 Core Features

- **Fluent Validation Chain**: Supports both `Fail-Fast` (immediate failure) and `Fail-Safe` (collect all errors) modes.
- **Rich Assertions**: Built-in 50+ validation methods for Objects, Strings, Numbers, Collections, Date/Time, Enums, Optionals, etc.
- **Context Integration**: Supports `TypedValidator` pattern to decouple validation logic from business logic.
- **Annotation-Driven**: Provides `@Validate` annotation and `FastValidator` interface for AOP-based validation.
- **Functional Results**: Provides `Result<T>` monad with `map`, `flatMap`, `recover` operations.
- **Smart Exception Handling**: Automatically maps business error codes to HTTP status codes, with `shadow-trace` for quick debugging.

## 📚 API Reference

**For detailed API documentation, method signatures, and usage examples, please refer to the complete reference document:**

👉 **[API_REFERENCE.md](./API_REFERENCE.md)** 👈

This document includes:
- Detailed explanation of Fail-Fast / Fail-Safe / Contextual modes
- Complete list of validation methods (String, Number, Date, Collection, etc.)
- Best practices and design patterns

---

## 🛠️ Quick Start

### 1. Requirements
- JDK 17+
- Spring Boot 3.2.x+

### 2. Dependency
This project is published on JitPack. Add the repository and dependency to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.KyrieChao</groupId>
    <artifactId>Failure</artifactId>
    <version>Tag</version> <!-- Replace with the latest Release version, e.g., 1.2.1 -->
</dependency>
```

### 3. Basic Usage

**Scenario 1: Fail-Fast**
```java
// Throws exception immediately if notBlank fails
Failure.begin()
    .notBlank(username, UserCode.USERNAME_REQUIRED)
    .email(email, UserCode.EMAIL_INVALID)
    .fail();
```

**Scenario 2: Fail-Safe (Collect All)**
```java
// Collects all errors and throws them together
Failure.strict()
    .notBlank(username, UserCode.USERNAME_REQUIRED)
    .email(email, UserCode.EMAIL_INVALID)
    .failAll();
```

For more advanced usage (like Context Integration, Custom Assertions), please refer to [API_REFERENCE.md](./API_REFERENCE.md).

---

## ⚙️ Configuration

Configure framework behavior in `application.yml`:

```yaml
fail-fast:
  shadow-trace: true   # Include class name and line number of the validation point in exception stack trace
  verbose: true        # Include detailed errors list in multi-error response (Fail-Safe)
  code-mapping:
    http-status:
      40001: 400       # Exact mapping: Error Code 40001 -> HTTP 400
      40100: 401
    groups:
      auth: [ "40100..40199" ]        # Range mapping: 401xx -> Default mapping rule
      business: [ "40000..40099" ]
```

## 🤝 Contributing

Issues and Pull Requests are welcome! Please run `mvn test` before submitting and follow the existing code style.

## 📄 License

Apache License 2.0 - See [LICENSE](LICENSE) for details.

---
**Author**: [KyrieChao](https://github.com/KyrieChao)
