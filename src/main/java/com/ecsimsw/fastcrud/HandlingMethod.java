package com.ecsimsw.fastcrud;

import com.ecsimsw.fastcrud.annotation.CrudType;
import com.ecsimsw.fastcrud.crudHandlers.FindAllHandler;
import com.ecsimsw.fastcrud.handler.CrudHandler;
import com.ecsimsw.fastcrud.utils.ReflectionUtils;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public enum HandlingMethod {

    SAVE(CrudType.CREATE, FindAllHandler.class, RequestMethod.POST),
    FIND_ALL(CrudType.READ, FindAllHandler.class, RequestMethod.GET),
    FIND_BY_ID(CrudType.READ, FindAllHandler.class, RequestMethod.GET, "/*"),
    UPDATE(CrudType.UPDATE, FindAllHandler.class, RequestMethod.PUT, "/*"),
    DELETE(CrudType.DELETE, FindAllHandler.class, RequestMethod.DELETE, "/*");

    private final CrudType crudType;
    private final Class<?> handlerType;
    private final RequestMethod requestMethod;
    private final String additionalPath;

    HandlingMethod(CrudType crudType, Class<?> handlerType, RequestMethod requestMethod, String additionalPath) {
        this.crudType = crudType;
        this.handlerType = handlerType;
        this.requestMethod = requestMethod;
        this.additionalPath = additionalPath;
    }

    HandlingMethod(CrudType crudType, Class<?> handlerType, RequestMethod requestMethod) {
        this(crudType, handlerType, requestMethod, "");
    }

    public void register(RequestMappingHandlerMapping handlerMapping, String rootPath, CrudHandler crudHandler) {
        final RequestMappingInfo requestMappingInfo = requestMappingInfo(rootPath);
        final Method method = ReflectionUtils.getMethodOf(crudHandler, methodName, HttpServletRequest.class);
        handlerMapping.registerMapping(requestMappingInfo, crudHandler, method);
    }

    public CrudType getCrudType() {
        return crudType;
    }

    public RequestMappingInfo requestMappingInfo(String rootPath) {
        return RequestMappingInfo
                .paths(rootPath + additionalPath)
                .methods(requestMethod)
                .build();
    }
}
