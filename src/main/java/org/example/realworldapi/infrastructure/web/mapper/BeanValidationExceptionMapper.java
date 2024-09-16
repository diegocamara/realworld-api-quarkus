package org.example.realworldapi.infrastructure.web.mapper;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.example.realworldapi.application.web.model.response.ErrorResponse;

@Provider
public class BeanValidationExceptionMapper
    implements ExceptionMapper<ConstraintViolationException> {

  @Override
  public Response toResponse(ConstraintViolationException e) {

    ErrorResponse errorResponse = new ErrorResponse();

    e.getConstraintViolations()
        .iterator()
        .forEachRemaining(
            contraint -> {
              errorResponse.getBody().add(contraint.getMessage());
            });

    return Response.ok(errorResponse).status(422).build();
  }
}
