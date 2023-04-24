package com.dynatrace.currency.averageExchange;

import com.dynatrace.currency.averageExchange.dto.SingleAverageExchangeDto;
import com.dynatrace.currency.nbpClient.NbpClient;
import com.dynatrace.currency.utils.exceptions.InvalidCurrencyCodeException;
import com.dynatrace.currency.utils.exceptions.InvalidDateException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AverageExchangeServiceTest {
    private static final LocalDate DATE = LocalDate.of(2023, 4, 1);
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
                () -> service.getSingleAverageExchangeDto("InvalidCode", DATE.format(ISO_LOCAL_DATE)))
                .isInstanceOf(InvalidCurrencyCodeException.class)
                .hasMessageStartingWith("Invalid currency code");
    }

    @ParameterizedTest(name = "should throw InvalidDateException when date is in wrong format [0]")
    @ValueSource(strings = {"invalidDate", "24-04-2023", "24-2023-04", "44-04-2023", "12-13-2023"})
    void shouldThrowInvalidDateExceptionWhenDateIsInWrongFormat(String date) {
        // WHEN & THEN
        assertThatThrownBy(() -> service.getSingleAverageExchangeDto(CURRENCY_CODE, date))
                .isInstanceOf(InvalidDateException.class)
                .hasMessageStartingWith("Invalid date");
    }

    @Test
    @DisplayName("should return SingleAverageExchangeDto on successful")
    void shouldReturnSingleAverageExchangeDtoOnSuccessful() {
        // GIVEN
        AverageExchange averageExchange = AverageExchange.builder()
                .currencyCode(Currency.getInstance(CURRENCY_CODE))
                .rates(List.of(new AverageRate(DATE, new BigDecimal("0.421"))))
                .build();
        given(nbpClient.getAverageExchange(any(Currency.class), any(LocalDate.class)))
                .willReturn(averageExchange);

        // WHEN
        var result = service.getSingleAverageExchangeDto(CURRENCY_CODE, DATE.format(ISO_LOCAL_DATE));

        // THEN
        assertThat(result).isInstanceOf(SingleAverageExchangeDto.class);
        assertThat(result.currencyCode()).isEqualTo(Currency.getInstance(CURRENCY_CODE));
        assertThat(result.average().date()).isEqualTo(DATE);
        assertThat(result.average().value()).isEqualTo(new BigDecimal("0.421"));
    }
}