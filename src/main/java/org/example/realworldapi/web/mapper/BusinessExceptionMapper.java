package org.example.realworldapi.web.mapper;

import org.example.realworldapi.domain.exception.BusinessException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException> {

    @Override
    public Response toResponse(BusinessException e) {
        return Response.ok(e.getMessage()).status(e.getStatusCode()).build();
    }

}
