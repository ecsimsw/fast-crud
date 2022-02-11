package com.ecsimsw.fastcrud.handler;

import javax.servlet.http.HttpServletRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public class DeleteHandler extends CrudHandlerAbst {

    public DeleteHandler(JpaRepository repository, Class<?> entityType) {
        super(repository, entityType);
    }

    @HandlingMethod
    @Override
    public ResponseEntity<?> handle(HttpServletRequest request) {
        final Long id = requestId(request);
        final Object object = getById(id);
        repository.delete(object);
        return ResponseEntity.noContent().build();
    }
}
