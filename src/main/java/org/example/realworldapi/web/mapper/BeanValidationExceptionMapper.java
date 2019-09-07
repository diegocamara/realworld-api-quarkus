package org.example.realworldapi.web.mapper;

import org.example.realworldapi.web.dto.BeanValidationResponseDTO;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BeanValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {

        BeanValidationResponseDTO beanValidationResponseDTO = new BeanValidationResponseDTO();

        e.getConstraintViolations().iterator().forEachRemaining(contraint -> {
            beanValidationResponseDTO.getBody().add(contraint.getMessage());
        });

        return Response.ok(beanValidationResponseDTO).status(422).build();
    }

}
