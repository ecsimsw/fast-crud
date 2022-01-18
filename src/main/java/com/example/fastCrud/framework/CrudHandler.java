package com.example.fastCrud.framework;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface CrudHandler {

    Object create(HttpServletRequest request) throws IllegalAccessException, NoSuchFieldException;

    Object readAll(HttpServletRequest request);

    Object readById(HttpServletRequest request);

    Object update(HttpServletRequest request) throws IllegalAccessException, NoSuchFieldException;

    void delete(HttpServletRequest request);
}