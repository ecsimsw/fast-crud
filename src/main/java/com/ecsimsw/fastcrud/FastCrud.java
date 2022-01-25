package com.ecsimsw.fastcrud;

import com.ecsimsw.fastcrud.exception.FastCrudException;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FastCrud {

    private final ApplicationContext context;
    private final RequestMappingHandlerMapping handlerMapping;

    public FastCrud(ApplicationContext context, RequestMappingHandlerMapping handlerMapping) {
        this.context = context;
        this.handlerMapping = handlerMapping;
    }

    @PostConstruct
    public void addMapping() {
        final String[] beanNamesForCRUD = context.getBeanNamesForAnnotation(CRUD.class);
        Arrays.stream(beanNamesForCRUD).forEach(entityName -> {
            final Class<?> aClass = crudClass(entityName);
            final CRUD crud = aClass.getAnnotation(CRUD.class);
            final String rootPath = rootPath(crud, entityName);
            final List<HandlingMethod> methods = mappingMethods(crud);
            final JpaRepository repository = jpaRepositoryBean(crud, entityName);
            register(aClass, rootPath, methods, repository);
        });
    }

    private void register(Class<?> aClass, String rootPath, List<HandlingMethod> methods, JpaRepository repository) {
        final CrudHandlerImpl crudHandler = new CrudHandlerImpl(repository, aClass);
        methods.forEach(it -> it.register(handlerMapping, rootPath, crudHandler));
    }

    private Class<?> crudClass(String entityName) {
        final Class<?> aClass = context.getBean(entityName).getClass();
        validateEntity(aClass);
        return aClass;
    }

    private void validateEntity(Class<?> aClass) {
        if (!aClass.isAnnotationPresent(Entity.class)) {
            throw new FastCrudException("Need Entity annotation");
        }
    }

    private String rootPath(CRUD crud, String entityName) {
        final String rootPath = crud.rootPath().trim();
        return rootPath.isEmpty() ? entityName : rootPath;
    }

    private List<HandlingMethod> mappingMethods(CRUD crud) {
        final List<CrudType> excluded = Arrays.asList(crud.exclude());
        return Arrays.stream(HandlingMethod.values())
                .filter(it -> !excluded.contains(it.getCrudType()))
                .collect(Collectors.toList());
    }

    private JpaRepository jpaRepositoryBean(CRUD crud, String entityName) {
        final String repositoryBeanName = repositoryBeanName(crud, entityName);
        final Object repository = context.getBean(repositoryBeanName);
        if (repository instanceof JpaRepository) {
            return (JpaRepository) repository;
        }
        throw new FastCrudException("You need JpaRepository bean, name with " + repositoryBeanName);
    }

    private String repositoryBeanName(CRUD crud, String entityName) {
        final String repoBeanName = crud.repositoryBean().trim();
        return repoBeanName.isEmpty() ? entityName + "Repository" : repoBeanName;
    }
}
