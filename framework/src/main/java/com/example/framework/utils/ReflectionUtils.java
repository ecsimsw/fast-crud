package com.example.framework.utils;

import com.example.framework.exception.ReflectionException;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static void copyFields(Object src, Object des) {
        try {
            for (Field field : src.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                field.set(des, field.get(src));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new ReflectionException(e.getMessage());
        }
    }

    public static void setFieldValue(Object target, String fieldName, Object value) {
        try {
            final Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new ReflectionException(e.getMessage());
        }
    }
}
