package com.github.zdf.branduserauthorizationbackend.exception;

public class EntityNotExistException extends RuntimeException{
    public EntityNotExistException() {
    }

    public EntityNotExistException(String message) {
        super(message);
    }
}
