package com.example.winter.framework;

import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;

public class HandlerInfo {

    private final RequestMappingInfo getMappingInfo;
    private final CrudHandler crudHandler;
    private final Method method;

    public HandlerInfo(RequestMappingInfo getMappingInfo, CrudHandler crudHandler, Method method) {
        this.getMappingInfo = getMappingInfo;
        this.crudHandler = crudHandler;
        this.method = method;
    }

    public RequestMappingInfo getGetMappingInfo() {
        return getMappingInfo;
    }

    public CrudHandler getCrudHandler() {
        return crudHandler;
    }

    public Method getMethod() {
        return method;
    }
}
