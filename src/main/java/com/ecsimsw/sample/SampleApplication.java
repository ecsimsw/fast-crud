package com.ecsimsw.sample;

import com.ecsimsw.fastcrud.annotation.EnableCrud;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@EnableCrud
@SpringBootApplication
public class SampleApplication {

    public static void main(String[] args) {
        var app = new SpringApplication(SampleApplication.class);
        app.run(args);
    }
}
