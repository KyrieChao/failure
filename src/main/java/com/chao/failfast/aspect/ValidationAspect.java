package com.chao.failfast.aspect;

import com.chao.failfast.annotation.FastValidator;
import com.chao.failfast.annotation.Validate;
import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.MultiBusiness;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 验证切面 - 处理 @Validate 注解声明的自定义验证器
 * 当前只保留最核心功能：
 * - value：指定自定义验证器
 * - fast：是否快速失败（默认 true）
 */
@Slf4j
@Aspect
@Component
@Order(100)
public class ValidationAspect {

    // 验证器缓存（提升性能，避免每次反射 newInstance）
    private static final ConcurrentHashMap<Class<? extends FastValidator<Object>>, FastValidator<Object>>
            VALIDATOR_CACHE = new ConcurrentHashMap<>();
    @Autowired
    private ApplicationContext applicationContext;

    @Around("@annotation(validate)")
    public Object around(ProceedingJoinPoint point, Validate validate) throws Throwable {
        Object[] args = point.getArgs();

        // 快速失败开关（默认 true）
        boolean failFast = validate.fast();

        // 如果没有指定自定义验证器 → 直接放行，交给 Spring 处理 @Valid / @Validated
        if (validate.value().length == 0) {
            return point.proceed();
        }

        List<Business> errors = new ArrayList<>();
        boolean stopped = false;

        // 执行所有自定义验证器
        for (Class<? extends FastValidator> validatorClass : validate.value()) {
            if (stopped) {
                break;
            }

            try {
                // 从缓存获取或创建验证器
                FastValidator<Object> fastValidator = getOrCreateValidator(validatorClass);

                // 创建验证上下文
                FastValidator.ValidationContext context = new FastValidator.ValidationContext(failFast);

                // 获取验证器支持的类型（类型安全过滤）
                Class<?> supportedType = getValidatorSupportedType(fastValidator);

                // 遍历所有方法参数
                for (Object arg : args) {
                    if (arg == null) continue;
                    // 类型兼容性检查
                    if (!supportedType.isAssignableFrom(arg.getClass())) {
                        continue;
                    }
                    // 执行自定义校验
                    fastValidator.validate(arg, context);
                    // 快速失败检查
                    if (context.isStopped() && failFast) {
                        stopped = true;
                        break;
                    }
                }

                // 收集错误
                if (!context.isValid()) {
                    errors.addAll(context.getErrors());
                    if (failFast) stopped = true;
                }
            } catch (Exception e) {
                log.error("自定义验证器 {} 执行失败", validatorClass.getSimpleName(), e);
            }
        }

        // 处理最终错误
        if (!errors.isEmpty()) {
            if (errors.size() == 1) {
                throw errors.get(0);
            } else {
                throw new MultiBusiness(errors);
            }
        }

        // 全部通过，继续执行目标方法
        return point.proceed();
    }

    @SuppressWarnings("unchecked")
    private FastValidator<Object> getOrCreateValidator(Class<? extends FastValidator> clazz) {
        if (applicationContext.getBeanNamesForType(clazz).length > 0) {
            return applicationContext.getBean(clazz);
        }

        // 否则 fallback 到反射创建
        return VALIDATOR_CACHE.computeIfAbsent(
                (Class<? extends FastValidator<Object>>) clazz,
                key -> {
                    try {
                        return key.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to instantiate validator: " + key.getName(), e);
                    }
                });
    }

    /**
     * 获取验证器支持的类型（用于类型过滤）
     */
    private Class<?> getValidatorSupportedType(FastValidator<?> fastValidator) {
        Class<?> declared = fastValidator.getSupportedType();
        if (declared != null && declared != Object.class) {
            return declared;
        }

        Class<?> clazz = fastValidator.getClass();

        // 1. 尝试从泛型接口推断 (implements Validator<T>)
        try {
            for (Type type : clazz.getGenericInterfaces()) {
                if (type instanceof ParameterizedType pt) {
                    if (FastValidator.class.equals(pt.getRawType())) {
                        Type[] args = pt.getActualTypeArguments();
                        if (args.length > 0 && args[0] instanceof Class<?> c) {
                            return c;
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }

        // 2. 尝试从泛型父类推断 (extends BaseValidator<T>)
        try {
            Type genericSuper = clazz.getGenericSuperclass();
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
}