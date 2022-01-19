package com.example.application.presentation;

import com.example.application.domain.TestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@Controller
public class OtherController {

    @GetMapping("/other")
    @ResponseBody
    public String other() {
        return "other";
    }

    @GetMapping("/other/{id}")
    @ResponseBody
    public String otherWithId(@PathVariable Long id) {
        return "other";
    }

    @PostMapping("/other/body")
    @ResponseBody
    public String otherWithBody(@RequestBody TestEntity entity) {
        return "other";
    }
}
