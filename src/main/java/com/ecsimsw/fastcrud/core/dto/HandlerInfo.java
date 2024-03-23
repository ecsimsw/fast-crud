package com.ecsimsw.fastcrud.core.dto;

import com.ecsimsw.fastcrud.core.handler.CrudRequestHandler;
import java.lang.reflect.Method;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.util.pattern.PathPatternParser;

public class HandlerInfo {

    private final CrudRequestHandler handler;
    private final RequestMappingInfo requestMappingInfo;

    public HandlerInfo(CrudRequestHandler handlerInstance, RequestMethod httpMethod, String requestPath) {
        this.handler = handlerInstance;
        var buildConfig = new RequestMappingInfo.BuilderConfiguration();
        buildConfig.setPathMatcher(new AntPathMatcher());
        buildConfig.setPatternParser(new PathPatternParser());
        this.requestMappingInfo = RequestMappingInfo
            .paths(requestPath)
            .methods(httpMethod)
            .options(buildConfig)
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
