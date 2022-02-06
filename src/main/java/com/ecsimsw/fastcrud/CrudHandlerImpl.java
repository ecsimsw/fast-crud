package com.ecsimsw.fastcrud;

import com.ecsimsw.fastcrud.exception.BadRequestException;
import com.ecsimsw.fastcrud.utils.HttpHandlerUtils;
import com.ecsimsw.fastcrud.utils.ReflectionUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.NoSuchElementException;

public class CrudHandlerImpl implements CrudHandler {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private final JpaRepository repository;
    private final Class<?> entityType;

    public CrudHandlerImpl(JpaRepository repository, Class<?> entityType) {
        this.repository = repository;
        this.entityType = entityType;
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @Override
    public ResponseEntity<?> save(HttpServletRequest request) {
        final Object entity = mapEntityFromBody(request);
        ReflectionUtils.setAnnotatedFieldValue(entity, Id.class, null);
        repository.save(entity);
        return ResponseEntity.ok(entity);
    }

    @Override
    public ResponseEntity<?> findAll(HttpServletRequest request) {
        return ResponseEntity.ok(repository.findAll());
    }

    @Override
    public ResponseEntity<?> findById(HttpServletRequest request) {
        final Long id = requestId(request);
        final Object entity = getById(id);
        return ResponseEntity.ok(entity);
    }

    @Override
    public ResponseEntity<?> update(HttpServletRequest request) {
        final Long id = requestId(request);
        final Object saved = getById(id);
        final Object other = mapEntityFromBody(request);

        ReflectionUtils.copyFields(other, saved);
        ReflectionUtils.setAnnotatedFieldValue(saved, Id.class, id);

        repository.save(saved);
        return ResponseEntity.ok(saved);
    }

    @Override
    public ResponseEntity<?> delete(HttpServletRequest request) {
        final Long id = requestId(request);
        final Object object = getById(id);
        repository.delete(object);
        return ResponseEntity.noContent().build();
    }

    private Long requestId(HttpServletRequest request) {
        try {
            return Long.parseLong(HttpHandlerUtils.getLastSegment(request));
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id must be Long type : the last segment of path has to be a parsable long");
        }
    }

    private Object getById(Long id) {
        try {
            return repository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new BadRequestException("No value present");
        }
    }

    private Object mapEntityFromBody(HttpServletRequest request) {
        try {
            final String body = HttpHandlerUtils.getBody(request);
            return OBJECT_MAPPER.readValue(body, entityType);
        } catch (IOException e) {
            throw new BadRequestException("The input JSON structure does not match structure expected for result type");
        }
    }
}
