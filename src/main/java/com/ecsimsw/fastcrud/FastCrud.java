package com.ecsimsw.fastcrud;

import com.ecsimsw.fastcrud.annotation.CRUD;
import com.ecsimsw.fastcrud.annotation.CrudType;
import com.ecsimsw.fastcrud.exception.FastCrudException;
import com.ecsimsw.fastcrud.handler.HandlerInfo;
import com.ecsimsw.fastcrud.handler.Handling;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
        final RequestHandlerMappings requestHandlerMappings = requestHandlerMappings();
        targetEntities()
                .forEach(target -> requestHandlerMappings.registerAll(handlerInfos(target)));
    }

    private RequestHandlerMappings requestHandlerMappings() {
        return new RequestHandlerMappings(context.getBean(RequestMappingHandlerMapping.class));
    }

    private List<TargetEntity> targetEntities() {
        return Arrays.stream(context.getBeanNamesForAnnotation(CRUD.class))
                .map(context::getBean)
                .map(TargetEntity::new)
                .collect(Collectors.toList());
    }

    public List<HandlerInfo> handlerInfos(TargetEntity target) {
        final JpaRepository repository = jpaRepository(target.repositoryBeanName());
        return Handling.handlerInfos(target, repository);
    }

    private JpaRepository jpaRepository(String repositoryBeanName) {
        final Object repository = context.getBean(repositoryBeanName);
        if (repository instanceof JpaRepository) {
            return (JpaRepository) repository;
        }
        throw new FastCrudException("You need JpaRepository bean, name with " + repositoryBeanName);
    }
}
