package com.ecsimsw.fastcrud.core.handler;

import javax.servlet.http.HttpServletRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public class ReadAllRequestHandler extends CrudRequestHandler {

    public ReadAllRequestHandler(JpaRepository repository, Class<?> entityType) {
        super(repository, entityType);
    }

    @HandlingMethod
    @Override
    public ResponseEntity<?> handle(HttpServletRequest request) {
        return ResponseEntity.ok(repository.findAll());
    }
}
