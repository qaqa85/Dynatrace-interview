package com.dynatrace.currency.nbpClient.exceptions;

public class ConversionToBigDecimalException extends RuntimeException {
    public ConversionToBigDecimalException() {
        super("Can't convert to BigDecimal");
    }
}
