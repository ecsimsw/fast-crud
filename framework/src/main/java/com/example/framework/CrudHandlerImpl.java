package com.example.framework;

import com.example.framework.exception.BadRequestException;
import com.example.framework.utils.HttpHandlerUtils;
import com.example.framework.utils.ReflectionUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final Class<?> aClass;

    public CrudHandlerImpl(JpaRepository repository, Class<?> aClass) {
        this.repository = repository;
        this.aClass = aClass;
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @Override
    public ResponseEntity<?> create(HttpServletRequest request) {
        final Object entity = mapEntityFromBody(request);
        ReflectionUtils.setFieldValue(entity, "id", null);
        repository.save(entity);
        return ResponseEntity.ok(entity);
    }

    @Override
    public ResponseEntity<?> readAll(HttpServletRequest request) {
        return ResponseEntity.ok(repository.findAll());
    }

    @Override
    public ResponseEntity<?> readById(HttpServletRequest request) {
        final Long id = getId(request);
        final Object entity = getById(id);
        return ResponseEntity.ok(entity);
    }

    @Override
    public ResponseEntity<?> update(HttpServletRequest request) {
        final Long id = getId(request);
        final Object saved = getById(id);
        final Object other = mapEntityFromBody(request);

        ReflectionUtils.copyFields(other, saved);
        ReflectionUtils.setFieldValue(saved, "id", id);

        repository.save(saved);
        return ResponseEntity.ok(saved);
    }

    @Override
    public ResponseEntity<?> delete(HttpServletRequest request) {
        final Long id = getId(request);
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Long getId(HttpServletRequest request) {
        try {
            return Long.parseLong(HttpHandlerUtils.getLastSegment(request));
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id must be Long type : the last segment of path has to be a parsable long");
        }
    }

    public Object getById(Long id) {
        try {
            return repository.findById(id).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new BadRequestException("No value present");
        }
    }

    private Object mapEntityFromBody(HttpServletRequest request) {
        try {
            final String body = HttpHandlerUtils.getBody(request);
            return OBJECT_MAPPER.readValue(body, aClass);
        } catch (IOException e) {
            throw new BadRequestException("The input JSON structure does not match structure expected for result type");
        }
    }
}
