package com.example.winter.framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
        try {
            final String body = HttpHandlerUtils.getBody(request);
            repository.save(OBJECT_MAPPER.readValue(body, aClass));
            return body;
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @Override
    public Object readAll(HttpServletRequest request) {
        return repository.findAll();
    }

    @ResponseBody
    @Override
    public Object readById(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final int indexOfId = requestURI.lastIndexOf('/') + 1;
        final Long id = Long.parseLong(requestURI.substring(indexOfId));
        return repository.findById(id).orElseThrow();
    }

    @ResponseBody
    @Override
    public Object update(HttpServletRequest servletRequest) {
        final Object saved = repository.findById(1L).get();
        return saved;
    }

    @ResponseBody
    @Override
    public Object delete(HttpServletRequest request) {
        final Object saved = repository.findById(1L).get();
        return saved;
    }
};
