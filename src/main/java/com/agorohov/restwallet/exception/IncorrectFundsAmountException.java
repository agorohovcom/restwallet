package com.agorohov.restwallet.exception;

public class IncorrectFundsAmountException extends RuntimeException {
    public IncorrectFundsAmountException(String message) {
        super(message);
    }
}
