package com.example.workflow.exception;

public class NotFoundActorException extends RuntimeException{

    public NotFoundActorException(String message) {
        super(message);
    }
}
