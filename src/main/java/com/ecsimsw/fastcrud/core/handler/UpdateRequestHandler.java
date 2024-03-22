package com.ecsimsw.fastcrud.core.handler;

import com.ecsimsw.fastcrud.exception.FastCrudBadRequestException;
import com.ecsimsw.fastcrud.exception.FastCrudException;
import com.ecsimsw.fastcrud.utils.HttpHandlerUtils;
import com.ecsimsw.fastcrud.utils.ReflectionUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public class UpdateRequestHandler extends CrudRequestHandler {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public UpdateRequestHandler(JpaRepository repository, Class<?> entityType) {
        super(repository, entityType);
    }

    @HandlingMethod
    @Override
    public ResponseEntity<?> handle(HttpServletRequest request) {
        var id = requestId(request);
        var requested = requestBodyToEntity(request);
        injectIdToEntityValue(requested, id);
        repository.save(requested);
        return ResponseEntity.ok(requested);
    }

    private void injectIdToEntityValue(Object requested, Long id) {
        try {
            var idField = Arrays.stream(entityType.getDeclaredFields())
                .filter(it -> it.isAnnotationPresent(Id.class))
                .findAny()
                .orElseThrow(() -> new FastCrudException("No annotation present"));
            idField.setAccessible(true);
            idField.set(requested, id);
        } catch (IllegalAccessException e) {
            throw new FastCrudException(e.getMessage());
        }
    }

    private Object requestBodyToEntity(HttpServletRequest request) {
        try {
            var requestBody = HttpHandlerUtils.getBody(request);
            return OBJECT_MAPPER.readValue(requestBody, entityType);
        } catch (IOException e) {
            throw new FastCrudBadRequestException("The input JSON structure does not match structure expected for result type");
        }
    }
}
