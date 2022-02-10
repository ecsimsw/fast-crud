package com.ecsimsw.fastcrud.handler;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

@Target(METHOD)
@Retention(RUNTIME)
@Component
public @interface HandlingMethod {
}
