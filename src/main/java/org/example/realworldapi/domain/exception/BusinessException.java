package org.example.realworldapi.domain.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(){
    }

    public BusinessException(String message){
        super(message);
    }

}
