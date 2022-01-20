package com.ecsimsw.lib;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface CrudHandler {

    ResponseEntity<?> create(HttpServletRequest request);

    ResponseEntity<?> readAll(HttpServletRequest request);

    ResponseEntity<?> readById(HttpServletRequest request);

    ResponseEntity<?> update(HttpServletRequest request);

    ResponseEntity<?> delete(HttpServletRequest request);
}