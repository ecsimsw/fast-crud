package com.ecsimsw.fastcrud.crudHandlers;

import javax.servlet.http.HttpServletRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public class FindAllHandler extends CrudHandler {

    public FindAllHandler(JpaRepository repository, Class<?> entityType) {
        super(repository, entityType);
    }

    @Override
    public ResponseEntity<?> handle(HttpServletRequest request) {
        return ResponseEntity.ok(repository.findAll());
    }
}
