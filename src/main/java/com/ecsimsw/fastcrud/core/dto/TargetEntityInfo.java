package com.ecsimsw.fastcrud.core.dto;

import com.ecsimsw.fastcrud.annotation.CRUD;
import com.ecsimsw.fastcrud.annotation.CrudType;
import com.ecsimsw.fastcrud.exception.FastCrudException;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Entity;
import org.springframework.util.ClassUtils;

public class TargetEntityInfo {

    private final Class<?> entityType;
    private final String rootPath;
    private final Class<?> repositoryType;

    public TargetEntityInfo(Object targetObj) {
        if (!targetObj.getClass().isAnnotationPresent(Entity.class)) {
            throw new FastCrudException("CRUD annotation must be with @Entity annotation");
        }
        this.entityType = targetObj.getClass();
        var crudInfo = entityType.getAnnotation(CRUD.class);
        this.rootPath = crudInfo.rootPath().isEmpty() ? ClassUtils.getShortNameAsProperty(entityType) : crudInfo.rootPath();
        this.repositoryType = crudInfo.repositoryType();
    }

    public boolean contains(CrudType type) {
        var excludes = Arrays.asList(entityType.getAnnotation(CRUD.class).excludeType());
        return !excludes.contains(type);
    }

    public String rootPath() {
        return rootPath;
    }

    public Class<?> repositoryType() {
        return repositoryType;
    }

    public Class<?> entityType() {
        return entityType;
    }
}
