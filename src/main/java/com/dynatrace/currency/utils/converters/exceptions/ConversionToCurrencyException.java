package com.dynatrace.currency.utils.converters.exceptions;

public class ConversionToCurrencyException extends RuntimeException {
    public ConversionToCurrencyException(String message) {
        super(message);
    }
}
