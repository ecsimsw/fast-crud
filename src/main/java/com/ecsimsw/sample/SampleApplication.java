package com.ecsimsw.sample;

import com.ecsimsw.fastcrud.annotation.EnableCrud;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCrud
@SpringBootApplication
public class SampleApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SampleApplication.class);
        app.run(args);
    }
}
