package org.example.realworldapi.domain.exception;

public class UnauthorizedException extends BusinessException{

    public UnauthorizedException() {
        super(401, "Unauthorized");
    }

}
