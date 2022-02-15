package com.ecsimsw.fastcrud.handler;

import com.ecsimsw.fastcrud.exception.BadRequestException;
import com.ecsimsw.fastcrud.utils.HttpHandlerUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public abstract class CrudHandlerAbst implements CrudHandler {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    protected final JpaRepository repository;
    protected final Class<?> entityType;

    public CrudHandlerAbst(JpaRepository repository, Class<?> entityType) {
        this.repository = repository;
        this.entityType = entityType;
    }

    public abstract ResponseEntity<?> handle(HttpServletRequest request);

    protected Long requestId(HttpServletRequest request) {
        try {
            return Long.parseLong(HttpHandlerUtils.getLastSegment(request));
        } catch (NumberFormatException e) {
            throw new BadRequestException("Id must be Long type : the last segment of path has to be a parsable long");
        }
    }

    protected Object getById(Long id) {
        try {
            return repository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new BadRequestException("No value present");
        }
    }

    protected Object mapEntityFromBody(HttpServletRequest request) {
        try {
            final String body = HttpHandlerUtils.getBody(request);
            return OBJECT_MAPPER.readValue(body, entityType);
        } catch (IOException e) {
            throw new BadRequestException("The input JSON structure does not match structure expected for result type");
        }
    }
}
