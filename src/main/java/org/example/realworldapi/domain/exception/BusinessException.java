package org.example.realworldapi.domain.exception;

public class BusinessException extends RuntimeException {

    private int statusCode;

    public BusinessException(int statusCode, String message){
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
