package com.cts.payment.exception;

@SuppressWarnings("serial")
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
