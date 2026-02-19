package com.chao.failfast.config;

import com.chao.failfast.internal.FailFastProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 错误码映射配置 - 支持配置化HTTP状态映射
 * 负责将业务错误码映射到相应的HTTP状态码
 * 支持精确匹配、范围匹配和动态注册功能
 */
@Component
public class CodeMappingConfig {

    /**
     * FailFast配置属性
     */
    private final FailFastProperties properties;
    /**
     * 默认错误码到HTTP状态码的映射表
     */
    private final Map<Integer, HttpStatus> DEFAULT_MAPPINGS;

    /**
     * 构造函数
     *
     * @param properties FailFast配置属性
     */
    public CodeMappingConfig(FailFastProperties properties) {
        this.properties = properties;
        Map<Integer, HttpStatus> temp = new HashMap<>();
        initializeDefaultMappings(temp);
        // 加载自定义配置（允许覆盖默认值）
        loadCustomMappings(temp);
        this.DEFAULT_MAPPINGS = Collections.unmodifiableMap(temp);
    }

    // 初始化默认映射关系
    private void initializeDefaultMappings(Map<Integer, HttpStatus> map) {
        // 4xx 客户端错误映射
        map.put(40000, HttpStatus.BAD_REQUEST);
        map.put(40100, HttpStatus.UNAUTHORIZED);
        map.put(40300, HttpStatus.FORBIDDEN);
        map.put(40400, HttpStatus.NOT_FOUND);
        map.put(40500, HttpStatus.METHOD_NOT_ALLOWED);
        map.put(40800, HttpStatus.REQUEST_TIMEOUT);
        map.put(40900, HttpStatus.CONFLICT);
        map.put(41000, HttpStatus.GONE);
        map.put(41300, HttpStatus.PAYLOAD_TOO_LARGE);
        map.put(41500, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        map.put(42200, HttpStatus.UNPROCESSABLE_ENTITY);
        map.put(42900, HttpStatus.TOO_MANY_REQUESTS);
        // 5xx 服务端错误映射
        map.put(50000, HttpStatus.INTERNAL_SERVER_ERROR);
        map.put(50100, HttpStatus.NOT_IMPLEMENTED);
        map.put(50200, HttpStatus.BAD_GATEWAY);
        map.put(50300, HttpStatus.SERVICE_UNAVAILABLE);
        map.put(50400, HttpStatus.GATEWAY_TIMEOUT);
    }

    private void loadCustomMappings(Map<Integer, HttpStatus> map) {
        Map<Integer, Integer> custom = properties.getCodeMapping().getHttpStatus();
        for (Map.Entry<Integer, Integer> entry : custom.entrySet()) {
            try {
                map.put(entry.getKey(), HttpStatus.valueOf(entry.getValue()));
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * 解析错误码对应的HTTP状态
     * 采用多层次匹配策略：精确匹配 → 范围匹配 → 大类匹配 → 标准HTTP码 → 默认值
     *
     * @param code 业务错误码
     * @return 对应的HttpStatus对象
     */
    public HttpStatus resolveHttpStatus(int code) {
        // 1. 精确匹配：查找完全相同的错误码
        HttpStatus exact = DEFAULT_MAPPINGS.get(code);
        if (exact != null) return exact;
        // 2. 范围匹配：根据错误码前缀匹配（如40000~40099匹配40000）
        int rangeStart = (code / 100) * 100;
        HttpStatus rangeStatus = DEFAULT_MAPPINGS.get(rangeStart);
        if (rangeStatus != null) return rangeStatus;

        // 3. 大类匹配：根据错误码范围确定大类
        if (code >= 40000 && code < 50000) return HttpStatus.BAD_REQUEST;
        if (code >= 50000 && code < 60000) return HttpStatus.INTERNAL_SERVER_ERROR;
        // 4. 标准HTTP状态码匹配（100-599）
        if (code >= 100 && code <= 599) {
            try {
                return HttpStatus.valueOf(code);
            } catch (IllegalArgumentException ignored) {
            }
        }
        // 5. 默认返回500内部服务器错误
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * 获取指定分组的所有错误码
     *
     * @param groupName 分组名称
     * @return 错误码列表，如果分组不存在则返回null
     */
    public List<Integer> getGroupCodes(String groupName) {
        return properties.getCodeMapping().getGroups().get(groupName);
    }

    /**
     * 判断错误码是否属于指定分组
     * 支持精确匹配和范围匹配两种方式
     *
     * @param code      要检查的错误码
     * @param groupName 分组名称
     * @return 如果属于该分组返回true，否则返回false
     */
    public boolean isInGroup(int code, String groupName) {
        List<Integer> groupCodes = getGroupCodes(groupName);
        if (groupCodes == null || groupCodes.isEmpty()) return false;
        // 支持范围匹配：检查是否在分组定义的范围内
        for (Integer groupCode : groupCodes) {
            int rangeStart = (groupCode / 100) * 100;
            int rangeEnd = rangeStart + 99;
            if (code >= rangeStart && code <= rangeEnd) return true;
        }
        // 精确匹配：检查是否完全相等
        return groupCodes.contains(code);
    }
}
