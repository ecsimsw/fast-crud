package com.example.tester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

@SpringBootApplication
public class TesterApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext ctx = SpringApplication.run(TesterApplication.class, args);
        final RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) ctx.getBean("requestMappingHandlerMapping");
        final Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        for (RequestMappingInfo info : handlerMethods.keySet()) {
            System.out.println(info + " " + handlerMethods.get(info));
        }
    }

}
