package com.example.workflow.exception;

public class NotFoundPaymentMethodException extends RuntimeException {
    public NotFoundPaymentMethodException(String s) {
        super(s);
    }
}
