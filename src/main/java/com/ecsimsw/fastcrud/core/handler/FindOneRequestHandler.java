package com.ecsimsw.fastcrud.core.handler;

import com.ecsimsw.fastcrud.exception.FastCrudBadRequestException;
import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public class FindOneRequestHandler extends CrudRequestHandler {

    public FindOneRequestHandler(JpaRepository repository, Class<?> entityType) {
        super(repository, entityType);
    }

    @HandlingMethod
    @Override
    public ResponseEntity<?> handle(HttpServletRequest request) {
        try {
            var id = requestId(request);
            var entity = repository.findById(id).orElseThrow();
            return ResponseEntity.ok(entity);
        } catch (NoSuchElementException e) {
            throw new FastCrudBadRequestException("No value present");
        }
    }
}
