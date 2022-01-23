package com.example.fastcrudjitpack;

import com.example.fastcrudjitpack.exception.FastCrudException;
import com.example.fastcrudjitpack.exception.ReflectionException;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// TODO :: ISSUE on after Spring boot 2.6
// java.lang.IllegalArgumentException: Expected lookupPath in request attribute "org.springframework.web.util.UrlPathHelper.PATH".

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
        Arrays.stream(beanNamesForCRUD).forEach(name -> {
            final Class<?> aClass = crudClass(name);
            final String rootPath = rootPath(aClass, name);
            final List<CrudMethod> methods = mappingMethods(aClass);
            registerCrudMappings(rootPath, methods, crudHandler(name, aClass));
        });
    }

    private Class<?> crudClass(String beanName) {
        final Class<?> aClass = context.getBean(beanName).getClass();
        validateEntity(aClass);
        return aClass;
    }

    private void validateEntity(Class<?> aClass) {
        if (!aClass.isAnnotationPresent(Entity.class)) {
            throw new FastCrudException("Need Entity annotation");
        }
    }

    private String rootPath(Class<?> aClass, String name) {
        final String rootPath = aClass.getAnnotation(CRUD.class).rootPath().trim();
        return rootPath.isEmpty() ? name : rootPath;
    }

    private List<CrudMethod> mappingMethods(Class<?> aClass) {
        final List<CrudMethod> excluded = Arrays.asList(aClass.getAnnotation(CRUD.class).exclude());
        return Arrays.stream(CrudMethod.values())
                .filter(it -> !excluded.contains(it))
                .collect(Collectors.toList());
    }

    private CrudHandler crudHandler(String beanName, Class<?> aClass) {
        final JpaRepository repositoryBean = repositoryBean(beanName);
        return new CrudHandlerImpl(repositoryBean, aClass);
    }

    private JpaRepository repositoryBean(String beanName) {
        final Object repository = context.getBean(beanName + "Repository");
        if (repository instanceof Repository) {
            return (JpaRepository) repository;
        }
        throw new FastCrudException("You need JpaRepository bean, name with \'" + beanName + "Repository\'");
    }

    private void registerCrudMappings(String rootPath, List<CrudMethod> methods, CrudHandler crudHandler) {
        if (methods.contains(CrudMethod.CREATE)) {
            register(api(rootPath, RequestMethod.POST), crudHandler, "create");
        }
        if (methods.contains(CrudMethod.READ)) {
            register(api(rootPath, RequestMethod.GET), crudHandler, "readAll");
            register(api(rootPath + "/*", RequestMethod.GET), crudHandler, "readById");
        }
        if (methods.contains(CrudMethod.UPDATE)) {
            register(api(rootPath + "/*", RequestMethod.PUT), crudHandler, "update");
        }
        if (methods.contains(CrudMethod.DELETE)) {
            register(api(rootPath + "/*", RequestMethod.DELETE), crudHandler, "delete");
        }
    }

    private void register(RequestMappingInfo getMappingInfo, CrudHandler crudHandler, String methodName) {
        try {
            final Method method = crudHandler.getClass().getMethod(methodName, HttpServletRequest.class);
            handlerMapping.registerMapping(getMappingInfo, crudHandler, method);
        } catch (NoSuchMethodException e) {
            throw new ReflectionException(e.getMessage());
        }
    }

    private RequestMappingInfo api(String path, RequestMethod requestMethod) {
        return RequestMappingInfo
                .paths(path)
                .methods(requestMethod)
                .build();
    }
}
