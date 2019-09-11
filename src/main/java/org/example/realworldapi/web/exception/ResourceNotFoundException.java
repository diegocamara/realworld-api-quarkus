package org.example.realworldapi.web.exception;

import org.example.realworldapi.domain.exception.BusinessException;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String message) {
//        super(404, message);
    }
}
