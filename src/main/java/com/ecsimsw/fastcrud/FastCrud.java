package com.ecsimsw.fastcrud;

import com.ecsimsw.fastcrud.annotation.CRUD;
import com.ecsimsw.fastcrud.exception.FastCrudException;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class FastCrud {

    private final ApplicationContext context;

    public FastCrud(ApplicationContext context) {
        this.context = context;
    }

    @PostConstruct
    public void addMapping() {
        final RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        final String[] beanNamesForCRUD = context.getBeanNamesForAnnotation(CRUD.class);
        Arrays.stream(beanNamesForCRUD).forEach(beanName -> register(handlerMapping, beanName));
    }

    private void register(RequestMappingHandlerMapping handlerMapping, String beanName) {
        final TargetEntity target = new TargetEntity(context.getBean(beanName), beanName);
        final JpaRepository repository = jpaRepositoryBean(target.repositoryBeanName());
        target.register(handlerMapping, repository);
    }

    private JpaRepository jpaRepositoryBean(String beanName) {
        final Object repository = context.getBean(beanName);
        if (repository instanceof JpaRepository) {
            return (JpaRepository) repository;
        }
        throw new FastCrudException("You need JpaRepository bean, name with " + beanName);
    }
}
