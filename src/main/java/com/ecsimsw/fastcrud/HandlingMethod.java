package com.ecsimsw.fastcrud;

import com.ecsimsw.fastcrud.utils.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public enum HandlingMethod {

    SAVE(CrudType.CREATE, RequestPath.post(), "save"),
    FIND_ALL(CrudType.READ, RequestPath.get(), "findAll"),
    FIND_BY_ID(CrudType.READ, RequestPath.get().additionalPath("/*"), "findById"),
    UPDATE(CrudType.UPDATE, RequestPath.put().additionalPath("/*"), "update"),
    DELETE(CrudType.DELETE, RequestPath.delete().additionalPath("/*"), "delete");

    private final CrudType crudType;
    private final RequestPath requestPath;
    private final String methodName;

    HandlingMethod(CrudType crudType, RequestPath requestPath, String methodName) {
        this.crudType = crudType;
        this.requestPath = requestPath;
        this.methodName = methodName;
    }

    public void register(RequestMappingHandlerMapping handlerMapping, String rootPath, CrudHandler crudHandler) {
        final Method method = ReflectionUtils.getMethodOf(crudHandler, methodName, HttpServletRequest.class);
        final RequestMappingInfo requestMappingInfo = requestPath.getRequestMappingInfo(rootPath);
        handlerMapping.registerMapping(requestMappingInfo, crudHandler, method);
    }

    public CrudType getCrudType() {
        return crudType;
    }
}

class RequestPath {

    private final RequestMethod requestMethod;
    private final String additionalPath;

    public static RequestPath post() {
        return new RequestPath(RequestMethod.POST);
    }

    public static RequestPath get() {
        return new RequestPath(RequestMethod.GET);
    }

    public static RequestPath put() {
        return new RequestPath(RequestMethod.PUT);
    }

    public static RequestPath delete() {
        return new RequestPath(RequestMethod.DELETE);
    }

    public RequestPath(RequestMethod requestMethod, String additionalPath) {
        this.requestMethod = requestMethod;
        this.additionalPath = additionalPath;
    }

    public RequestPath(RequestMethod requestMethod) {
        this(requestMethod, "");
    }

    public RequestPath additionalPath(String additionalPath) {
        return new RequestPath(this.requestMethod, additionalPath);
    }

    public RequestMappingInfo getRequestMappingInfo(String rootPath) {
        return RequestMappingInfo
                .paths(rootPath + additionalPath)
                .methods(requestMethod)
                .build();
    }
}
