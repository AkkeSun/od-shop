package com.common.infrastructure.util;

import java.lang.reflect.Field;
import org.springframework.util.StringUtils;

public class ValidationUtil {

    public static boolean existsField(Object value, String fieldName) {
        Object fieldValue = getField(value, fieldName);
        return fieldValue != null && StringUtils.hasText(fieldValue.toString());
    }

    public static Object getField(Object value, String fieldName) {
        try {
            Field field = value.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
}
