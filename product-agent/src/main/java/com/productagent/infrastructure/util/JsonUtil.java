package com.productagent.infrastructure.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JsonUtil() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static <T> T parseJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static JsonNode getJsonNode(String payload) {
        try {
            return objectMapper.readTree(payload);
        } catch (Exception e) {
            return null;
        }
    }
}