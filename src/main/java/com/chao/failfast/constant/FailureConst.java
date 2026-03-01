package com.chao.failfast.constant;

import com.chao.failfast.internal.core.ViolationSpec;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

/**
 * Failure 框架全局常量定义。
 *
 * <p>包含验证、配置、响应等模块的共享常量，统一维护避免魔法值散落。</p>
 *
 * @author KyrieChao
 * @since 1.3.1
 */
public final class FailureConst {

    /**
     * 私有构造方法，防止该工具类被实例化
     */
    private FailureConst() {
    }

    // ==================== 响应字段名（JSON 字段） ====================

    /**
     * 错误码字段
     */
    public static final String FIELD_CODE = "code";

    /**
     * 错误消息字段
     */
    public static final String FIELD_MESSAGE = "message";

    /**
     * 错误详情字段
     */
    public static final String FIELD_DESCRIPTION = "description";

    /**
     * 错误明细字段
     */
    public static final String FIELD_DETAIL = "detail";

    /**
     * 时间戳字段
     */
    public static final String FIELD_TIMESTAMP = "timestamp";

    /**
     * 错误列表字段（详细模式）
     */
    public static final String FIELD_ERRORS = "errors";


    // ==================== 通用错误消息（英文） ====================

    /**
     * 参数无效
     */
    public static final String INVALID_PARAMETER = "Invalid parameter";

    /**
     * 未知错误
     */
    public static final String UNKNOWN_ERROR = "Unknown error";

    /**
     * 未知
     */
    public static final String UNKNOWN = "Unknown";

    /**
     * 多重校验错误
     */
    public static final String MULTIPLE_VALIDATION_ERRORS = "Multiple validation errors";

    /**
     * 位置分隔符
     */
    public static final String AT = " at ";

    /**
     * 校验错误
     */
    public static final String VALIDATION_ERROR = "Unknown validation error";

    /**
     * 无法缩减空列表
     */
    public static final String CANNOT_REDUCE_EMPTY_LIST = "Cannot reduce empty list";


    // ==================== 错误提示模板（中文） ====================

    /**
     * code 不能为空
     */
    public static final String CODE_REQUIRED = "code 不能为空";

    /**
     * message 或 description 至少一个不能为 null
     */
    public static final String MESSAGE_OR_DESCRIPTION_REQUIRED = "message 或 description 至少一个不能为 null";

    /**
     * 不支持的校验类型前缀
     */
    public static final String UNSUPPORTED_VALIDATION_TYPE = "不支持的校验类型: ";

    /**
     * 校验失败前缀
     */
    public static final String VALIDATION_ERROR_PREFIX = "校验失败,共";

    /**
     * 错误项后缀
     */
    public static final String ERROR_ITEM_SUFFIX = " 项问题";

    /**
     * 错误过多提示
     */
    public static final String TOO_MANY_ERRORS = "校验失败，错误过多";


    // ==================== 系统默认值 ====================

    /**
     * 系统默认错误码
     */
    public static final Integer SYSTEM_CODE = 500;

    /**
     * 默认错误消息
     */
    public static final String DEFAULT_MESSAGE = "参数绑定失败";

    /**
     * 最大错误收集数
     */
    public static final int MAX_ERRORS = 100;


    // ==================== 时间相关 ====================

    /**
     * 中国标准时间（Asia/Shanghai）
     */
    public static final ZoneId CST = ZoneId.of("Asia/Shanghai");

    /**
     * 默认日期时间格式
     */
    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认日期时间格式化器
     */
    public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN);


    // ==================== 验证相关 ====================

    /**
     * 无操作配置（用于默认方法）
     */
    public static final Consumer<ViolationSpec> NO_OP = s -> {
    };
}