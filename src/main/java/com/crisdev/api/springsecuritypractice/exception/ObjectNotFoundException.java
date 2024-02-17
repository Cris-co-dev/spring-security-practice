package com.crisdev.api.springsecuritypractice.exception;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException() {
    }

    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}