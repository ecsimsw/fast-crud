package com.ecsimsw.fastcrud;

import com.ecsimsw.fastcrud.annotation.CRUD;
import com.ecsimsw.fastcrud.annotation.CrudType;
import com.ecsimsw.fastcrud.exception.FastCrudException;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Entity;
import org.springframework.util.ClassUtils;

public class TargetEntity {

    private final Class<?> entityType;
    private final String rootPath;
    private final Class<?> repositoryType;

    public TargetEntity(Object targetObj) {
        if (!targetObj.getClass().isAnnotationPresent(Entity.class)) {
            throw new FastCrudException("CRUD annotation must be with @Entity annotation");
        }
        this.entityType = targetObj.getClass();
        var userDefinedRootPath = crud().rootPath();
        this.rootPath = userDefinedRootPath.isEmpty() ? ClassUtils.getShortNameAsProperty(entityType) : userDefinedRootPath;
        this.repositoryType = crud().repositoryType();
    }

    public List<CrudType> excludedTypes() {
        return Arrays.asList(crud().excludeType());
    }

    public String rootPath() {
        return rootPath;
    }

    public Class<?> repositoryType() {
        return repositoryType;
    }

    private CRUD crud() {
        return entityType.getAnnotation(CRUD.class);
    }

    public Class<?> entityType() {
        return entityType;
    }
}
