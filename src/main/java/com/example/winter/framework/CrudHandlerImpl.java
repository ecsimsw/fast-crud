package com.example.winter.framework;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

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
    public Object read(HttpServletRequest request, HttpServletResponse response) {
        try{
            final Object saved = repository.findById(1L).get();
            response.getOutputStream().print(saved.toString());
            return saved;
        } catch (Exception e){
            return null;
        }
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
