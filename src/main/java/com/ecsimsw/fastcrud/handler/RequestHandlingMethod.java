package com.ecsimsw.fastcrud.handler;

import com.ecsimsw.fastcrud.TargetEntity;
import com.ecsimsw.fastcrud.annotation.CrudType;
import com.ecsimsw.fastcrud.utils.ReflectionUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

public enum RequestHandlingMethod {

    SAVE(CrudType.CREATE, SaveHandler.class, RequestMethod.POST, ""),
    FIND_ALL(CrudType.READ, FindAllHandler.class, RequestMethod.GET, ""),
    FIND_BY_ID(CrudType.READ, FindOneHandler.class, RequestMethod.GET, "/*"),
    UPDATE(CrudType.UPDATE, UpdateHandler.class, RequestMethod.PUT, "/*"),
    DELETE(CrudType.DELETE, DeleteHandler.class, RequestMethod.DELETE, "/*");

    private final CrudType crudType;
    private final Class<?> handlerType;
    private final RequestMethod requestMethod;
    private final String additionalPath;

    RequestHandlingMethod(CrudType crudType, Class<?> handlerType, RequestMethod requestMethod, String additionalPath) {
        this.crudType = crudType;
        this.handlerType = handlerType;
        this.requestMethod = requestMethod;
        this.additionalPath = additionalPath;
    }

    public static List<HandlerInfo> handlerInfos(TargetEntity target, JpaRepository repository) {
        final List<CrudType> excludedTypes = target.excludedTypes();
        return Arrays.stream(values())
                .filter(it -> !excludedTypes.contains(it.crudType()))
                .map(it -> it.handlerInfo(target.rootPath(), repository, target.type()))
                .collect(Collectors.toList());
    }

    private HandlerInfo handlerInfo(String rootPath, JpaRepository repository, Class<?> type) {
        final Constructor<?> constructor = ReflectionUtils.getConstructorOf(handlerType, JpaRepository.class, Class.class);
        final CrudHandler crudHandler = (CrudHandler) ReflectionUtils.instance(constructor, repository, type);
        final Method method = ReflectionUtils.getAnnotatedMethod(crudHandler, HandlingMethod.class);
        return new HandlerInfo(crudHandler, method, requestMappingInfo(rootPath));
    }

    private RequestMappingInfo requestMappingInfo(String rootPath) {
        return RequestMappingInfo
                .paths(rootPath + additionalPath)
                .methods(requestMethod)
                .build();
    }

    private CrudType crudType() {
        return crudType;
    }
}
