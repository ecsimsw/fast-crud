package com.ecsimsw.fastcrud;

import com.ecsimsw.fastcrud.annotation.CRUD;
import com.ecsimsw.fastcrud.exception.FastCrudException;
import com.ecsimsw.fastcrud.handler.HandlerInfo;
import com.ecsimsw.fastcrud.handler.RequestHandlingMethod;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class FastCrudRegister {

    private static final Logger LOGGER = LoggerFactory.getLogger(FastCrudRegister.class);

    private final ApplicationContext context;
    private final RequestHandlerMappings requestHandlerMappings;

    public FastCrudRegister(
        ApplicationContext context,
        RequestMappingHandlerMapping requestMappingHandlerMapping
    ) {
        this.context = context;
        this.requestHandlerMappings = new RequestHandlerMappings(requestMappingHandlerMapping);
    }

    @PostConstruct
    public void addMapping() {
        var targetEntities = findAllTargetEntity();
        targetEntities.forEach(target -> {
            LOGGER.info("Register fastCrud : " + target.repositoryType());
            var handlerInfos = handlerInfos(target);
            requestHandlerMappings.registerAll(handlerInfos);
        });
    }

    private List<TargetEntity> findAllTargetEntity() {
        return Arrays.stream(context.getBeanNamesForAnnotation(CRUD.class))
            .map(context::getBean)
            .map(TargetEntity::new)
            .collect(Collectors.toList());
    }

    private List<HandlerInfo> handlerInfos(TargetEntity target) {
        var repository = context.getBean(target.repositoryType());
        if (repository instanceof JpaRepository) {
            return RequestHandlingMethod.handlerInfos(target, (JpaRepository) repository);
        }
        throw new FastCrudException("You need JpaRepository bean with" + target.entityType());
    }
}
