package com.example.workflow.exception;

public class NotFoundRedirectException extends RuntimeException {
    public NotFoundRedirectException(String notCorrectUserOrMarketplace) {
        super(notCorrectUserOrMarketplace);
    }
}
