package com.ecsimsw.fastcrud;

import java.lang.annotation.*;
import org.springframework.context.annotation.Import;

@Import(SharedConfigurationReference.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnableCrud {


}
