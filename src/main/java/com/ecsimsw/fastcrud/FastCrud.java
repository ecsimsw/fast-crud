package com.ecsimsw.fastcrud;

import com.ecsimsw.fastcrud.annotation.CRUD;
import com.ecsimsw.fastcrud.exception.FastCrudException;
import com.ecsimsw.fastcrud.handler.HandlerInfo;
import com.ecsimsw.fastcrud.handler.RequestHandlingMethod;
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
    private final RequestHandlerMappings requestHandlerMappings;

    public FastCrud(ApplicationContext context, RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.context = context;
        this.requestHandlerMappings = new RequestHandlerMappings(requestMappingHandlerMapping);
    }

    @PostConstruct
    public void addMapping() {
        final List<TargetEntity> targetEntities = targetEntities();
        targetEntities.forEach(target -> requestHandlerMappings.registerAll(handlerInfos(target)));
    }

    private List<TargetEntity> targetEntities() {
        return Arrays.stream(context.getBeanNamesForAnnotation(CRUD.class))
                .map(context::getBean)
                .map(TargetEntity::new)
                .collect(Collectors.toList());
    }

    private List<HandlerInfo> handlerInfos(TargetEntity target) {
        final JpaRepository repository = jpaRepository(target.repositoryBeanName());
        return RequestHandlingMethod.handlerInfos(target, repository);
    }

    private JpaRepository jpaRepository(String repositoryBeanName) {
        final Object repository = context.getBean(repositoryBeanName);
        if (repository instanceof JpaRepository) {
            return (JpaRepository) repository;
        }
        throw new FastCrudException("You need JpaRepository bean, name with " + repositoryBeanName);
    }
}
