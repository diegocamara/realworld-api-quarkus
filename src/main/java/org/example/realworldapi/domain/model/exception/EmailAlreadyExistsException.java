package org.example.realworldapi.domain.model.exception;

public class EmailAlreadyExistsException extends BusinessException {

    public EmailAlreadyExistsException(){
        super("email already exists");
    }

}
