package com.example.application.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OtherController {

    @GetMapping("/other")
    @ResponseBody
    public String other() {
        return "other";
    }
}
