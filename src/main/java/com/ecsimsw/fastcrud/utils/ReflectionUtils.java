package com.ecsimsw.fastcrud.utils;

import com.ecsimsw.fastcrud.exception.ReflectionException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionUtils {

    public static Constructor<?> getConstructorOf(Class<?> targetClass, Class<?>... constructorArgs) {
        try {
            return targetClass.getConstructor(constructorArgs);
        } catch (NoSuchMethodException e) {
            throw new ReflectionException(e.getMessage());
        }
    }

    public static Object instance(Constructor<?> constructor, Object... args) {
        try{
            return constructor.newInstance(args);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ReflectionException(e.getMessage());
        }
    }
}
