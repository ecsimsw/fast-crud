package com.ecsimsw.fastcrud.core.dto;

import com.ecsimsw.fastcrud.core.handler.CrudRequestHandler;
import java.lang.reflect.Method;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

public class HandlerInfo {

    private final CrudRequestHandler handler;
    private final RequestMappingInfo requestMappingInfo;

    public HandlerInfo(CrudRequestHandler handlerInstance, RequestMethod httpMethod, String requestPath) {
        this.handler = handlerInstance;
        this.requestMappingInfo =  RequestMappingInfo
            .paths(requestPath)
            .methods(httpMethod)
            .build();
    }

    public CrudRequestHandler handler() {
        return handler;
    }

    public Method method() {
        return handler.getHandlerMethod();
    }

    public RequestMappingInfo requestMappingInfo() {
        return requestMappingInfo;
    }
}
