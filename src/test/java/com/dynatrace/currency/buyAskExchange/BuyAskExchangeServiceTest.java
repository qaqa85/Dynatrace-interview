package com.dynatrace.currency.buyAskExchange;

import com.dynatrace.currency.buyAskExchange.dto.BuyAskDto;
import com.dynatrace.currency.buyAskExchange.dto.MajorDifferenceDto;
import com.dynatrace.currency.nbpClient.NbpClient;
import com.dynatrace.currency.utils.exceptions.InvalidCurrencyCodeException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BuyAskExchangeServiceTest {
    private static final String CURRENCY_CODE = "USD";
    private static final String QUOTATIONS_NUMBER = "3";
    private static final LocalDate DATE_FIRST = LocalDate.of(2023, 4, 1);
    private static final LocalDate DATE_SECOND = LocalDate.of(2023, 4, 2);
    private static final LocalDate DATE_THIRD = LocalDate.of(2023, 4, 3);

    @Mock
    NbpClient nbpClient;

    @InjectMocks
    BuyAskExchangeService service;

    @ParameterizedTest(name = "should throw invalid InvalidLastQuotationsNumberException when number is [0]")
    @ValueSource(strings = {"invalidNumber", "-10", "0", "256"})
    void shouldThrowInvalidLastQuotationsNumberExceptionWhenNumberIsInvalid(String invalidNumber) {
        // WHEN & THEN
        assertThatThrownBy(() -> service.getMajorDifference(CURRENCY_CODE, invalidNumber))
                .isInstanceOf(InvalidLastQuotationsNumberException.class)
                .hasMessageStartingWith("Quotations number out of scope");
    }

    @Test
    @DisplayName("should throw invalid InvalidCurrencyCodeException when code is invalid")
    void shouldThrowInvalidCurrencyCodeExceptionWhenCodeIsInvalid() {
        // WHEN & THEN
        assertThatThrownBy(() -> service.getMajorDifference("InvalidCode", QUOTATIONS_NUMBER))
                .isInstanceOf(InvalidCurrencyCodeException.class)
                .hasMessageStartingWith("Invalid currency code");
    }

    @Test
    @DisplayName("should return MajorDifferenceDto from 3 quotations")
    void shouldReturnMajorDifferenceDtoFromThreeQuotations() {
        // GIVEN
        BuyAskExchange buyAskExchange = BuyAskExchange.builder()
                .currencyCode(Currency.getInstance("USD"))
                .rates(getBuyAskRates())
                .build();

        given(nbpClient.getBuyAskExchange(any(Currency.class), anyInt())).willReturn(buyAskExchange);

        // WHEN
        var result = service.getMajorDifference(CURRENCY_CODE, QUOTATIONS_NUMBER);

        // THEN
        assertThat(result).isInstanceOf(MajorDifferenceDto.class);
        assertThat(result.difference().ask()).isEqualTo(new BuyAskDto(DATE_THIRD, new BigDecimal("24.45")));
        assertThat(result.difference().buy()).isEqualTo(new BuyAskDto(DATE_FIRST, new BigDecimal("20.45")));
        assertThat(result.difference().difference()).isEqualTo(new BigDecimal("4.00"));
    }

    @Test
    @DisplayName("should return MajorDifferenceDto from 1 quotations")
    void shouldReturnMajorDifferenceDtoFromOneQuotations() {
        // GIVEN
        BuyAskExchange buyAskExchange = BuyAskExchange.builder()
                .currencyCode(Currency.getInstance("USD"))
                .rates(getBuyAskRates().stream().limit(1).toList())
                .build();

        given(nbpClient.getBuyAskExchange(any(Currency.class), anyInt())).willReturn(buyAskExchange);

        // WHEN
        var result = service.getMajorDifference(CURRENCY_CODE, QUOTATIONS_NUMBER);

        // THEN
        assertThat(result).isInstanceOf(MajorDifferenceDto.class);
        assertThat(result.difference().ask()).isEqualTo(new BuyAskDto(DATE_FIRST, new BigDecimal("22.45")));
        assertThat(result.difference().buy()).isEqualTo(new BuyAskDto(DATE_FIRST, new BigDecimal("20.45")));
        assertThat(result.difference().difference()).isEqualTo(new BigDecimal("2.00"));
    }

    private List<BuyAskRate> getBuyAskRates() {
        return List.of(
                BuyAskRate.builder()
                        .date(DATE_FIRST)
                        .ask(new BigDecimal("22.45"))
                        .buy(new BigDecimal("20.45"))
                        .build(),
                BuyAskRate.builder()
                        .date(DATE_SECOND)
                        .ask(new BigDecimal("23.45"))
                        .buy(new BigDecimal("21.45"))
                        .build(),
                BuyAskRate.builder()
                        .date(DATE_THIRD)
                        .ask(new BigDecimal("24.45"))
                        .buy(new BigDecimal("22.45"))
                        .build());
    }
}