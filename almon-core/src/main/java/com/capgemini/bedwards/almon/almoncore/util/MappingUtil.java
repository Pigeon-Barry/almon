package com.capgemini.bedwards.almon.almoncore.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.util.MultiValueMap;


public final class MappingUtil {
    private MappingUtil() {
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public static ObjectNode convertToObjectNode(MultiValueMap<String, String> paramMap) {
        return getObjectMapper().convertValue(paramMap, ObjectNode.class);
    }
}
