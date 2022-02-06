package com.ecsimsw.fastcrud;

import com.ecsimsw.fastcrud.annotation.CrudType;
import com.ecsimsw.fastcrud.utils.ReflectionUtils;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public enum HandlingMethod {

    SAVE(CrudType.CREATE, "save", RequestMethod.POST),
    FIND_ALL(CrudType.READ, "findAll", RequestMethod.GET),
    FIND_BY_ID(CrudType.READ, "findById", RequestMethod.GET, "/*"),
    UPDATE(CrudType.UPDATE, "update", RequestMethod.PUT, "/*"),
    DELETE(CrudType.DELETE, "delete", RequestMethod.DELETE, "/*");

    private final CrudType crudType;
    private final String methodName;
    private final RequestMethod requestMethod;
    private final String additionalPath;

    HandlingMethod(CrudType crudType, String methodName, RequestMethod requestMethod, String additionalPath) {
        this.crudType = crudType;
        this.methodName = methodName;
        this.requestMethod = requestMethod;
        this.additionalPath = additionalPath;
    }

    HandlingMethod(CrudType crudType, String methodName, RequestMethod requestMethod) {
        this(crudType, methodName, requestMethod, "");
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
