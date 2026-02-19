package com.chao.failfast.aspect;

import com.chao.failfast.annotation.Validate;
import com.chao.failfast.annotation.Validator;
import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.MultiBusiness;
import com.chao.failfast.internal.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 验证切面 - 处理 @Validate 注解声明的自定义验证器
 * 支持：
 * - 校验所有非 null 参数（默认行为）
 * - 通过 targets 指定要校验的参数名
 * - 通过 getSupportedType() 进行类型安全过滤
 * - 快速失败 / 全量收集 双模式
 */
@Slf4j
@Aspect
@Component
@Order(100)
public class ValidationAspect {
    private static final ConcurrentHashMap<Class<? extends Validator<Object>>, Validator<?>>
            VALIDATOR_CACHE = new ConcurrentHashMap<>();

    @Around("@annotation(validate)")
    public Object around(ProceedingJoinPoint point, Validate validate) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = point.getArgs();

        boolean failFast = validate.failFast();
        Class<?>[] groups = validate.groups();
        String fallbackCode = validate.errorCode();
        String fallbackMessage = validate.errorMessage();

        // 没有自定义验证器 → 直接放行，交给 Spring 处理标准校验
        if (validate.value().length == 0) {
            return handleFallbackValidation(point, parameters, args, fallbackCode, fallbackMessage);
        }

        // 是否指定了要校验的参数名
        Set<String> targetParamNames = validate.targets().length > 0
                ? Set.of(validate.targets())
                : null;

        List<Business> errors = new ArrayList<>();
        boolean stopped = false;

        for (Class<? extends Validator> validatorClass : validate.value()) {
            if (stopped && validate.failFast()) {
                break;
            }

            try {
                // 创建验证器实例
                Validator validator = getOrCreateValidator(validatorClass);

                Validator.ValidationContext context = new Validator.ValidationContext(failFast, groups);

                // 获取验证器声明的支持类型
                Class<?> supportedType = getValidatorSupportedType(validator);

                // 遍历所有参数
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (arg == null) continue;
                    String paramName = getParameterName(parameters[i], i);
                    // 如果指定了 targets 且当前参数名不在列表中 → 跳过
                    if (targetParamNames != null && !targetParamNames.contains(paramName)) continue;
                    // 类型兼容性检查
                    if (!supportedType.isAssignableFrom(arg.getClass())) {
                        log.warn("验证器 {} 声明支持 {}，但参数 {} 类型为 {}，跳过校验", validatorClass.getSimpleName(), supportedType.getSimpleName(), paramName, arg.getClass().getSimpleName());
                        continue;
                    }

                    // 执行校验
                    validator.validate(arg, context);

                    if (context.isStopped() && validate.failFast()) {
                        stopped = true;
                        // 可选：记录第一个失败的参数名，便于问题定位
                        if (!context.getErrors().isEmpty()) {
                            Business err = context.getErrors().get(0);
                            log.error("快速失败 - 参数 {} 校验失败: {}", paramName, err.getDetail());
                        }
                        break;
                    }
                }

                // 收集错误
                if (!context.isValid()) {
                    errors.addAll(context.getErrors());
                    if (validate.failFast()) stopped = true;
                }
            } catch (RuntimeException e) {
                log.error("无法实例化自定义验证器 {}: {}", validatorClass.getName(), e.toString());
            } catch (Exception e) {
                log.error("自定义验证器 {} 执行期间发生异常", validatorClass.getName(), e);
            }
        }
        // 处理收集到的错误
        if (!errors.isEmpty()) {
            if (errors.size() == 1) throw errors.get(0);
            else throw new MultiBusiness(errors);
        }
        return point.proceed();
    }

    @SuppressWarnings("unchecked")
    private <T> Validator<T> getOrCreateValidator(Class<? extends Validator> clazz) {
        return (Validator<T>) VALIDATOR_CACHE.computeIfAbsent((Class<? extends Validator<Object>>) clazz, key -> {
            try {
                return key.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate validator: " + key.getName(), e);
            }
        });
    }

    /**
     * 当没有自定义验证器时，执行简单的兜底校验：
     * - 所有非 null 参数必须非空（String/Collection/Map 判空，对象判 null）
     * - 使用 errorCode / errorMessage 作为统一错误信息
     */
    private Object handleFallbackValidation(ProceedingJoinPoint point, Parameter[] parameters, Object[] args,
                                            String fallbackCode, String fallbackMessage) throws Throwable {

        List<Business> errors = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            String paramName = getParameterName(parameters[i], i);

            if (arg == null) {
                errors.add(createFallbackError(paramName, fallbackCode, fallbackMessage, "不能为空"));
                continue;
            }
            // 简单判空逻辑（可根据需要扩展）
            if (arg instanceof String s && s.trim().isEmpty()) {
                errors.add(createFallbackError(paramName, fallbackCode, fallbackMessage, "不能为空或空白"));
            } else if (arg instanceof Collection<?> c && c.isEmpty()) {
                errors.add(createFallbackError(paramName, fallbackCode, fallbackMessage, "集合不能为空"));
            } else if (arg instanceof Map<?, ?> m && m.isEmpty()) {
                errors.add(createFallbackError(paramName, fallbackCode, fallbackMessage, "Map不能为空"));
            }
            // 可继续扩展其他类型（如数组等）
        }
        if (!errors.isEmpty()) {
            if (errors.size() == 1) {
                throw errors.get(0);
            }
            throw new MultiBusiness(errors);
        }

        return point.proceed();
    }

    private Business createFallbackError(String paramName, String codeStr, String msg, String detailSuffix) {
        int code = 40000;
        String message = "参数校验失败";
        String detail = "参数 " + paramName + " " + detailSuffix;

        if (StringUtils.hasText(codeStr)) {
            try {
                code = Integer.parseInt(codeStr);
            } catch (NumberFormatException ignored) {
            }
        }

        if (StringUtils.hasText(msg)) {
            message = msg;
            detail = msg + " (" + paramName + ")";
        }
        return Business.of(ResponseCode.of(code, message), detail);
    }

    /**
     * 获取验证器声明的支持类型
     * 优先使用重写的 getSupportedType() 方法
     * 如果未重写，尝试从泛型推断（不一定成功）
     */
    private Class<?> getValidatorSupportedType(Validator<?> validator) {
        Class<?> declared = validator.getSupportedType();
        if (declared != null && declared != Object.class) {
            return declared;
        }

        // 尝试从泛型推断（作为 fallback）
        try {
            Type genericSuper = validator.getClass().getGenericSuperclass();
            if (genericSuper instanceof ParameterizedType pt) {
                Type[] args = pt.getActualTypeArguments();
                if (args.length > 0 && args[0] instanceof Class<?> c) {
                    return c;
                }
            }
        } catch (Exception ignored) {
        }
        return Object.class;
    }

    /**
     * 获取参数名（兼容 -parameters 编译选项）
     */
    private String getParameterName(Parameter parameter, int index) {
        if (parameter.isNamePresent()) return parameter.getName();
        return "arg" + index;
    }
}