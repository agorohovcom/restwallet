package com.agorohov.restwallet.exception;

public class TooLargeBalanceException extends RuntimeException {
    public TooLargeBalanceException(String message) {
        super(message);
    }
}
