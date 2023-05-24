package com.example.workflow.exception;

public class NotHandledWithdrawException extends RuntimeException {
    public NotHandledWithdrawException(String s) {
        super(s);
    }
}
