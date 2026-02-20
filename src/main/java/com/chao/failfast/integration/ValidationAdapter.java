package com.chao.failfast.integration;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.MultiBusiness;
import com.chao.failfast.internal.ResponseCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Bean Validation 适配器 - 桥接标准 JSR-303/380 验证
 * 将标准的Jakarta Validation API转换为FailFast框架的Business异常
 * 提供多种验证模式：快速失败、全部验证、列表返回等
 */
@Component
public class ValidationAdapter {
    /**
     * Jakarta Validator实例
     */
    private final Validator validator;

    public ValidationAdapter(Validator validator) {
        this.validator = validator;
    }

    /**
     * 校验对象，失败时抛出 Business 异常（FailFast模式）
     * 遇到第一个验证错误立即抛出异常，提高响应速度
     *
     * @param object 待验证的对象
     * @param <T>    对象类型
     * @throws Business 验证失败时抛出的业务异常
     */
    public <T> void validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);

        // 快速失败：只处理第一个错误
        if (!violations.isEmpty()) {
            ConstraintViolation<T> first = violations.iterator().next();
            throw convertToBusiness(first);
        }
    }

    /**
     * 校验对象，收集所有错误后抛出 MultiBusiness
     * 适用于需要一次性获取所有验证错误的场景
     *
     * @param object 待验证的对象
     * @param <T>    对象类型
     * @throws Business      单个错误时抛出Business异常
     * @throws MultiBusiness 多个错误时抛出批量异常
     */
    public <T> void validateAll(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            // 收集所有验证错误
            List<Business> errors = violations.stream()
                    .map(this::convertToBusiness)
                    .collect(Collectors.toList());

            // 根据错误数量决定抛出单异常还是批量异常
            if (errors.size() == 1) throw errors.get(0);
            else throw new MultiBusiness(errors);
        }
    }

    /**
     * 校验并返回错误列表（不抛出异常）
     * 适用于需要程序化处理验证结果的场景
     *
     * @param object 待验证的对象
     * @param <T>    对象类型
     * @return 验证错误列表，验证通过时返回空列表
     */
    public <T> List<Business> validateToList(T object) {
        return validator.validate(object).stream()
                .map(this::convertToBusiness)
                .collect(Collectors.toList());
    }

    /**
     * 检查对象是否验证通过
     *
     * @param object 待验证的对象
     * @param <T>    对象类型
     * @return true表示验证通过，false表示存在验证错误
     */
    public <T> boolean isValid(T object) {
        return validator.validate(object).isEmpty();
    }

    /**
     * 将 ConstraintViolation 转换为 Business 异常
     * 提取验证错误信息并构建成统一的Business异常格式
     *
     * @param violation 约束违反对象
     * @param <T>       对象类型
     * @return 构建好的Business异常
     */
    private <T> Business convertToBusiness(ConstraintViolation<T> violation) {
        ResponseCode code = mapAnnotationToCode(violation);
        String detail = formatViolationMessage(violation);
        return Business.of(code, detail);
    }

    /**
     * 根据注解类型映射错误码
     * 为不同的验证注解提供语义化的错误码和描述
     *
     * @param violation 约束违反对象
     * @param <T>       对象类型
     * @return 对应的ResponseCode对象
     */
    private <T> ResponseCode mapAnnotationToCode(ConstraintViolation<T> violation) {
        Annotation annotation = violation.getConstraintDescriptor().getAnnotation();
        Class<? extends Annotation> type = annotation.annotationType();

        // 根据注解类型返回对应的错误码
        if (type == NotNull.class || type == NotEmpty.class || type == NotBlank.class) {
            return ResponseCode.of(40001, "字段不能为空", getFieldName(violation) + " 是必填项");
        }
        if (type == Size.class || type == Length.class) {
            return ResponseCode.of(40002, "字段长度不合法", getFieldName(violation) + " 长度不符合要求");
        }
        if (type == Min.class || type == Max.class || type == DecimalMin.class || type == DecimalMax.class) {
            return ResponseCode.of(40003, "数值范围不合法", getFieldName(violation) + " 数值超出允许范围");
        }
        if (type == Email.class) {
            return ResponseCode.of(40004, "邮箱格式错误", getFieldName(violation) + " 邮箱格式不正确");
        }
        if (type == Pattern.class) {
            return ResponseCode.of(40005, "字段格式错误", getFieldName(violation) + " 格式不符合要求");
        }
        if (type == Positive.class || type == PositiveOrZero.class) {
            return ResponseCode.of(40006, "数值必须为正数", getFieldName(violation) + " 必须大于0");
        }
        if (type == Negative.class || type == NegativeOrZero.class) {
            return ResponseCode.of(40007, "数值必须为负数", getFieldName(violation) + " 必须小于0");
        }
        if (type == Future.class || type == FutureOrPresent.class) {
            return ResponseCode.of(40008, "日期必须是未来时间", getFieldName(violation) + " 必须是未来日期");
        }
        if (type == Past.class || type == PastOrPresent.class) {
            return ResponseCode.of(40009, "日期必须是过去时间", getFieldName(violation) + " 必须是过去日期");
        }
        if (type == AssertTrue.class || type == AssertFalse.class) {
            return ResponseCode.of(40010, "布尔值校验失败", getFieldName(violation) + " 布尔值不符合要求");
        }
        // 默认错误码
        return ResponseCode.of(40000, "参数校验失败", violation.getMessage());
    }

    /**
     * 格式化验证错误消息
     * 提供统一的错误信息格式，包含字段名、错误描述和实际值
     *
     * @param violation 约束违反对象
     * @param <T>       对象类型
     * @return 格式化后的错误消息
     */
    private <T> String formatViolationMessage(ConstraintViolation<T> violation) {
        Object invalidValue = violation.getInvalidValue();
        String fieldName = getFieldName(violation);
        return String.format("当前:%s->%s", fieldName, invalidValue != null ? invalidValue.toString() : "null");
    }

    /**
     * 获取验证字段的名称
     *
     * @param violation 约束违反对象
     * @param <T>       对象类型
     * @return 字段路径字符串
     */
    private <T> String getFieldName(ConstraintViolation<T> violation) {
        return violation.getPropertyPath().toString();
    }
}
