package com.ecsimsw.lib.utils;

import com.ecsimsw.lib.exception.ReflectionException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

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

    public static Field getFieldForAnnotation(Object target, Class<? extends Annotation> annotationType) {
        return Arrays.stream(target.getClass().getDeclaredFields())
                .filter(it -> it.isAnnotationPresent(annotationType))
                .findAny()
                .orElseThrow();
    }
}
