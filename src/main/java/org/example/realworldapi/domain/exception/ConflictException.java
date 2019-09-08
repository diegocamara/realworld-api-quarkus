package org.example.realworldapi.domain.exception;

public class ConflictException extends BusinessException{

    public ConflictException() {
        super(409, "Conflict");
    }

}
