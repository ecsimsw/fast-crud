package com.ecsimsw.fastcrud.crudHandlers;

import com.ecsimsw.fastcrud.utils.ReflectionUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.annotation.Id;
import org.springframework.http.ResponseEntity;

public interface ICrudHandler {

    ResponseEntity<?> handle(HttpServletRequest request);
}
