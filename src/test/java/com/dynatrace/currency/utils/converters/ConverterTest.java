package com.dynatrace.currency.utils.converters;

import com.dynatrace.currency.utils.exceptions.ConversionToBigDecimalException;
import com.dynatrace.currency.utils.exceptions.ConversionToCurrencyException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConverterTest {
    @ParameterizedTest(name = "should throw ConversionToBigDecimalException when {0} cannot be parsed to BigDecimal")
    @ValueSource(strings = {"invalidDecimal", "12,12", "12,", " "})
    @NullSource
    void convertToBigDecimalShouldThrowConversionToBigDecimalExceptionWhenInvalidInput(String input) {
        // WHEN & THEN
        assertThatThrownBy(() -> Converter.convertToBigDecimal(input))
                .isInstanceOf(ConversionToBigDecimalException.class)
                .hasMessageStartingWith("Can't convert");
    }

    @ParameterizedTest(name = "should convert {0} to BigDecimal")
    @CsvSource({"12.12, 12.12", "1.12, 1.12", ".12, 0.12", "12., 12"})
    void convertToCurrencyConvertToBigDecimalShouldConvertToBigDecimal(String input, BigDecimal decimal) {
        // WHEN
        var result = Converter.convertToBigDecimal(input);

        // THEN
        assertEquals(result, decimal);
    }

    @ParameterizedTest(name = "should throw ConversionToCurrencyException when {0} cannot be parsed to Currency")
    @ValueSource(strings = {"pln", "usd", "tar", "mac", "CAT"})
    @NullSource
    void convertToCurrencyShouldThrowConversionToCurrencyExceptionWhenInvalidInput(String input) {
        // WHEN
        assertThatThrownBy(() -> Converter.convertToCurrency(input))
                .isInstanceOf(ConversionToCurrencyException.class)
                .hasMessageStartingWith("Cannot convert currency");
    }

    @ParameterizedTest(name = "should convert {0} to Currency")
    @ValueSource(strings = {"PLN", "USD", "GBP"})
    void convertToCurrencyShouldConvertToCurrency(String input) {
        // WHEN
        var result = Converter.convertToCurrency(input);

        // THEN
        assertEquals(result.getCurrencyCode(), input);
    }
}