package com.chao.failfast.internal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FailFastProperties 配置属性测试")
class FailFastPropertiesTest {

    @Test
    @DisplayName("应当正确设置和获取属性")
    void shouldSetAndGetProperties() {
        FailFastProperties properties = new FailFastProperties();
        properties.setShadowTrace(true);
        
        FailFastProperties.CodeMapping mapping = new FailFastProperties.CodeMapping();
        Map<Integer, Integer> httpStatus = new HashMap<>();
        httpStatus.put(1001, 400);
        mapping.setHttpStatus(httpStatus);
        
        Map<String, List<Object>> groups = new HashMap<>();
        groups.put("group1", Collections.singletonList(1001));
        mapping.setGroups(groups);
        
        properties.setCodeMapping(mapping);
        
        assertThat(properties.isShadowTrace()).isTrue();
        assertThat(properties.getCodeMapping().getHttpStatus()).containsEntry(1001, 400);
        assertThat(properties.getCodeMapping().getGroups()).containsKey("group1");
    }
}
