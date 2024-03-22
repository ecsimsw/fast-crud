package com.ecsimsw.fastcrud.annotation;

import com.ecsimsw.fastcrud.SharedConfigurationReference;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Import(SharedConfigurationReference.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnableCrud {
}
