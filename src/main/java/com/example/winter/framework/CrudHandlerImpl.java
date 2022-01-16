package com.example.winter.framework;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

public class CrudHandlerImpl implements CrudHandler {

    private final JpaRepository repository;

    public CrudHandlerImpl(JpaRepository repository) {
        this.repository = repository;
    }

    @ResponseBody
    @Override
    public Object create(HttpServletRequest servletRequest) {
        final Object saved = repository.findById(1L).get();
        return saved;
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
    public Object delete(HttpServletRequest servletRequest) {
        final Object saved = repository.findById(1L).get();
        return saved;
    }
};
