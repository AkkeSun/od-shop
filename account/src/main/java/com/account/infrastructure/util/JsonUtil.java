package com.account.infrastructure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;

public class JsonUtil {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T parseJson(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> parseJsonList(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectNode toObjectNode(Object obj) {
        return objectMapper.valueToTree(obj);
    }

    public static String extractJsonField(String json, String... path) {
        try {
            JsonNode node = objectMapper.readTree(json);
            for (String key : path) {
                node = node.path(key);
            }
            return node.isMissingNode() ? "" : node.asText();
        } catch (Exception e) {
            return "";
        }
    }
}