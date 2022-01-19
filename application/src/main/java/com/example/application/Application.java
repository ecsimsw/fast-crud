package com.example.application;

import com.example.application.domain.FieldObject;
import com.example.application.domain.TestEntity;
import com.example.application.domain.TestEntityRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
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
        testEntityRepository.save(new TestEntity("test1", new FieldObject("1")));
        testEntityRepository.save(new TestEntity("test2", new FieldObject("3")));
    }
}
