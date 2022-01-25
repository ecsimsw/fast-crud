package com.ecsimsw.fastcrud;

import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Component
public @interface CRUD {

    String rootPath() default "";

    CrudType[] exclude() default {};

    String repositoryBean() default "";
}