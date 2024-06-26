package com.ecsimsw.fastcrud.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
public @interface CRUD {

    String rootPath() default "";

    CrudType[] excludeType() default {};

    Class<?> repositoryType();
}
