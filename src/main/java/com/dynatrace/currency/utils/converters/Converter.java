package com.dynatrace.currency.utils.converters;

import com.dynatrace.currency.utils.exceptions.*;
import com.dynatrace.currency.utils.validators.Validator;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

@Slf4j
public class Converter {
    public static final String CURRENCY_CONVERSION_LOG = "Cannot convert currency code into Currency object";

    public static BigDecimal convertToBigDecimal(String string) throws ConversionToBigDecimalException {
        try {
            return new BigDecimal(string);
        } catch (NumberFormatException ex) {
            log.error(ex.getMessage());
            throw new ConversionToBigDecimalException();
        }
    }

    public static Currency convertToCurrency(String string) throws ConversionToCurrencyException {
        try {
            return Currency.getInstance(string);
        } catch (IllegalArgumentException | NullPointerException ex) {
            log.error(CURRENCY_CONVERSION_LOG);
            throw new ConversionToCurrencyException(ex.getMessage());
        }
    }

    public static Currency getCurrencyFromString(String string) {
        return Optional.ofNullable(string)
                .filter(Validator::isCurrencyCodeValid)
                .map(Converter::convertToCurrency)
                .orElseThrow(
                        () -> new InvalidCurrencyCodeException("Invalid currency code. Check ISO 4217 codes"));
    }

    public static Integer getQuotationsFromString(String string) {
        return Optional.ofNullable(string)
                .filter(Validator::isQuotationValid)
                .map(Integer::parseInt)
                .orElseThrow(
                        () -> new InvalidLastQuotationsNumberException("Quotations number out of scope [1-255]"));
    }

    public static LocalDate getLocalDateFromString(String string) {
        return Optional.ofNullable(string)
                .filter(Validator::isDateValid)
                .map(date -> LocalDate.parse(date, ISO_LOCAL_DATE))
                .orElseThrow(
                        () -> new InvalidDateException("Invalid date. Insert date in format YYYY-MM-DD")
                );
    }
}
