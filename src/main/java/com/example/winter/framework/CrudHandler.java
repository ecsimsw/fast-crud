package com.example.winter.framework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CrudHandler {

    Object create(HttpServletRequest request);

    Object read(HttpServletRequest request, HttpServletResponse response);

    Object update(HttpServletRequest request);

    Object delete(HttpServletRequest request);
}