package com.example.winter.framework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CrudHandler {

    Object create(HttpServletRequest request);

    Object readAll(HttpServletRequest request);

    Object readById(HttpServletRequest request) throws Throwable;

    Object update(HttpServletRequest request);

    Object delete(HttpServletRequest request);
}