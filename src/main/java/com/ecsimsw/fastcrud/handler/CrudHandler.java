package com.ecsimsw.fastcrud.handler;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface CrudHandler {

    @HandlingMethod
    ResponseEntity<?> handle(HttpServletRequest request);
}
