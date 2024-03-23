package com.ecsimsw.sample;

import com.ecsimsw.fastcrud.annotation.EnableCrud;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@EnableCrud
@SpringBootApplication
public class SampleApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SampleApplication.class);
        ConfigurableApplicationContext run = app.run(args);
        String[] beanDefinitionNames = run.getBeanDefinitionNames();
        for(var name : beanDefinitionNames) {
            System.out.println(name);
        }
    }
}
