package com.example.workflow.exception;


public class TransactionException extends RuntimeException{

    public TransactionException(String message) {
        super(message);
    }
}
