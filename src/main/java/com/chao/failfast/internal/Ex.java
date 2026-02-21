package com.chao.failfast.internal;

import java.util.Set;

/**
 * 异常构建工具类 - 线程安全改进版
 */
public final class Ex {
    /**
     * StackWalker实例，用于遍历调用栈
     * RETAIN_CLASS_REFERENCE选项保留类引用信息，便于获取完整的类名
     */
    private static final StackWalker WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    /**
     * 需要跳过的包前缀集合
     * 这些前缀通常对应框架代码、系统类或内部工具类
     * 在捕获调用位置时会过滤掉这些无关的调用帧
     */
    private static final Set<String> SKIP_PREFIXES = Set.of(
            "com.chao.failfast.advice",     // 异常处理切面包
            "com.chao.failfast.annotation", // 内部工具包
            "com.chao.failfast.aspect",     // 切面包
            "com.chao.failfast.config",     // 配置类
            "com.chao.failfast.integration",// 集成包
            "com.chao.failfast.internal",   // 内部工具包
            "com.chao.failfast.result",     // 响应结果类
            "com.chao.failfast.Failure",    // 失败处理类
            "org.springframework",          // Spring框架
            "org.apache",                   // Apache相关组件
            "jakarta",                      // Jakarta EE规范
            "java.",                        // Java标准库
            "jdk.",                         // JDK内部类
            "sun."                          // Sun Microsystems遗留类
    );

    /**
     * FailFast上下文对象，用于控制异常处理的行为配置
     * 包含是否打印方法信息等运行时配置
     */
    private static FailureContext context;

    /**
     * 设置FailFast上下文
     *
     * @param ctx FailFast上下文对象，包含配置信息
     */
    public static void setContext(FailureContext ctx) {
        Ex.context = ctx;
    }


    /**
     * 获取当前的FailFast上下文
     *
     * @return 当前的FailFast上下文对象，可能为null
     */
    static FailureContext getContext() {
        return context;
    }


    /**
     * 私有构造函数，防止实例化
     * 这是一个纯工具类，所有方法都是静态的
     */
    private Ex() {
    }

    /**
     * 获取当前调用位置信息的便捷方法
     * 主要用于需要单独获取位置信息的场景
     *
     * @return 返回格式化的调用位置字符串，格式如"ClassName.methodName(ClassName.java:lineNumber)"
     * 如果未启用方法打印或无法获取，则返回null
     */
    static String location() {
        return isShadowTrace() ? captureLocation() : null;
    }

    /**
     * 获取当前调用方法信息的便捷方法
     * 主要用于需要单独获取方法信息的场景
     *
     * @return 返回格式化的方法名称字符串，格式如"SimpleClassName#methodName"
     * 如果未启用方法打印或无法获取，则返回null
     */
    static String method() {
        return isShadowTrace() ? captureMethodName() : null;
    }

    /**
     * 检查是否应该打印方法信息的内部辅助方法
     * 通过检查上下文配置来决定是否启用方法信息捕获功能
     *
     * @return 当上下文存在且启用了方法打印时返回true，否则返回false
     */
    private static boolean isShadowTrace() {
        return context != null && context.isShadowTrace();
    }

    /**
     * 捕获并格式化当前调用位置信息
     * 使用StackWalker遍历调用栈，过滤掉系统和框架相关的调用帧，
     * 找到第一个用户业务代码的位置并格式化输出
     *
     * @return 返回格式化的调用位置字符串，如果未启用打印或无法获取则返回null，
     * 格式示例："MyService.doSomething(MyService.java:45)"
     */
    static String captureLocation() {
        // 首先检查是否启用方法打印功能
        if (!isShadowTrace()) return null;

        // 使用StackWalker遍历调用栈
        return WALKER.walk(stream -> stream
                // 过滤掉不需要的调用帧（框架、系统类等）
                .filter(Ex::isNotSkipped)
                // 获取第一个有效的业务调用帧
                .findFirst()
                // 将栈帧格式化为位置字符串
                .map(Ex::formatLocation)
                // 如果找不到有效帧，返回默认值
                .orElse("unknown"));
    }

    /**
     * 获取方法名称的静态方法
     *
     * @return 返回方法名称字符串，如果条件不满足则返回null
     */
    static String captureMethodName() {
        if (!isShadowTrace()) return null;

        return WALKER.walk(stream -> stream
                .filter(Ex::isNotSkipped)
                // 额外过滤验证器类，以便定位到调用验证器的业务方法 (e.g. Controller/Service)
                // 1. 过滤 com.chao.failfast.validator 包下的类
                // 2. 过滤类名以 Validator 或 Validators 结尾的类
                .filter(f -> !f.getClassName().startsWith("com.chao.failfast.validator")
                        && !f.getClassName().endsWith("Validator")
                        && !f.getClassName().endsWith("Validators"))
                .findFirst()
                .map(Ex::formatMethodName)
                .orElse("unknown"));
    }

    /**
     * 检查给定的栈帧是否不被跳过
     * 该方法通过检查类名是否以任何预定义的前缀开头来判断是否跳过该栈帧
     *
     * @param f 要检查的栈帧对象
     * @return 如果类名不以任何SKIP_PREFIXES中的前缀开头，返回true；否则返回false
     */
    private static boolean isNotSkipped(StackWalker.StackFrame f) {
        String cls = f.getClassName();
        return SKIP_PREFIXES.stream().noneMatch(cls::startsWith);
    }

    /**
     * 格式化栈帧信息为可读的字符串
     *
     * @param f StackWalker.StackFrame对象，包含调用栈帧的信息
     * @return 格式化后的字符串，格式为"类名.方法名(类名.java:行号)"
     */
    private static String formatLocation(StackWalker.StackFrame f) {
        String full = f.getClassName();
        String simple = full.substring(full.lastIndexOf('.') + 1);
        int line = f.getLineNumber();
        // 如果行号有效(>0)，显示文件名:行号；否则仅显示文件名（避免出现 -1 导致无法跳转）
        // CGLIB 代理类或 Native 方法通常没有行号信息
        String fileInfo = (line > 0) ? simple + ".java:" + line : simple + ".java";
        return simple + "." + f.getMethodName() + "(" + fileInfo + ")";
    }

    /**
     * 格式化方法名称，将类名和方法名组合成特定格式的字符串
     *
     * @param f StackWalker.StackFrame对象，包含调用栈信息
     * @return 返回格式化后的字符串，格式为"简单类名#方法名"
     */
    private static String formatMethodName(StackWalker.StackFrame f) {
        String cls = f.getClassName();
        String simple = cls.substring(cls.lastIndexOf('.') + 1);
        String methodName = f.getMethodName();

        // 处理 Lambda 表达式生成的方法名: lambda$resultsUtils$9 -> resultsUtils
        if (methodName.startsWith("lambda$")) {
            int firstDollar = methodName.indexOf('$');
            int lastDollar = methodName.lastIndexOf('$');
            if (firstDollar != -1 && lastDollar > firstDollar) {
                methodName = methodName.substring(firstDollar + 1, lastDollar);
            }
        }
        return simple + "#" + methodName;
    }
}
