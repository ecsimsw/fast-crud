package com.ecsimsw.fastcrud.core.handler;

import javax.servlet.http.HttpServletRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public class DeleteRequestHandler extends CrudRequestHandler {

    public DeleteRequestHandler(JpaRepository repository, Class<?> entityType) {
        super(repository, entityType);
    }

    @HandlingMethod
    @Override
    public ResponseEntity<?> handle(HttpServletRequest request) {
        repository.deleteById(requestId(request));
        return ResponseEntity.noContent().build();
    }
}
