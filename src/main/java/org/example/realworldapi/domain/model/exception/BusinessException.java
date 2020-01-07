package org.example.realworldapi.domain.model.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(){
    }

    public BusinessException(String message){
        super(message);
    }

}
