package com.dynatrace.currency.averageExchange;

import com.dynatrace.currency.averageExchange.dto.MinMaxAverageDto;
import com.dynatrace.currency.averageExchange.dto.SingleAverageDto;
import com.dynatrace.currency.nbpClient.NbpClient;
import com.dynatrace.currency.utils.exceptions.InvalidCurrencyCodeException;
import com.dynatrace.currency.utils.exceptions.InvalidDateException;
import com.dynatrace.currency.utils.exceptions.InvalidLastQuotationsNumberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AverageExchangeServiceTest {
    private static final LocalDate DATE_FIRST = LocalDate.of(2023, 4, 1);
    private static final LocalDate DATE_SECOND = LocalDate.of(2023, 4, 2);
    private static final LocalDate DATE_THIRD = LocalDate.of(2023, 4, 3);
    private static final String CURRENCY_CODE = "USD";
    @Mock
    NbpClient nbpClient;

    @InjectMocks
    AverageExchangeService service;

    @Test
    @DisplayName("should throw invalid InvalidCurrencyCodeException when code is invalid")
    void getSingleAverageExchangeDtoShouldThrowInvalidCurrencyCodeExceptionWhenCodeIsInvalid() {
        // WHEN & THEN
        assertThatThrownBy(
                () -> service.getSingleAverageExchangeDto("InvalidCode", DATE_FIRST.format(ISO_LOCAL_DATE)))
                .isInstanceOf(InvalidCurrencyCodeException.class)
                .hasMessageStartingWith("Invalid currency code");
    }

    @ParameterizedTest(name = "should throw InvalidDateException when date is in wrong format [0]")
    @ValueSource(strings = {"invalidDate", "24-04-2023", "24-2023-04", "44-04-2023", "12-13-2023"})
    void getSingleAverageExchangeDtoShouldThrowInvalidDateExceptionWhenDateIsInWrongFormat(String date) {
        // WHEN & THEN
        assertThatThrownBy(() -> service.getSingleAverageExchangeDto(CURRENCY_CODE, date))
                .isInstanceOf(InvalidDateException.class)
                .hasMessageStartingWith("Invalid date");
    }

    @Test
    @DisplayName("should return SingleAverageExchangeDto on successful")
    void getSingleAverageExchangeDtoShouldReturnSingleAverageExchangeDtoOnSuccessful() {
        // GIVEN
        AverageExchange averageExchange = AverageExchange.builder()
                .currencyCode(Currency.getInstance(CURRENCY_CODE))
                .rates(List.of(new AverageRate(DATE_FIRST, new BigDecimal("0.421"))))
                .build();
        given(nbpClient.getAverageExchange(any(Currency.class), any(LocalDate.class)))
                .willReturn(averageExchange);

        // WHEN
        var result = service.getSingleAverageExchangeDto(CURRENCY_CODE, DATE_FIRST.format(ISO_LOCAL_DATE));

        // THEN
        assertThat(result).isInstanceOf(SingleAverageDto.class);
        assertThat(result.currencyCode()).isEqualTo(CURRENCY_CODE);
        assertThat(result.average().date()).isEqualTo(DATE_FIRST);
        assertThat(result.average().value()).isEqualTo(new BigDecimal("0.421"));
    }

    @Test
    @DisplayName("should throw invalid InvalidCurrencyCodeException when code is invalid")
    void getMinMaxAverageDtoShouldThrowInvalidCurrencyCodeExceptionWhenCodeIsInvalid() {
        // WHEN & THEN
        assertThatThrownBy(
                () -> service.getMinMaxAverageDto("InvalidCode", "3"))
                .isInstanceOf(InvalidCurrencyCodeException.class)
                .hasMessageStartingWith("Invalid currency code");
    }

    @ParameterizedTest(name = "should throw invalid InvalidLastQuotationsNumberException when number is [0]")
    @ValueSource(strings = {"invalidNumber", "-10", "0", "256"})
    void getMinMaxAverageDtoShouldThrowInvalidLastQuotationsNumberExceptionWhenNumberIsInvalid(String invalidNumber) {
        // WHEN & THEN
        assertThatThrownBy(() -> service.getMinMaxAverageDto(CURRENCY_CODE, invalidNumber))
                .isInstanceOf(InvalidLastQuotationsNumberException.class)
                .hasMessageStartingWith("Quotations number out of scope");
    }

    @Test
    @DisplayName("should return SingleAverageExchangeDto on successful")
    void  getMinMaxAverageDtoShouldReturnSingleAverageExchangeDtoOnSuccessful() {
        // GIVEN
        AverageExchange averageExchange = AverageExchange.builder()
                .currencyCode(Currency.getInstance(CURRENCY_CODE))
                .rates(List.of(
                        new AverageRate(DATE_FIRST, new BigDecimal("3.213")),
                        new AverageRate(DATE_SECOND, new BigDecimal("1.131")),
                        new AverageRate(DATE_THIRD, new BigDecimal("8.123"))
                        ))
                .build();
        given(nbpClient.getAverageExchange(any(Currency.class), anyInt()))
                .willReturn(averageExchange);

        // WHEN
        var result = service.getMinMaxAverageDto(CURRENCY_CODE, "3");

        // THEN
        assertThat(result).isInstanceOf(MinMaxAverageDto.class);
        assertThat(result.currencyCode()).isEqualTo(CURRENCY_CODE);
        assertThat(result.min().date()).isEqualTo(DATE_SECOND);
        assertThat(result.min().value()).isEqualTo(new BigDecimal("1.131"));
        assertThat(result.max().date()).isEqualTo(DATE_THIRD);
        assertThat(result.max().value()).isEqualTo(new BigDecimal("8.123"));
    }
}