package com.ecsimsw.fastcrud.handler;

import com.ecsimsw.fastcrud.utils.ReflectionUtils;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public class SaveHandler extends CrudHandlerAbst {

    public SaveHandler(JpaRepository repository, Class<?> entityType) {
        super(repository, entityType);
    }

    @HandlingMethod
    @Override
    public ResponseEntity<?> handle(HttpServletRequest request) {
        final Object entity = mapEntityFromBody(request);
        ReflectionUtils.setAnnotatedFieldValue(entity, Id.class, null);
        repository.save(entity);
        return ResponseEntity.ok(entity);
    }
}
