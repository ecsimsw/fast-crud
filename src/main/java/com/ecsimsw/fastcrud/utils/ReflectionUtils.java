package com.ecsimsw.fastcrud.utils;

import com.ecsimsw.fastcrud.exception.ReflectionException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

    private static void setFieldValue(Object target, Object value, Field field) {
        try {
            field.setAccessible(true);
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e.getMessage());
        }
    }

    private static Object getFieldValue(Object src, Field field) {
        try {
            field.setAccessible(true);
            return field.get(src);
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

    public static Constructor<?> getConstructorOf(Class<?> targetClass, Class<?>... constructorArgs) {
        try {
            return targetClass.getConstructor(constructorArgs);
        } catch (NoSuchMethodException e) {
            throw new ReflectionException(e.getMessage());
        }
    }

    public static Method getAnnotatedMethod(Object target, Class<? extends Annotation> annotationClass) {
        return Arrays.stream(target.getClass().getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(annotationClass))
                .findAny()
                .orElseThrow(() -> new ReflectionException("No annotation present"));
    }

    public static Object instance(Constructor<?> constructor, Object... args) {
        try{
            return constructor.newInstance(args);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ReflectionException(e.getMessage());
        }
    }
}
