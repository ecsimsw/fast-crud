package com.example.tester;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OtherController {

    @ResponseBody
    @GetMapping("other")
    public String hi() {
        return "other";
    }
}
