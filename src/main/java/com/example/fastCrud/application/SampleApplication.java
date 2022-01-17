package com.example.fastCrud.application;

import com.example.fastCrud.application.domain.TestEntity;
import com.example.fastCrud.application.domain.TestEntityRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class SampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class);
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
