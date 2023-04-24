package com.dynatrace.currency.nbpClient.exceptions;

public class InvalidCurrencyCodeException extends RuntimeException {
    public InvalidCurrencyCodeException(String message) {
        super(message);
    }
}
