package com.example.workflow.exception;


public class NotHandledPurchaseException extends RuntimeException {
    public NotHandledPurchaseException(String s) {
        super(s);
    }
}
