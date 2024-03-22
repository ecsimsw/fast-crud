package com.ecsimsw.fastcrud.core.handler;

import com.ecsimsw.fastcrud.exception.FastCrudBadRequestException;
import com.ecsimsw.fastcrud.exception.FastCrudException;
import java.lang.reflect.Method;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public abstract class CrudRequestHandler {

    protected final JpaRepository repository;
    protected final Class<?> entityType;

    public CrudRequestHandler(JpaRepository repository, Class<?> entityType) {
        this.repository = repository;
        this.entityType = entityType;
    }

    public abstract ResponseEntity<?> handle(HttpServletRequest request);

    public Method getHandlerMethod() {
        return Arrays.stream(this.getClass().getDeclaredMethods())
            .filter(it -> it.isAnnotationPresent(HandlingMethod.class))
            .findAny()
            .orElseThrow(() -> new FastCrudException("No annotation present"));
    }

    protected Long requestId(HttpServletRequest request) {
        try {
            var requestURI = request.getRequestURI();
            var indexOfId = requestURI.lastIndexOf('/') + 1;
            return Long.parseLong(requestURI.substring(indexOfId));
        } catch (NumberFormatException e) {
            throw new FastCrudBadRequestException("Id must be Long type : the last segment of path has to be a parsable long");
        }
    }
}
