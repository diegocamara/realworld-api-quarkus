package org.example.realworldapi.web.mapper;

import org.example.realworldapi.web.dto.ErrorResponseDTO;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BeanValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();

        e.getConstraintViolations().iterator().forEachRemaining(contraint -> {
            errorResponseDTO.getBody().add(contraint.getMessage());
        });

        return Response.ok(errorResponseDTO).status(422).build();
    }

}
