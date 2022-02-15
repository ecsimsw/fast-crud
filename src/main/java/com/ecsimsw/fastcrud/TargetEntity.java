package com.ecsimsw.fastcrud;

import com.ecsimsw.fastcrud.annotation.CRUD;
import com.ecsimsw.fastcrud.annotation.CrudType;
import com.ecsimsw.fastcrud.exception.FastCrudException;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Entity;
import org.springframework.util.ClassUtils;

public class TargetEntity {

    private final static String POSTFIX_REPOSITORY_BEAN_NAME = "Repository";

    private final Class<?> type;
    private final String rootPath;
    private final String repositoryBeanName;

    public TargetEntity(Object targetObj) {
        if (!targetObj.getClass().isAnnotationPresent(Entity.class)) {
            throw new FastCrudException("CRUD annotation must be with @Entity annotation");
        }
        this.type = targetObj.getClass();

        final String classShortName = ClassUtils.getShortNameAsProperty(type);
        this.rootPath = rootPath(classShortName);
        this.repositoryBeanName = repositoryBeanName(classShortName);
    }

    private String rootPath(String classShortName) {
        final String userDefinedRootPath = crud().rootPath();
        return userDefinedRootPath.isEmpty() ? classShortName : userDefinedRootPath;
    }

    private String repositoryBeanName(String classShortName) {
        final String defaultRepositoryBeanName = classShortName + POSTFIX_REPOSITORY_BEAN_NAME;
        final String userDefinedRepositoryBeanName = crud().repositoryBean();
        return userDefinedRepositoryBeanName.isEmpty() ? defaultRepositoryBeanName : userDefinedRepositoryBeanName;
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
