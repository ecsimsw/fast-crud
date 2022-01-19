package com.example.framework;

import javax.servlet.http.HttpServletRequest;

public interface CrudHandler {

    Object create(HttpServletRequest request) throws IllegalAccessException, NoSuchFieldException;

    Object readAll(HttpServletRequest request);

    Object readById(HttpServletRequest request);

    Object update(HttpServletRequest request) throws IllegalAccessException, NoSuchFieldException;

    void delete(HttpServletRequest request);
}