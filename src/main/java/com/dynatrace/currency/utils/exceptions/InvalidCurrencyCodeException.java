package com.dynatrace.currency.utils.exceptions;

public class InvalidCurrencyCodeException extends RuntimeException{
    public InvalidCurrencyCodeException(String message) {
        super(message);
    }
}
