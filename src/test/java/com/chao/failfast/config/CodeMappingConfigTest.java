package com.chao.failfast.config;

import com.chao.failfast.internal.FailFastProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CodeMappingConfig 配置类测试")
class CodeMappingConfigTest {

    private CodeMappingConfig config;
    private FailFastProperties properties;

    @BeforeEach
    void setUp() {
        properties = new FailFastProperties();
        FailFastProperties.CodeMapping mapping = new FailFastProperties.CodeMapping();
        properties.setCodeMapping(mapping);
        config = new CodeMappingConfig(properties);
    }

    @Nested
    @DisplayName("HTTP 状态码解析测试")
    class ResolveHttpStatusTest {
        @Test
        @DisplayName("默认映射应当正确解析")
        void shouldResolveDefaultMappings() {
            assertThat(config.resolveHttpStatus(40000)).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(config.resolveHttpStatus(50000)).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        @DisplayName("自定义映射应当覆盖默认映射")
        void shouldResolveCustomMappings() {
            Map<Integer, Integer> custom = new HashMap<>();
            custom.put(90000, 418); // I'm a teapot
            properties.getCodeMapping().setHttpStatus(custom);
            
            // Re-initialize config to load custom mappings
            config = new CodeMappingConfig(properties);
            
            assertThat(config.resolveHttpStatus(90000)).isEqualTo(HttpStatus.I_AM_A_TEAPOT);
        }

        @Test
        @DisplayName("范围匹配应当正确解析")
        void shouldResolveRangeMappings() {
            // 400xx -> 400
            // Assuming 40000 is mapped to BAD_REQUEST
            // 40099 should map to BAD_REQUEST if exact match not found
            // But implementation logic:
            // int rangeStart = (code / 100) * 100;
            // HttpStatus rangeStatus = DEFAULT_MAPPINGS.get(rangeStart);
            
            // 40000 is mapped. So 40099 -> 40000 -> BAD_REQUEST
            assertThat(config.resolveHttpStatus(40099)).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("大类匹配应当正确解析")
        void shouldResolveCategoryMappings() {
            // 49999 is not mapped, range 49900 not mapped.
            // But 40000 <= code < 50000 -> BAD_REQUEST
            assertThat(config.resolveHttpStatus(49999)).isEqualTo(HttpStatus.BAD_REQUEST);
            
            // 59999 -> INTERNAL_SERVER_ERROR
            assertThat(config.resolveHttpStatus(59999)).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        @DisplayName("标准HTTP状态码应当正确解析")
        void shouldResolveStandardHttpStatus() {
            assertThat(config.resolveHttpStatus(404)).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(config.resolveHttpStatus(200)).isEqualTo(HttpStatus.OK);
        }

        @Test
        @DisplayName("未知错误码应当解析为500")
        void shouldResolveUnknownToInternalServerError() {
            assertThat(config.resolveHttpStatus(99999)).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Nested
    @DisplayName("分组测试")
    class GroupTest {
        @BeforeEach
        void setUpGroups() {
            Map<String, List<Object>> groups = new HashMap<>();
            groups.put("testGroup", Arrays.asList(1001, "2000-2005", "3000..3002"));
            properties.getCodeMapping().setGroups(groups);
            config = new CodeMappingConfig(properties);
        }

        @Test
        @DisplayName("应当正确判断错误码是否在分组内")
        void shouldCheckIfInGroup() {
            assertThat(config.isInGroup(1001, "testGroup")).isTrue();
            assertThat(config.isInGroup(2003, "testGroup")).isTrue(); // In 2000-2005
            assertThat(config.isInGroup(3001, "testGroup")).isTrue(); // In 3000..3002
            
            assertThat(config.isInGroup(1002, "testGroup")).isFalse();
            assertThat(config.isInGroup(2006, "testGroup")).isFalse();
        }

        @Test
        @DisplayName("应当正确展开分组错误码")
        void shouldExpandGroupCodes() {
            // "2000-2005" -> 2000, 2001, 2002, 2003, 2004, 2005 (6 items)
            // Total items > 5
            String expanded = config.getGroupCodesExpanded("testGroup");
            assertThat(expanded).contains("...");
            
            // Small group
            Map<String, List<Object>> groups = new HashMap<>();
            groups.put("smallGroup", Arrays.asList(1, 2));
            properties.getCodeMapping().setGroups(groups);
            config = new CodeMappingConfig(properties);
            
            assertThat(config.getGroupCodesExpanded("smallGroup")).isEqualTo("[1, 2]");
        }
    }
}
