package com.example.winter;

import com.example.winter.domain.TestEntity;
import com.example.winter.domain.TestEntityRepository;
import com.example.winter.framework.ByHandlingDispatcherServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;

@SpringBootApplication
public class WinterApplication {

    public static void main(String[] args) {
        SpringApplication.run(WinterApplication.class);
    }
}

@Component
class SampleData {

    private final TestEntityRepository testEntityRepository;

    public SampleData(TestEntityRepository testEntityRepository) {
        this.testEntityRepository = testEntityRepository;
    }

    @PostConstruct
    public void setUp() {
        testEntityRepository.save(new TestEntity("test1"));
        testEntityRepository.save(new TestEntity("test2"));
    }
}
