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
            final Class<?> aClass = context.getBean(name).getClass();
            registerCrudMappings(name, new CrudHandlerImpl(repository, aClass));
        });
    }

    private void registerCrudMappings(String name, CrudHandler crudHandler) {
        register(createMappingInfo(name, crudHandler));
        register(readByIdMappingInfo(name, crudHandler));
        register(readAllMappingInfo(name, crudHandler));
        register(updateMapping(name, crudHandler));
        register(deleteMapping(name, crudHandler));
    }

    private void register(HandlerInfo handlerInfo) {
        requestMappingHandlerMapping.registerMapping(
                handlerInfo.getGetMappingInfo(),
                handlerInfo.getCrudHandler(),
                handlerInfo.getMethod()
        );
    }

    private HandlerInfo createMappingInfo(String rootPath, CrudHandler crudHandler) {
        return getHandlerInfo(
                getRequestMappingInfo("/" + rootPath, RequestMethod.POST),
                crudHandler,
                "create"
        );
    }

    private HandlerInfo readByIdMappingInfo(String rootPath, CrudHandler crudHandler) {
        return getHandlerInfo(
                getRequestMappingInfo("/" + rootPath + "/*", RequestMethod.GET),
                crudHandler,
                "readById"
        );
    }

    private HandlerInfo readAllMappingInfo(String rootPath, CrudHandler crudHandler) {
        return getHandlerInfo(
                getRequestMappingInfo("/" + rootPath, RequestMethod.GET),
                crudHandler,
                "readAll"
        );
    }

    private HandlerInfo updateMapping(String rootPath, CrudHandler crudHandler) {
        return getHandlerInfo(
                getRequestMappingInfo("/" + rootPath + "/*", RequestMethod.PUT),
                crudHandler,
                "update"
        );
    }

    private HandlerInfo deleteMapping(String rootPath, CrudHandler crudHandler) {
        return getHandlerInfo(
                getRequestMappingInfo("/" + rootPath + "/*", RequestMethod.DELETE),
                crudHandler,
                "delete"
        );
    }

    private HandlerInfo getHandlerInfo(RequestMappingInfo mappingInfo, CrudHandler crudHandler, String handlerName) {
        try {
            return new HandlerInfo(
                    mappingInfo,
                    crudHandler,
                    crudHandler.getClass().getMethod(handlerName, HttpServletRequest.class)
            );
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    private RequestMappingInfo getRequestMappingInfo(String path, RequestMethod requestMethod) {
        return RequestMappingInfo
                .paths(path)
                .methods(requestMethod)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
