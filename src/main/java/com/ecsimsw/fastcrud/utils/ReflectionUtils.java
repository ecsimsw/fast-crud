package com.ecsimsw.fastcrud.utils;

import com.ecsimsw.fastcrud.exception.ReflectionException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionUtils {

    public static void copyFields(Object src, Object des) {
        for (Field field : src.getClass().getDeclaredFields()) {
            setFieldValue(des, getFieldValue(src, field), field);
        }
    }

    public static void setAnnotatedFieldValue(Object target, Class<? extends Annotation> annotationType, Object value) {
        final Field field = getFieldForAnnotation(target, annotationType);
        setFieldValue(target, value, field);
    }

    private static Object getFieldValue(Object src, Field field) {
        try {
            field.setAccessible(true);
            return field.get(src);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e.getMessage());
        }
    }

    private static void setFieldValue(Object target, Object value, Field field) {
        try {
            field.setAccessible(true);
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e.getMessage());
        }
    }

    private static Field getFieldForAnnotation(Object target, Class<? extends Annotation> annotationType) {
        return Arrays.stream(target.getClass().getDeclaredFields())
                .filter(it -> it.isAnnotationPresent(annotationType))
                .findAny()
                .orElseThrow(() -> new ReflectionException("No annotation present"));
    }

    public static Method getMethodOf(Object target, String methodName, Class<?>... parameterTypes) {
        try {
            return target.getClass().getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new ReflectionException(e.getMessage());
        }
    }
}
