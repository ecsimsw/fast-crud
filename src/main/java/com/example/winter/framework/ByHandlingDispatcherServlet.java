package com.example.winter.framework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

// TODO :: ISSUE on after Spring boot 2.6
// java.lang.IllegalArgumentException: Expected lookupPath in request attribute "org.springframework.web.util.UrlPathHelper.PATH".

@Component
public class ByHandlingDispatcherServlet {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @PostConstruct
    public void addMapping() {
        final String[] beanNamesForCRUD = context.getBeanNamesForAnnotation(CRUD.class);
        Arrays.stream(beanNamesForCRUD).forEach(name -> {
            final JpaRepository repository = (JpaRepository) context.getBean(name + "Repository");
            registerCrudMappings(name, new CrudHandlerImpl(repository));
        });
    }

    private void registerCrudMappings(String name, CrudHandler crudHandler) {
        try {
            registerReadByIdMapping(name, crudHandler);
            registerReadAllMapping(name, crudHandler);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void registerReadByIdMapping(String rootPath, CrudHandler crudHandler) throws NoSuchMethodException {
        final RequestMappingInfo getMappingInfo = getRequestMappingInfo("/" + rootPath + "/*", RequestMethod.GET);
        final Method method = crudHandler.getClass().getMethod("readById", HttpServletRequest.class);
        requestMappingHandlerMapping.registerMapping(getMappingInfo, crudHandler, method);
    }

    private void registerReadAllMapping(String rootPath, CrudHandler crudHandler) throws NoSuchMethodException {
        final RequestMappingInfo getMappingInfo = getRequestMappingInfo("/" + rootPath, RequestMethod.GET);
        final Method method = crudHandler.getClass().getMethod("readAll", HttpServletRequest.class);
        requestMappingHandlerMapping.registerMapping(getMappingInfo, crudHandler, method);
    }

    private RequestMappingInfo getRequestMappingInfo(String path, RequestMethod requestMethod) {
        return RequestMappingInfo
                .paths(path)
                .methods(requestMethod)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
