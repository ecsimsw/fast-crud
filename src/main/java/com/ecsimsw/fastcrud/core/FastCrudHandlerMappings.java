package com.ecsimsw.fastcrud.core;

import com.ecsimsw.fastcrud.annotation.CRUD;
import com.ecsimsw.fastcrud.core.dto.HandlerInfo;
import com.ecsimsw.fastcrud.core.dto.TargetEntityInfo;
import com.ecsimsw.fastcrud.core.handler.CrudHandlerType;
import com.ecsimsw.fastcrud.exception.FastCrudException;
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
public class FastCrudHandlerMappings {

    private static final Logger LOGGER = LoggerFactory.getLogger(FastCrudHandlerMappings.class);

    private final ApplicationContext context;
    private final RequestMappingHandlerMapping handlerMapping;

    public FastCrudHandlerMappings(
        ApplicationContext context,
        RequestMappingHandlerMapping handlerMapping
    ) {
        this.context = context;
        this.handlerMapping = handlerMapping;
    }

    @PostConstruct
    public void addMapping() {
        var targetEntities = findAllTargetEntity();
        targetEntities.forEach(target -> {
            LOGGER.info("Register fastCrud : " + target.entityType());
            var handlerInfos = handlerInfos(target);
            handlerInfos.forEach(it-> handlerMapping.registerMapping(
                it.requestMappingInfo(),
                it.handler(),
                it.method())
            );
        });
    }

    private List<TargetEntityInfo> findAllTargetEntity() {
        // TODO:: Remove component
        return Arrays.stream(context.getBeanNamesForAnnotation(CRUD.class))
            .map(context::getBean)
            .map(TargetEntityInfo::new)
            .collect(Collectors.toList());
    }

    private List<HandlerInfo> handlerInfos(TargetEntityInfo target) {
        var repository = context.getBean(target.repositoryType());
        if (!(repository instanceof JpaRepository)) {
            throw new FastCrudException("You need JpaRepository bean with" + target.entityType());
        }
        return Arrays.stream(CrudHandlerType.values())
            .filter(it -> target.contains(it.crudType()))
            .map(it -> it.handlerInfo(target, (JpaRepository) repository))
            .collect(Collectors.toList());
    }
}
