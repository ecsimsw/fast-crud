package com.example.winter.domain;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public class TestEntityController {

    public ResponseEntity<String> hi() {
        return ResponseEntity.ok("hi");
    }
}
