package com.ecsimsw.fastcrud.handler;

import com.ecsimsw.fastcrud.annotation.CrudType;
import com.ecsimsw.fastcrud.handler.DeleteHandler;
import com.ecsimsw.fastcrud.handler.FindAllHandler;
import com.ecsimsw.fastcrud.handler.FindOneHandler;
import com.ecsimsw.fastcrud.handler.HandlerInfo;
import com.ecsimsw.fastcrud.handler.HandlingMethod;
import com.ecsimsw.fastcrud.handler.CrudHandler;
import com.ecsimsw.fastcrud.exception.FastCrudException;
import com.ecsimsw.fastcrud.handler.SaveHandler;
import com.ecsimsw.fastcrud.handler.UpdateHandler;
import com.ecsimsw.fastcrud.utils.ReflectionUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

public enum Handling {

    SAVE(CrudType.CREATE, SaveHandler.class, RequestMethod.POST),
    FIND_ALL(CrudType.READ, FindAllHandler.class, RequestMethod.GET),
    FIND_BY_ID(CrudType.READ, FindOneHandler.class, RequestMethod.GET, "/*"),
    UPDATE(CrudType.UPDATE, UpdateHandler.class, RequestMethod.PUT, "/*"),
    DELETE(CrudType.DELETE, DeleteHandler.class, RequestMethod.DELETE, "/*");

    private final CrudType crudType;
    private final Class<?> handlerType;
    private final RequestMethod requestMethod;
    private final String additionalPath;

    Handling(CrudType crudType, Class<?> handlerType, RequestMethod requestMethod, String additionalPath) {
        this.crudType = crudType;
        this.handlerType = handlerType;
        this.requestMethod = requestMethod;
        this.additionalPath = additionalPath;
    }

    Handling(CrudType crudType, Class<?> handlerType, RequestMethod requestMethod) {
        this(crudType, handlerType, requestMethod, "");
    }

    public HandlerInfo handlerInfo(String rootPath, JpaRepository repository, Class<?> type) {
        try {
            final Constructor<?> constructor = ReflectionUtils.getConstructorOf(handlerType, JpaRepository.class, Class.class);
            final CrudHandler crudHandler = (CrudHandler) constructor.newInstance(repository, type);
            final Method method = ReflectionUtils.getAnnotatedMethod(crudHandler, HandlingMethod.class);
            return new HandlerInfo(requestMappingInfo(rootPath), crudHandler, method);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new FastCrudException(e.getMessage());
        }
    }

    private RequestMappingInfo requestMappingInfo(String rootPath) {
        return RequestMappingInfo
                .paths(rootPath + additionalPath)
                .methods(requestMethod)
                .build();
    }

    public CrudType crudType() {
        return crudType;
    }
}
