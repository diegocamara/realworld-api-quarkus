package org.example.realworldapi.domain.model.exception;

public class UsernameAlreadyExistsException extends BusinessException {

    public UsernameAlreadyExistsException(){
        super("username already exists");
    }

}
