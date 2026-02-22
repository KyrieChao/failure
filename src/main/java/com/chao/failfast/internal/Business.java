package com.chao.failfast.internal;

import com.chao.failfast.config.CodeMappingConfig;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;

/**
 * 业务异常类 - 增强版
 * 提供丰富的业务异常信息，支持链式调用和自动堆栈跟踪控制
 */
@Getter
public class Business extends RuntimeException implements Serializable {

    /**
     * 响应码枚举、定义错误类型和HTTP状态码
     */
    private final ResponseCode responseCode;

    /**
     * 错误详细描述信息 覆盖父类description
     */
    private final String detail;

    /**
     * 异常发生的方法名称
     */
    private final String method;

    /**
     * 异常发生的位置信息（类名.方法名:行号）
     */
    private final String location;

    /**
     * HTTP状态码
     */
    private final HttpStatus httpStatus;
    /**
     * 序列化版本UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 构造函数
     *
     * @param responseCode 响应码枚举
     * @param detail       详细错误描述
     * @param method       发生异常的方法名
     * @param location     发生异常的位置信息
     */
    Business(ResponseCode responseCode, String detail, String method, String location, HttpStatus httpStatus) {
        this.responseCode = responseCode;
        this.detail = detail;
        this.method = method;
        this.location = location;
        this.httpStatus = httpStatus != null ? httpStatus : HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public static Business of(int code, String message) {
        return of(simpleCode(code, message));
    }

    public static Business of(int code, String message, String detail) {
        return of(simpleCode(code, message), detail);
    }

    public static Business of(int code, String message, String detail, Object... args) {
        return of(simpleCode(code, message), String.format(detail, args));
    }

    /**
     * 创建业务异常的静态工厂方法
     *
     * @param code 响应码
     * @return 构建好的Business异常对象
     */
    public static Business of(ResponseCode code) {
        return compose().code(code).materialize();
    }

    /**
     * 创建带详细描述的业务异常
     *
     * @param code   响应码
     * @param detail 详细错误描述
     * @return 构建好的Business异常对象
     */
    public static Business of(ResponseCode code, String detail) {
        return compose().code(code).detail(detail).materialize();
    }

    /**
     * 创建带格式化参数的业务异常
     *
     * @param code   响应码
     * @param detail 包含占位符的描述模板
     * @param args   格式化参数
     * @return 构建好的Business异常对象
     */
    public static Business of(ResponseCode code, String detail, Object... args) {
        return compose().code(code).detail(String.format(detail, args)).materialize();
    }

    /**
     * 创建指定方法和位置的业务异常
     *
     * @param code     响应码
     * @param detail   详细错误描述
     * @param method   方法名称
     * @param location 位置信息
     * @return 构建好的Business异常对象
     */
    public static Business of(ResponseCode code, String detail, String method, String location) {
        return compose().code(code).detail(detail).method(method).location(location).materialize();
    }

    private static ResponseCode simpleCode(int code, String message) {
        return ResponseCode.of(code, message, message);
    }

    /**
     * 获取构建器实例，用于链式调用构建Business对象
     *
     * @return Fabricator构建器实例
     */
    public static Fabricator compose() {
        return new Fabricator();
    }

    /**
     * Business对象构建器类
     * 支持链式调用方式构建复杂的Business异常对象
     */
    public static class Fabricator implements Serializable {
        /**
         * 响应码
         */
        private ResponseCode code;

        /**
         * 详细描述
         */
        private String detail;

        /**
         * 方法名称
         */
        private String method;

        /**
         * 位置信息
         */
        private String location;
        /**
         * 序列化版本UID
         */
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 设置响应码
         *
         * @param code 响应码枚举
         * @return 当前构建器实例，支持链式调用
         */
        public Fabricator code(ResponseCode code) {
            this.code = code;
            return this;
        }

        /**
         * 设置详细描述信息
         *
         * @param detail 详细描述
         * @return 当前构建器实例，支持链式调用
         */
        public Fabricator detail(String detail) {
            this.detail = detail;
            return this;
        }

        /**
         * 设置方法名称（包级私有）
         *
         * @param method 方法名称
         * @return 当前构建器实例，支持链式调用
         */
        Fabricator method(String method) {
            this.method = method;
            return this;
        }

        /**
         * 设置位置信息（包级私有）
         *
         * @param location 位置信息
         * @return 当前构建器实例，支持链式调用
         */
        Fabricator location(String location) {
            this.location = location;
            return this;
        }

        /**
         * 构建最终的Business对象
         * 自动处理默认值和上下文信息
         *
         * @return 构建完成的Business异常对象
         * @throws IllegalArgumentException 当code为空时抛出
         */
        public Business materialize() {
            // 校验必要参数
            if (code == null) throw new IllegalArgumentException("code 不能为空");
            // 设置默认详细描述
            if (detail == null) {
                detail = code.getDescription();
                if (detail == null) detail = code.getMessage();
            }
            // 根据上下文自动填充方法和位置信息
            FailureContext ctx = Ex.getContext();
            if (ctx != null && ctx.isShadowTrace()) {
                if (method == null) method = Ex.method();
                if (location == null) location = Ex.location();
            }
            CodeMappingConfig cfg = Ex.getContext() != null ? Ex.getContext().getCodeMappingConfig() : null;
            HttpStatus status = (cfg != null) ? cfg.resolveHttpStatus(code.getCode()) : HttpStatus.INTERNAL_SERVER_ERROR;
            return new Business(code, detail, method, location, status);
        }
    }

    /**
     * 重写toString方法，提供格式化的异常信息输出
     * 格式：[方法名] {code=xxx_xx, mes=消息, des=描述} (文件名:行号)
     *
     * @return 格式化的字符串表示
     */
    @Override
    public String toString() {
        // 格式化代码为xxx_xx格式
        String codeStr = String.valueOf(responseCode.getCode()).replaceFirst("(\\d{3})(\\d{2})", "$1_$2");
        // 构建基础信息字符串
        String base = "{code=%s, mes=%s, des=%s}".formatted(codeStr, responseCode.getMessage(), detail);
        // 根据是否有方法信息决定输出格式
        if (method == null) return base + (location != null ? " (" + extractFileLine(location) + ")" : "");

        // 处理内部类方法名 (如 TestController$AdvancedUserValidator#validate -> TestController#validate)
        String displayMethod = method;
        int dollarIndex = method.indexOf('$');
        if (dollarIndex > 0) {
            int hashIndex = method.lastIndexOf('#');
            if (hashIndex > dollarIndex) {
                displayMethod = method.substring(0, dollarIndex) + method.substring(hashIndex);
            }
        }
        return "[%s] %s".formatted(displayMethod, base) + (location != null ? " (" + extractFileLine(location) + ")" : "");
    }

    /**
     * 从完整位置信息中提取文件名和行号
     * 输入格式："ClassName.methodName(ClassName.java:123)" 或 "TestController$AdvancedUserValidator.validate(TestController.java:103)"
     * 输出格式："ClassName.java:123" 或 "TestController.java:103"
     *
     * @param loc 完整的位置信息字符串
     * @return 提取的文件名和行号，如果输入为null则返回空字符串
     */
    private String extractFileLine(String loc) {
        if (loc == null) return "";
        // 查找左括号位置
        int start = loc.indexOf("(");
        if (start < 0) return loc;  // 如果没有找到括号，返回原字符串
        // 提取括号内的内容（去除右括号）
        String content = loc.substring(start + 1, loc.length() - 1);

        // 处理内部类文件名包含 $ 的情况 (如 TestController$AdvancedUserValidator.java:103)
        int dollarIndex = content.indexOf('$');
        if (dollarIndex > 0) {
            // 查找文件名结束的点号 (如 .java)
            int dotIndex = content.indexOf('.', dollarIndex);
            if (dotIndex > 0) {
                return content.substring(0, dollarIndex) + content.substring(dotIndex);
            }
        }
        return content;
    }
}
