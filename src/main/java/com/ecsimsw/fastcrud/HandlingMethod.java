package com.ecsimsw.fastcrud;

import com.ecsimsw.fastcrud.utils.ReflectionUtils;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public enum HandlingMethod {

    SAVE(CrudType.CREATE, new RequestPath(RequestMethod.POST), "save"),
    FIND_ALL(CrudType.READ, new RequestPath(RequestMethod.GET), "findAll"),
    FIND_BY_ID(CrudType.READ, new RequestPath(RequestMethod.GET, "/*"), "findById"),
    UPDATE(CrudType.UPDATE, new RequestPath(RequestMethod.PUT, "/*"), "update"),
    DELETE(CrudType.DELETE, new RequestPath(RequestMethod.DELETE, "/*"), "delete");

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

    public RequestPath(RequestMethod requestMethod, String additionalPath) {
        this.requestMethod = requestMethod;
        this.additionalPath = additionalPath;
    }

    public RequestPath(RequestMethod requestMethod) {
        this(requestMethod, "");
    }

    public RequestMappingInfo getRequestMappingInfo(String rootPath) {
        return RequestMappingInfo
                .paths(rootPath + additionalPath)
                .methods(requestMethod)
                .build();
    }
}
