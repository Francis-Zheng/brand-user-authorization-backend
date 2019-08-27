package com.github.zdf.branduserauthorizationbackend.exception;

public class EntityNotExitException extends RuntimeException{
    public EntityNotExitException() {
    }

    public EntityNotExitException(String message) {
        super(message);
    }
}
