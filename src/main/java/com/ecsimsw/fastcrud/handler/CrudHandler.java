package com.ecsimsw.fastcrud.handler;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface CrudHandler {

    ResponseEntity<?> save(HttpServletRequest request);

    ResponseEntity<?> findAll(HttpServletRequest request);

    ResponseEntity<?> findById(HttpServletRequest request);

    ResponseEntity<?> update(HttpServletRequest request);

    ResponseEntity<?> delete(HttpServletRequest request);
}