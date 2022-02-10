package com.ecsimsw.fastcrud.handler;

import java.lang.reflect.Method;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

public class HandlerInfo {

    private final CrudHandler handler;
    private final Method method;
    private final RequestMappingInfo requestMappingInfo;

    public HandlerInfo(RequestMappingInfo requestMappingInfo, CrudHandler handler, Method method) {
        this.handler = handler;
        this.method = method;
        this.requestMappingInfo = requestMappingInfo;
    }

    public CrudHandler handler() {
        return handler;
    }

    public Method method() {
        return method;
    }

    public RequestMappingInfo requestMappingInfo() {
        return requestMappingInfo;
    }
}
