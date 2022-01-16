package com.example.winter;

import com.example.winter.domain.TestEntity;
import com.example.winter.framework.ByHandlingDispatcherServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

@SpringBootApplication
public class WinterApplication {

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(WinterApplication.class);
        ConfigurableApplicationContext ctx = application.run(args);
        JpaRepository testEntityRepository = (JpaRepository) ctx.getBean("testEntityRepository");
        testEntityRepository.save(new TestEntity("test1"));

        try{
            ByHandlingDispatcherServlet fw = (ByHandlingDispatcherServlet) ctx.getBean("byHandlingDispatcherServlet");
            fw.addMapping();
        }catch (Exception e) {

        }
    }
}
