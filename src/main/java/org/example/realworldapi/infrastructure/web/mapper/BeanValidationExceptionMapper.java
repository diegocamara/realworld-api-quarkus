package org.example.realworldapi.infrastructure.web.mapper;

import org.example.realworldapi.infrastructure.web.model.response.ErrorResponse;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BeanValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {

        ErrorResponse errorResponse = new ErrorResponse();

        e.getConstraintViolations().iterator().forEachRemaining(contraint -> {
            errorResponse.getBody().add(contraint.getMessage());
        });

        return Response.ok(errorResponse).status(422).build();
    }

}
