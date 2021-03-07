package org.example.realworldapi.infrastructure.web.mapper;

import org.example.realworldapi.application.web.model.response.ErrorResponse;
import org.example.realworldapi.domain.model.exception.*;
import org.example.realworldapi.infrastructure.web.exception.ResourceNotFoundException;
import org.example.realworldapi.infrastructure.web.exception.UnauthorizedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {

  private final Map<Class<? extends BusinessException>, BusinessExceptionHandler> exceptionMapper;

  public BusinessExceptionMapper() {
    this.exceptionMapper = configureExceptionMapper();
  }

  private Map<Class<? extends BusinessException>, BusinessExceptionHandler>
      configureExceptionMapper() {

    Map<Class<? extends BusinessException>, BusinessExceptionHandler> handlerMap = new HashMap<>();

    handlerMap.put(EmailAlreadyExistsException.class, conflict());
    handlerMap.put(UserNotFoundException.class, notFound());
    handlerMap.put(InvalidPasswordException.class, unauthorized());
    handlerMap.put(ResourceNotFoundException.class, notFound());
    handlerMap.put(UnauthorizedException.class, unauthorized());
    handlerMap.put(UsernameAlreadyExistsException.class, conflict());
    handlerMap.put(TagNotFoundException.class, notFound());
    handlerMap.put(ArticleNotFoundException.class, notFound());
    handlerMap.put(ModelValidationException.class, unprocessableEntity());

    return handlerMap;
  }

  private BusinessExceptionHandler notFound() {
    return exceptionHandler(
        Response.Status.NOT_FOUND.name(), Response.Status.NOT_FOUND.getStatusCode());
  }

  private BusinessExceptionHandler conflict() {
    return exceptionHandler(
        Response.Status.CONFLICT.name(), Response.Status.CONFLICT.getStatusCode());
  }

  private BusinessExceptionHandler unauthorized() {
    return exceptionHandler(
        Response.Status.UNAUTHORIZED.name(), Response.Status.UNAUTHORIZED.getStatusCode());
  }

  private BusinessExceptionHandler unprocessableEntity() {
    return exceptionHandler("Unprocessable Entity", 422);
  }

  private BusinessExceptionHandler exceptionHandler(String message, int httpStatusCode) {
    return ex -> {
      var resultMessages = new LinkedList<String>();
      resultMessages.add(message);
      if (ex.messages() != null && !ex.messages().isEmpty()) {
        resultMessages = new LinkedList<>(ex.messages());
      }
      return errorResponse(resultMessages, httpStatusCode);
    };
  }

  private Response errorResponse(List<String> errors, int httpStatusCode) {
    return Response.ok(new ErrorResponse(errors)).status(httpStatusCode).build();
  }

  @Override
  public Response toResponse(BusinessException businessException) {
    return this.exceptionMapper.get(businessException.getClass()).handler(businessException);
  }

  private interface BusinessExceptionHandler {
    Response handler(BusinessException ex);
  }
}
