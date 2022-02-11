package com.ecsimsw.fastcrud.handler;

import com.ecsimsw.fastcrud.handler.CrudHandlerAbst;
import com.ecsimsw.fastcrud.handler.HandlingMethod;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public class FindOneHandler extends CrudHandlerAbst {

    public FindOneHandler(JpaRepository repository, Class<?> entityType) {
        super(repository, entityType);
    }

    @HandlingMethod
    @Override
    public ResponseEntity<?> handle(HttpServletRequest request) {
        final Long id = requestId(request);
        final Object entity = getById(id);
        return ResponseEntity.ok(entity);
    }
}
