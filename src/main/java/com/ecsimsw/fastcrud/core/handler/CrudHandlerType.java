package com.ecsimsw.fastcrud.core.handler;

import com.ecsimsw.fastcrud.core.dto.HandlerInfo;
import com.ecsimsw.fastcrud.core.handler.CreateRequestHandler;
import com.ecsimsw.fastcrud.core.handler.CrudRequestHandler;
import com.ecsimsw.fastcrud.core.handler.DeleteRequestHandler;
import com.ecsimsw.fastcrud.core.handler.FindOneRequestHandler;
import com.ecsimsw.fastcrud.core.handler.ReadAllRequestHandler;
import com.ecsimsw.fastcrud.core.handler.UpdateRequestHandler;
import com.ecsimsw.fastcrud.core.dto.TargetEntityInfo;
import com.ecsimsw.fastcrud.annotation.CrudType;
import com.ecsimsw.fastcrud.exception.FastCrudException;
import java.lang.reflect.InvocationTargetException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

public enum CrudHandlerType {

    SAVE(CrudType.CREATE, CreateRequestHandler.class, RequestMethod.POST, ""),
    FIND_ALL(CrudType.READ, ReadAllRequestHandler.class, RequestMethod.GET, ""),
    FIND_BY_ID(CrudType.READ, FindOneRequestHandler.class, RequestMethod.GET, "/*"),
    UPDATE(CrudType.UPDATE, UpdateRequestHandler.class, RequestMethod.PUT, "/*"),
    DELETE(CrudType.DELETE, DeleteRequestHandler.class, RequestMethod.DELETE, "/*");

    private final CrudType crudType;
    private final Class<? extends CrudRequestHandler> handlerType;
    private final RequestMethod httpMethod;
    private final String additionalPath;

    CrudHandlerType(
        CrudType crudType,
        Class<? extends CrudRequestHandler> handlerType,
        RequestMethod httpMethod,
        String additionalPath
    ) {
        this.crudType = crudType;
        this.handlerType = handlerType;
        this.httpMethod = httpMethod;
        this.additionalPath = additionalPath;
    }

    public HandlerInfo handlerInfo(TargetEntityInfo target, JpaRepository jpaRepository) {
        try {
            var constructor = handlerType.getConstructor(JpaRepository.class, Class.class);
            var handlerInstance = constructor.newInstance(jpaRepository, target.entityType());
            var requestPath = target.rootPath() + additionalPath;
            return new HandlerInfo(handlerInstance, httpMethod, requestPath);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new FastCrudException(e.getMessage());
        }
    }

    public CrudType crudType() {
        return crudType;
    }
}
