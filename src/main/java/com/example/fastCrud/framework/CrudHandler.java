package com.example.fastCrud.framework;

import javax.servlet.http.HttpServletRequest;

public interface CrudHandler {

    Object create(HttpServletRequest request) throws IllegalAccessException;

    Object readAll(HttpServletRequest request);

    Object readById(HttpServletRequest request);

    Object update(HttpServletRequest request) throws IllegalAccessException;

    void delete(HttpServletRequest request);
}