package com.ecsimsw.fastcrud;

import com.ecsimsw.fastcrud.annotation.CRUD;
import com.ecsimsw.fastcrud.annotation.CrudType;
import com.ecsimsw.fastcrud.handler.HandlerInfo;
import com.ecsimsw.fastcrud.exception.FastCrudException;
import com.ecsimsw.fastcrud.handler.Handling;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class TargetEntity {

    private final String beanName;
    private final Class<?> type;

    public TargetEntity(Object entityBean, String beanName) {
        if (!entityBean.getClass().isAnnotationPresent(Entity.class)) {
            throw new FastCrudException("CRUD annotation must be with @Entity annotation");
        }
        this.beanName = beanName;
        this.type = entityBean.getClass();
    }

    public void register(RequestMappingHandlerMapping handlerMapping, JpaRepository repository) {
        handlingMethods().forEach(it -> {
            final HandlerInfo info = it.handlerInfo(rootPath(), repository, type);
            handlerMapping.registerMapping(info.requestMappingInfo(), info.handler(), info.method());
        });
    }

    private List<Handling> handlingMethods() {
        final List<CrudType> excluded = Arrays.asList(crud().excludeType());
        return Arrays.stream(Handling.values())
                .filter(it -> !excluded.contains(it.crudType()))
                .collect(Collectors.toList());
    }

    private String rootPath() {
        final String userDefinedPath = crud().rootPath();
        if (userDefinedPath.isEmpty()) {
            return beanName;
        }
        return userDefinedPath;
    }

    public String repositoryBeanName() {
        final String defaultName = beanName + "Repository";
        final String userDefinedName = crud().repositoryBean();
        return userDefinedName.isEmpty() ? defaultName : userDefinedName;
    }

    private CRUD crud() {
        return type.getAnnotation(CRUD.class);
    }
}
