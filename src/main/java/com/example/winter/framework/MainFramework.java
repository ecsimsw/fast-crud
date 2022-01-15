package com.example.winter.framework;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainFramework {

    @Autowired
    private ApplicationContext context;

    @Bean
    public ServletRegistrationBean<HttpServlet> foo() {
        String[] crudBeans = context.getBeanNamesForAnnotation(CRUD.class);
        String testName = crudBeans[0];
        JpaRepository repository = (JpaRepository) context.getBean(testName + "Repository");

        final HttpServlet servlet = new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                System.out.println(req.getContextPath());
                System.out.println(req.getServletPath());
                System.out.println(req.getPathInfo());
                System.out.println(req.getRequestURI());
                System.out.println(req.getRequestURL());

                Object saved = repository.findById(1L).get();
                resp.getOutputStream().print(saved.toString());
            }
        };
        return new ServletRegistrationBean<>(servlet, "/"+testName+"/*");
    }
}
