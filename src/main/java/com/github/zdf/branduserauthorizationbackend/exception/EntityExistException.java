package com.github.zdf.branduserauthorizationbackend.exception;

public class EntityExistException extends RuntimeException{
    public EntityExistException() {
    }

    public EntityExistException(String message) {
        super(message);
    }
}
