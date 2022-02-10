package com.ecsimsw.fastcrud.handler;

import javax.servlet.http.HttpServletRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public class FindAllHandler extends CrudHandlerAbst {

    public FindAllHandler(JpaRepository repository, Class<?> entityType) {
        super(repository, entityType);
    }

    @HandlingMethod
    @Override
    public ResponseEntity<?> handle(HttpServletRequest request) {
        return ResponseEntity.ok(repository.findAll());
    }
}
