package com.ecsimsw.fastcrud;

import com.ecsimsw.fastcrud.exception.FastCrudException;
import javax.persistence.Entity;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
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
        Arrays.stream(beanNamesForCRUD).forEach(entity -> register(entity));
    }

    private void register(String entityName) {
        final Object bean = findEntityBean(entityName);
        final String rootPath = rootPath(bean, entityName);
        final List<HandlingMethod> methods = mappingMethods(bean);
        final JpaRepository repository = jpaRepositoryBean(bean, entityName);

        final CrudHandlerImpl crudHandler = new CrudHandlerImpl(repository, bean.getClass());
        methods.forEach(it -> it.register(handlerMapping, rootPath, crudHandler));
    }

    private Object findEntityBean(String beanName) {
        final Object bean = context.getBean(beanName);
        if (!bean.getClass().isAnnotationPresent(Entity.class)) {
            throw new FastCrudException("CRUD annotation must be with @Entity annotation");
        }
        return bean;
    }

    private String rootPath(Object bean, String entityName) {
        final CRUD crud = crudAnnotation(bean);
        final String rootPath = crud.rootPath().trim();
        return rootPath.isEmpty() ? entityName : rootPath;
    }

    private List<HandlingMethod> mappingMethods(Object bean) {
        final CRUD crud = crudAnnotation(bean);
        final List<CrudType> excluded = Arrays.asList(crud.exclude());
        return Arrays.stream(HandlingMethod.values())
                .filter(it -> !excluded.contains(it.getCrudType()))
                .collect(Collectors.toList());
    }

    private JpaRepository jpaRepositoryBean(Object bean, String entityName) {
        final CRUD crud = crudAnnotation(bean);
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

    private CRUD crudAnnotation(Object bean) {
        return bean.getClass().getAnnotation(CRUD.class);
    }
}
