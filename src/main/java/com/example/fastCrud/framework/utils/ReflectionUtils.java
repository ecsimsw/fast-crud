package com.example.fastCrud.framework.utils;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static void copyFields(Object src, Object des) throws IllegalAccessException {
        for (Field field : src.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            field.set(des, field.get(src));
        }
    }

    public static void setFieldValue(Object target, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        final Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
