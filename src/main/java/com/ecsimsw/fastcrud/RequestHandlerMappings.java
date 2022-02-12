package com.ecsimsw.fastcrud;

import com.ecsimsw.fastcrud.handler.HandlerInfo;
import java.util.List;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class RequestHandlerMappings {

    private final RequestMappingHandlerMapping handlerMapping;

    public RequestHandlerMappings(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    public void registerAll(List<HandlerInfo> handlerInfos) {
        handlerInfos.forEach(it-> handlerMapping.registerMapping(
                it.requestMappingInfo(),
                it.handler(),
                it.method())
        );
    }
}
