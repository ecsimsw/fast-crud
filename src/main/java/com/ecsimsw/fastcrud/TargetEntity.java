package com.ecsimsw.fastcrud;

import com.ecsimsw.fastcrud.annotation.CRUD;
import com.ecsimsw.fastcrud.annotation.CrudType;
import com.ecsimsw.fastcrud.exception.FastCrudException;
import com.ecsimsw.fastcrud.handler.CrudHandler;
import com.ecsimsw.fastcrud.handler.CrudHandlerImpl;
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

    // fast-crud

    public void register(RequestMappingHandlerMapping handlerMapping, JpaRepository repository) {
        handlingMethods().forEach(it -> {
            final String rootPath = rootPath();
            final CrudHandler crudHandler = new CrudHandlerImpl(repository, type);
            it.register(handlerMapping, rootPath, crudHandler);
        });
    }

    private List<HandlingMethod> handlingMethods() {
        final List<CrudType> excluded = Arrays.asList(crud().excludeType());
        return Arrays.stream(HandlingMethod.values())
                .filter(it -> !excluded.contains(it.getCrudType()))
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
