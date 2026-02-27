# Fail-Fast Spring Boot Starter

[![Java CI with Maven](https://github.com/KyrieChao/Failure/actions/workflows/ci.yml/badge.svg)](https://github.com/KyrieChao/Failure/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/KyrieChao/Failure/branch/main/graph/badge.svg)](https://codecov.io/gh/KyrieChao/Failure)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![Java 17+](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/technologies/downloads/)
[![Spring Boot 3](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Release](https://jitpack.io/v/KyrieChao/Failure.svg)](https://jitpack.io/#KyrieChao/Failure)

[English Version](./README.en.md)

Fail-Fast 是一个专为 Spring Boot 3.x 设计的轻量级、高性能参数校验与业务异常处理框架。它遵循 "Fail Fast, Fail Strict" 设计哲学，旨在消除样板代码，提供类型安全、流式调用的校验体验。

🔗 **实战示例项目**: [Failure-in-Action](https://github.com/KyrieChao/Failure-in-Action)

---

## 🚀 核心特性

- **流式校验链**: 支持 `Fail-Fast` (快速失败) 与 `Fail-Strict` (全量收集) 双模式。
- **丰富的断言库**: 内置对象、字符串、数值、集合、日期时间、枚举、Optional 等 50+ 种校验方法。
- **上下文集成**: 支持 `TypedValidator` 模式，将校验逻辑与业务逻辑解耦。
- **注解驱动**: 提供 `@Validate` 注解与 `FastValidator` 接口，支持 AOP 切面校验。
- **函数式结果**: 提供 `Result<T>` 单子类型，支持 `map`, `flatMap`, `recover` 等函数式操作。
- **智能异常处理**: 自动映射业务错误码到 HTTP 状态码，支持影子追踪 (`shadow-trace`) 快速定位问题。

## 📚 API 参考文档

**所有 API 的详细说明、方法签名及使用示例，请查阅完整参考文档：**

👉 **[API_REFERENCE.md](./API_REFERENCE.md)** 👈

该文档包含：
- Fail-Fast / Fail-Strict / Contextual 三种模式详解
- 完整的校验方法列表（String, Number, Date, Collection 等）
- 最佳实践与设计模式

---

## 🛠️ 快速开始

### 1. 环境要求
- JDK 17+
- Spring Boot 3.2.x+

### 2. 引入依赖
本项目发布在 JitPack。请在 `pom.xml` 中添加仓库和依赖：

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
    <version>Tag</version> <!-- 请替换为最新 Release 版本，例如 1.2.1 -->
</dependency>
```

### 3. 基础用法示例

**场景 1: 快速失败 (Fail-Fast)**
```java
// 一旦 notBlank 失败，立即抛出异常
Failure.begin()
    .notBlank(username, UserCode.USERNAME_REQUIRED)
    .email(email, UserCode.EMAIL_INVALID)
    .fail();
```

**场景 2: 全量收集 (Fail-Strict)**
```java
// 收集所有错误后统一抛出
Failure.strict()
    .notBlank(username, UserCode.USERNAME_REQUIRED)
    .email(email, UserCode.EMAIL_INVALID)
    .failAll();
```

更多高级用法（如上下文集成、自定义断言等）请参阅 [API_REFERENCE.md](./API_REFERENCE.md)。

---

## ⚙️ 配置说明

在 `application.yml` 中配置框架行为：

```yaml
fail-fast:
  shadow-trace: true   # 是否在异常堆栈中包含校验点的类名与行号（便于调试）
  verbose: true        # 多错误响应（Fail-Strict）是否包含详细的 errors 列表
  code-mapping:
    http-status:
      40001: 400       # 精确映射：错误码 40001 -> HTTP 400
      40100: 401
    groups:
      auth: [ "40100..40199" ]        # 范围映射：401xx -> 默认映射规则
      business: [ "40000..40099" ]
      # 精确值（数字或字符串）：40001 / "40001"
      # 自动顺序：5-1 会自动转为 1-5
```

## 🤝 贡献指南

欢迎提交 Issue 或 Pull Request！请确保在提交前运行 `mvn test` 并遵循现有的代码风格。

## 📄 许可证

Apache License 2.0 - 详情见 [LICENSE](LICENSE) 文件。

---
**Author**: [KyrieChao](https://github.com/KyrieChao)
