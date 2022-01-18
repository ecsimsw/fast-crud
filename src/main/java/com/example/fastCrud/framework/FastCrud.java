package com.example.fastCrud.framework;

import java.lang.reflect.Method;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

// TODO :: ISSUE on after Spring boot 2.6
// java.lang.IllegalArgumentException: Expected lookupPath in request attribute "org.springframework.web.util.UrlPathHelper.PATH".

@Component
public class FastCrud {

    private final ApplicationContext context;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public FastCrud(ApplicationContext context, RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.context = context;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @PostConstruct
    public void addMapping() {
        final String[] beanNamesForCRUD = context.getBeanNamesForAnnotation(CRUD.class);
        Arrays.stream(beanNamesForCRUD).forEach(name -> {
            final JpaRepository repository = (JpaRepository) context.getBean(name + "Repository");
            final Class<?> aClass = context.getBean(name).getClass();
            registerCrudMappings(name, new CrudHandlerImpl(repository, aClass));
        });
    }

    private void registerCrudMappings(String name, CrudHandler crudHandler) {
        register(api("/" + name, RequestMethod.POST), crudHandler, "create");
        register(api("/" + name, RequestMethod.GET), crudHandler, "readAll");
        register(api("/" + name + "/*", RequestMethod.GET), crudHandler, "readById");
        register(api("/" + name + "/*", RequestMethod.PUT), crudHandler, "update");
        register(api("/" + name + "/*", RequestMethod.DELETE), crudHandler, "delete");
    }

    private void register(RequestMappingInfo getMappingInfo, CrudHandler crudHandler, String methodName) {
        try {
            final Method method = crudHandler.getClass().getMethod(methodName, HttpServletRequest.class);
            requestMappingHandlerMapping.registerMapping(getMappingInfo, crudHandler, method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private RequestMappingInfo api(String path, RequestMethod requestMethod) {
        return RequestMappingInfo
                .paths(path)
                .methods(requestMethod)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
