package com.example.fastCrud.framework.backup;

import com.example.fastCrud.framework.CRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@RestController
public class ByRegisteringServlet {

    @Autowired
    private ApplicationContext context;

    @Bean
    public ServletRegistrationBean<HttpServlet> servletRegistrationBean() {
        String[] crudBeans = context.getBeanNamesForAnnotation(CRUD.class);
        String testName = crudBeans[0];
        JpaRepository repository = (JpaRepository) context.getBean(testName + "Repository");

        final HttpServlet servlet = new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                Object saved = repository.findById(1L).get();
                resp.getOutputStream().print(saved.toString());
            }
        };
        return new ServletRegistrationBean<>(servlet, "/" + testName + "/*");
    }
}
