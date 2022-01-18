package com.example.fastCrud.framework;

import com.example.fastCrud.framework.utils.HttpHandlerUtils;
import com.example.fastCrud.framework.utils.ReflectionUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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

    @ResponseBody
    @Override
    public Object create(HttpServletRequest request) throws IllegalAccessException, NoSuchFieldException {
        final Object requestEntity = mapEntityFromBody(request);
        ReflectionUtils.setFieldValue(requestEntity, "id", null);
        return repository.save(requestEntity);
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
    public Object update(HttpServletRequest request) throws IllegalAccessException, NoSuchFieldException {
        final Long id = getId(request);
        final Object saved = repository.findById(id).orElseThrow();
        final Object other = mapEntityFromBody(request);

        ReflectionUtils.copyFields(other, saved);
        ReflectionUtils.setFieldValue(saved, "id", id);

        repository.save(saved);
        return saved;
    }

    @ResponseBody
    @Override
    public void delete(HttpServletRequest request) {
        final Long id = getId(request);
        repository.deleteById(id);
    }

    // TODO :: NumberFormatException
    private Long getId(HttpServletRequest request) {
        return Long.parseLong(HttpHandlerUtils.getLastSegment(request));
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
