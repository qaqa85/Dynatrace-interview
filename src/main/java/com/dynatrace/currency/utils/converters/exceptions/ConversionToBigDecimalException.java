package com.dynatrace.currency.utils.converters.exceptions;

public class ConversionToBigDecimalException extends RuntimeException {
    public ConversionToBigDecimalException() {
        super("Can't convert to BigDecimal");
    }
}
