package com.ecsimsw.fastcrud;

import com.ecsimsw.fastcrud.annotation.CRUD;
import com.ecsimsw.fastcrud.annotation.CrudType;
import com.ecsimsw.fastcrud.exception.FastCrudException;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Entity;
import org.springframework.util.ClassUtils;

public class TargetEntity {

    private final Class<?> type;
    private final String rootPath;
    private final String repositoryBeanName;

    public TargetEntity(Object targetObj) {
        if (!targetObj.getClass().isAnnotationPresent(Entity.class)) {
            throw new FastCrudException("CRUD annotation must be with @Entity annotation");
        }
        this.type = targetObj.getClass();

        final String classShortName = ClassUtils.getShortNameAsProperty(type);
        this.rootPath = crud().rootPath().isEmpty() ? classShortName : crud().rootPath();

        final String defaultRepositoryBeanName = classShortName + "Repository";
        this.repositoryBeanName = crud().repositoryBean().isEmpty() ? defaultRepositoryBeanName : crud().repositoryBean();
    }

    public List<CrudType> excludedTypes() {
        return Arrays.asList(crud().excludeType());
    }

    public String rootPath() {
        return rootPath;
    }

    public String repositoryBeanName() {
        return repositoryBeanName;
    }

    private CRUD crud() {
        return type.getAnnotation(CRUD.class);
    }

    public Class<?> type() {
        return type;
    }
}
