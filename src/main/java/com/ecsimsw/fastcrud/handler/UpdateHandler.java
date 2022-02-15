package com.ecsimsw.fastcrud.handler;

import com.ecsimsw.fastcrud.utils.ReflectionUtils;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public class UpdateHandler extends CrudHandlerAbst {

    public UpdateHandler(JpaRepository repository, Class<?> entityType) {
        super(repository, entityType);
    }

    @HandlingMethod
    @Override
    public ResponseEntity<?> handle(HttpServletRequest request) {
        final Long id = requestId(request);
        final Object saved = getById(id);
        final Object other = mapEntityFromBody(request);

        ReflectionUtils.copyFields(other, saved);
        ReflectionUtils.setAnnotatedFieldValue(saved, Id.class, id);

        repository.save(saved);
        return ResponseEntity.ok(saved);
    }
}
