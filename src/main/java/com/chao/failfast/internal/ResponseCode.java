package com.chao.failfast.internal;

/**
 * 响应码接口 - 支持配置化HTTP状态映射
 * 定义业务错误码的标准结构，支持动态消息格式化和HTTP状态映射
 */
public interface ResponseCode {

    /**
     * 获取错误码数值
     *
     * @return 错误码整数值
     */
    int getCode();

    /**
     * 获取错误消息模板
     *
     * @return 错误消息字符串
     */
    String getMessage();

    /**
     * 获取详细错误描述
     *
     * @return 详细描述信息
     */
    String getDescription();

    /**
     * 支持动态消息模板
     * 使用String.format对消息模板进行参数化
     *
     * @param args 格式化参数
     * @return 格式化后的消息
     */
    default String formatMessage(Object... args) {
        return String.format(getMessage(), args);
    }

    /**
     * 创建简单的响应码（只有错误码）
     *
     * @param code 错误码
     * @return ResponseCode实例
     */
    static ResponseCode of(int code) {
        return new Simple(code, null, null);
    }

    /**
     * 创建响应码（错误码+消息）
     *
     * @param code    错误码
     * @param message 错误消息
     * @return ResponseCode实例
     */
    static ResponseCode of(int code, String message) {
        return new Simple(code, message, null);
    }

    /**
     * 创建完整的响应码（错误码+消息+描述）
     *
     * @param code        错误码
     * @param message     错误消息
     * @param description 详细描述
     * @return ResponseCode实例
     */
    static ResponseCode of(int code, String message, String description) {
        return new Simple(code, message, description);
    }

    /**
     * 简单响应码实现类
     * 使用record实现不可变的数据载体
     */
    record Simple(int code, String message, String description) implements ResponseCode {
        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }
}
