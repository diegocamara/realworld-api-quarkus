package org.example.realworldapi.web.mapper;

import org.example.realworldapi.domain.exception.BusinessException;
import org.example.realworldapi.domain.exception.ExistingEmailException;
import org.example.realworldapi.domain.exception.InvalidPasswordException;
import org.example.realworldapi.domain.exception.UserNotFoundException;

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

        return handlerMap;
    }

    private BusinessExceptionHandler invalidPasswordExceptionHandler() {
        return ex -> unauthorizedResponse();
    }

    private BusinessExceptionHandler userNotFoundExceptionHandler() {
        return ex -> unauthorizedResponse();
    }

    private Response unauthorizedResponse() {
        return Response.ok(Response.Status.UNAUTHORIZED.name()).status(Response.Status.UNAUTHORIZED).build();
    }

    private BusinessExceptionHandler existingEmailExceptionHandler() {
        return ex -> Response.ok(Response.Status.CONFLICT.name()).status(Response.Status.CONFLICT).build();
    }

    @Override
    public Response toResponse(BusinessException businessException) {
        return this.exceptionMapper.get(businessException.getClass()).handler(businessException);
    }

    private interface BusinessExceptionHandler{
        Response handler(BusinessException ex);
    }

}
