package org.example.realworldapi.web.mapper;

import org.example.realworldapi.domain.exception.BusinessException;
import org.example.realworldapi.domain.exception.ExistingEmailException;
import org.example.realworldapi.domain.exception.InvalidPasswordException;
import org.example.realworldapi.domain.exception.UserNotFoundException;
import org.example.realworldapi.web.exception.ResourceNotFoundException;
import org.example.realworldapi.web.exception.UnauthorizedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {

    private Map<Class<? extends BusinessException>, BusinessExceptionHandler> exceptionMapper;

    public BusinessExceptionMapper(){
        this.exceptionMapper = configureExceptionMapper();
    }

    private Map<Class<? extends BusinessException>, BusinessExceptionHandler> configureExceptionMapper() {

        Map<Class<? extends BusinessException>, BusinessExceptionHandler> handlerMap = new HashMap<>();

        handlerMap.put(ExistingEmailException.class, existingEmailExceptionHandler());
        handlerMap.put(UserNotFoundException.class, userNotFoundExceptionHandler());
        handlerMap.put(InvalidPasswordException.class, invalidPasswordExceptionHandler());
        handlerMap.put(ResourceNotFoundException.class, resourceNotFoundExceptionHandler());
        handlerMap.put(UnauthorizedException.class, unauthorizedExceptionHandler());

        return handlerMap;
    }

    private BusinessExceptionHandler unauthorizedExceptionHandler() {
        return ex -> unauthorizedResponse();
    }

    private BusinessExceptionHandler resourceNotFoundExceptionHandler() {
        return ex -> errorResponse(Response.Status.NOT_FOUND.name(), Response.Status.NOT_FOUND.getStatusCode());
    }

    private BusinessExceptionHandler invalidPasswordExceptionHandler() {
        return ex -> unauthorizedResponse();
    }

    private BusinessExceptionHandler userNotFoundExceptionHandler() {
        return ex -> unauthorizedResponse();
    }

    private BusinessExceptionHandler existingEmailExceptionHandler() {
        return ex -> errorResponse(Response.Status.CONFLICT.name(), Response.Status.CONFLICT.getStatusCode());
    }

    private Response unauthorizedResponse() {
        return errorResponse(Response.Status.UNAUTHORIZED.name(), Response.Status.UNAUTHORIZED.getStatusCode());
    }



    private Response errorResponse(String name, int code){
        return Response.ok(name).status(code).build();
    }

    @Override
    public Response toResponse(BusinessException businessException) {
        return this.exceptionMapper.get(businessException.getClass()).handler(businessException);
    }

    private interface BusinessExceptionHandler{
        Response handler(BusinessException ex);
    }

}
