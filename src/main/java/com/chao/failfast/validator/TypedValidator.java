package com.chao.failfast.validator;

import com.chao.failfast.annotation.FastValidator;
import com.chao.failfast.internal.core.ResponseCode;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * TypedValidator 是一个抽象的泛型校验器类，实现了 FastValidator 接口，用于支持多种类型的校验逻辑。
 */
public abstract class TypedValidator implements FastValidator<Object> {

    /**
     * 使用 ConcurrentHashMap 存储不同类型的校验器，键为 Class 对象，值为对应的校验逻辑 BiConsumer。
     * 使用线程安全的 ConcurrentHashMap 确保多线程环境下的安全性。
     */
    private final Map<Class<?>, BiConsumer<Object, ValidationContext>> validators = new ConcurrentHashMap<>();

    /**
     * 构造方法，在创建实例时自动调用 registerValidators() 方法，
     * 让子类可以注册自己支持的校验逻辑。
     */
    protected TypedValidator() {
        registerValidators();
    }

    /**
     * 子类实现这个方法来注册校验逻辑
     */
    protected void registerValidators() {
        // 默认空实现
    }

    /**
     * 注册一个类型的校验方法
     */
    protected final <T> void register(Class<T> type, BiConsumer<T, ValidationContext> validator) {
        validators.put(type, (obj, ctx) -> validator.accept(type.cast(obj), ctx));
    }


    /**
     * 获取所有注册的类型
     */
    public Set<Class<?>> getRegisteredTypes() {
        return Set.copyOf(validators.keySet());
    }

    @Override
    public Class<?> getSupportedType() {
        // 单一类型直接返回，多类型返回 Object
        return validators.size() == 1 ? validators.keySet().iterator().next() : Object.class;
    }

    /**
     * 执行对象校验的核心方法。
     * <p>
     * 该方法首先检查目标对象是否为空，若为空则立即记录错误并终止校验流程；
     * 若目标对象非空，则尝试从已注册的校验器映射中查找对应类型的处理器，
     * 并调用该处理器执行具体校验逻辑；如果未找到匹配的处理器，则记录不支持的类型错误。
     *
     * @param object  待校验的目标对象，不能为 {@code null}
     * @param context 校验上下文，用于记录校验过程中的错误信息
     */
    @Override
    public final void validate(Object object, ValidationContext context) {
        if (object == null) {
            context.reportError(ResponseCode.of(50000, "校验对象不能为空"));
            return;
        }
        // 查找并执行对应类型的校验处理器
        BiConsumer<Object, ValidationContext> handler = validators.get(object.getClass());
        if (handler != null) {
            handler.accept(object, context);
        } else {
            // 处理未注册的类型情况
            context.reportError(ResponseCode.of(40099, "不支持的校验类型: " + object.getClass().getSimpleName()));
        }
    }
}