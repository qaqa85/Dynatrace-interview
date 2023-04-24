package com.dynatrace.currency.utils.validators;

import java.util.Currency;
import java.util.regex.Pattern;

public class Validator {
    private static final Pattern quotationPattern = Pattern
            .compile("^(0*(?:[1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5]))$");

    private static final Pattern bigDecimalPattern = Pattern
            .compile("^[+-]?(?:0|[1-9]\\d{0,2}(?:,?\\d{3})*)(?:\\.\\d+)?$");

    public static boolean isQuotationValid(String string) {
        return quotationPattern.matcher(string).find();
    }

    public static boolean isBigDecimalValid(String string) {
        return bigDecimalPattern.matcher(string).find();
    }

    public static boolean isCurrencyCodeValid(String string) {
        return Currency.getAvailableCurrencies().stream()
                .map(Currency::getCurrencyCode)
                .anyMatch(code -> code.equals(string));
    }
}
