package com.example.fastCrud.framework;

import com.example.fastCrud.framework.utils.HttpHandlerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;

public class CrudHandlerImpl implements CrudHandler {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final JpaRepository repository;
    private final Class<?> aClass;

    public CrudHandlerImpl(JpaRepository repository, Class<?> aClass) {
        this.repository = repository;
        this.aClass = aClass;
    }

    @ResponseBody
    @Override
    public Object create(HttpServletRequest request) {
        return repository.save(mapEntityFromBody(request));
    }

    @ResponseBody
    @Override
    public Object readAll(HttpServletRequest request) {
        return repository.findAll();
    }

    @ResponseBody
    @Override
    public Object readById(HttpServletRequest request) {
        final Long id = getId(request);
        return repository.findById(id).orElseThrow();
    }

    @ResponseBody
    @Override
    public Object update(HttpServletRequest request) throws IllegalAccessException {
        final Long id = getId(request);
        final Object saved = repository.findById(id).orElseThrow();
        final Object other = mapEntityFromBody(request);

        for (Field field : saved.getClass().getDeclaredFields()) {
            if (field.getName().equals("id")) {
                continue;
            }
            field.setAccessible(true);
            field.set(saved, field.get(other));
        }
        repository.save(saved);
        return other;
    }

    @ResponseBody
    @Override
    public void delete(HttpServletRequest request) {
        final Long id = getId(request);
        repository.deleteById(id);
    }

    private Long getId(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final int indexOfId = requestURI.lastIndexOf('/') + 1;
        return Long.parseLong(requestURI.substring(indexOfId));
    }

    private Object mapEntityFromBody(HttpServletRequest request) {
        try {
            final String body = HttpHandlerUtils.getBody(request);
            return OBJECT_MAPPER.readValue(body, aClass);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        }
    }
}
