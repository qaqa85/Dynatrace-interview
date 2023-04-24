package com.dynatrace.currency.utils.exceptions;

public class ConversionToBigDecimalException extends RuntimeException {
    public ConversionToBigDecimalException() {
        super("Can't convert to BigDecimal");
    }
}
