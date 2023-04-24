package com.dynatrace.currency.utils.mappers;

import com.dynatrace.currency.averageExchange.AverageExchange;
import com.dynatrace.currency.averageExchange.AverageRate;
import com.dynatrace.currency.buyAskExchange.BuyAskExchange;
import com.dynatrace.currency.buyAskExchange.BuyAskRate;
import com.dynatrace.currency.nbpClient.dtos.AverageExchangeDto;
import com.dynatrace.currency.nbpClient.dtos.AverageExchangeDto.AverageExchangeDtoBuilder;
import com.dynatrace.currency.nbpClient.dtos.AverageRateDto;
import com.dynatrace.currency.nbpClient.dtos.BuyAskExchangeDto;
import com.dynatrace.currency.nbpClient.dtos.BuyAskExchangeDto.BuyAskExchangeDtoBuilder;
import com.dynatrace.currency.nbpClient.dtos.BuyAskRateDto;
import com.dynatrace.currency.utils.converters.exceptions.ConversionToBigDecimalException;
import com.dynatrace.currency.utils.converters.exceptions.ConversionToCurrencyException;
import com.dynatrace.currency.utils.mappers.ExchangeMapper;
import com.dynatrace.currency.utils.mappers.RateMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ExchangeMapperTest {
    private static final LocalDate DATE_2023_04_01 = LocalDate.of(2023, 4, 1);
    private static final LocalDate DATE_2023_04_02 = LocalDate.of(2023, 4, 2);
    ExchangeMapper exchangeMapper = new ExchangeMapper(new RateMapper());

    @Test
    @DisplayName("should throw null pointer exception when averageExchangeDto is null")
    void toAverageExchangeShouldThrowNullPointerExceptionWhenAverageExchangeDtoIsNull() {
        // WHEN & THEN
        assertThatThrownBy(() -> exchangeMapper.toAverageExchange(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageStartingWith("Average exchange");
    }

    @Test
    @DisplayName("should throw null pointer exception when rate list is null")
    void toAverageExchangeShouldThrowNullPointerExceptionWhenRateListIsNull() {
        // GIVEN
        AverageExchangeDto averageExchangeDto = defaultAverageExchange()
                .rates(null)
                .build();

        // WHEN & THEN
        assertThatThrownBy(() -> exchangeMapper.toAverageExchange(averageExchangeDto))
                .isInstanceOf(NullPointerException.class)
                .hasMessageStartingWith("Rate list");
    }

    @Test
    @DisplayName("should throw ConversionToBigDecimalException when rate value is invalid")
    void toAverageExchangeShouldThrowConversionToBigDecimalExceptionWhenRateValueIsInvalid() {
        // GIVEN
        AverageExchangeDto averageExchangeDto = defaultAverageExchange()
                .rates(List.of(new AverageRateDto(DATE_2023_04_02, "invalidNumber")))
                .build();

        // WHEN & THEN
        assertThatThrownBy(() -> exchangeMapper.toAverageExchange(averageExchangeDto))
                .isInstanceOf(ConversionToBigDecimalException.class)
                .hasMessageStartingWith("Can't convert");
    }

    @Test
    @DisplayName("should throw InvalidCurrencyCodeException when currencyCode is invalid")
    void toAverageExchangeShouldThrowInvalidCurrencyCodeExceptionWhenCurrencyCodeIsInvalid() {
        // GIVEN
        AverageExchangeDto averageExchangeDto = defaultAverageExchange()
                .currencyCode("invalidCurrency")
                .build();

        // WHEN & THEN
        assertThatThrownBy(() -> exchangeMapper.toAverageExchange(averageExchangeDto))
                .isInstanceOf(ConversionToCurrencyException.class);
    }

    @Test
    @DisplayName("should throw InvalidCurrencyCodeException when currencyCode is null")
    void toAverageExchangeShouldThrowInvalidCurrencyCodeExceptionWhenCurrencyCodeIsNull() {
        // GIVEN
        AverageExchangeDto averageExchangeDto = defaultAverageExchange()
                .currencyCode(null)
                .build();

        // WHEN & THEN
        assertThatThrownBy(() -> exchangeMapper.toAverageExchange(averageExchangeDto))
                .isInstanceOf(ConversionToCurrencyException.class);
    }

    @Test
    @DisplayName("should return averageExchange")
    void toAverageExchangeShouldReturnAverageExchange() {
        // GIVEN
        AverageExchangeDto averageExchangeDto = defaultAverageExchange()
                .build();

        AverageRate[] averageExchangeInBigDecimal = averageExchangeDto.rates().stream()
                .map(rateDto -> new AverageRate(rateDto.effectiveDate(), new BigDecimal(rateDto.averageValue())))
                .toArray(AverageRate[]::new);

        // WHEN
        var result = exchangeMapper.toAverageExchange(averageExchangeDto);

        // THEN
        assertThat(result).isInstanceOf(AverageExchange.class);
        assertThat(result.currencyCode().getCurrencyCode()).isEqualTo(averageExchangeDto.currencyCode());
        assertThat(result.rates()).containsExactly(averageExchangeInBigDecimal);
    }

    @Test
    @DisplayName("should throw null pointer exception when BuyAskExchangeDto is null")
    void toBuyAskExchangeShouldThrowNullPointerExceptionWhenBuyAskExchangeDtoIsNull() {
        // WHEN & THEN
        assertThatThrownBy(() -> exchangeMapper.toBuyAskExchange(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageStartingWith("BuyAsk exchange");
    }

    @Test
    @DisplayName("should throw null pointer exception when rate list is null")
    void toBuyAskExchangeShouldThrowNullPointerExceptionWhenRateListIsNull() {
        // GIVEN
        BuyAskExchangeDto buyAskExchangeDto = defaultBuyAskExchange()
                .rates(null)
                .build();

        // WHEN & THEN
        assertThatThrownBy(() -> exchangeMapper.toBuyAskExchange(buyAskExchangeDto))
                .isInstanceOf(NullPointerException.class)
                .hasMessageStartingWith("Rate list");
    }

    @ParameterizedTest(name = "should throw ConversionToBigDecimalException when buy value [0] and ask value [1]")
    @CsvSource({"21.99, InvalidValue", "InvalidValue, 21.99", "InvalidValue, InvalidValue"})
    void toBuyAskExchangeShouldThrowConversionToBigDecimalExceptionWhenRateValueIsInvalid(String buy, String ask) {
        // GIVEN
        BuyAskExchangeDto buyAskExchangeDto = defaultBuyAskExchange()
                .rates(List.of(new BuyAskRateDto(DATE_2023_04_01, buy, ask)))
                .build();

        // WHEN & THEN
        assertThatThrownBy(() -> exchangeMapper.toBuyAskExchange(buyAskExchangeDto))
                .isInstanceOf(ConversionToBigDecimalException.class)
                .hasMessageStartingWith("Can't convert");
    }

    @Test
    @DisplayName("should throw InvalidCurrencyCodeException when currencyCode is invalid")
    void toBuyAskExchangeShouldThrowInvalidCurrencyCodeExceptionWhenCurrencyCodeIsInvalid() {
        // GIVEN
        BuyAskExchangeDto buyAskExchangeDto = defaultBuyAskExchange()
                .currencyCode("InvalidCurrency")
                .build();

        // WHEN & THEN
        assertThatThrownBy(() -> exchangeMapper.toBuyAskExchange(buyAskExchangeDto))
                .isInstanceOf(ConversionToCurrencyException.class);
    }

    @Test
    @DisplayName("should throw InvalidCurrencyCodeException when currencyCode is null")
    void toBuyAskExchangeShouldThrowInvalidCurrencyCodeExceptionWhenCurrencyCodeIsNull() {
        // GIVEN
        BuyAskExchangeDto buyAskExchangeDto = defaultBuyAskExchange()
                .currencyCode(null)
                .build();

        // WHEN & THEN
        assertThatThrownBy(() -> exchangeMapper.toBuyAskExchange(buyAskExchangeDto))
                .isInstanceOf(ConversionToCurrencyException.class);
    }

    @Test
    @DisplayName("should return buyAskExchange")
    void toBuyAskExchangeShouldReturnBuyAskExchange() {
        // GIVEN
        BuyAskExchangeDto buyAskExchangeDto = defaultBuyAskExchange()
                .build();

        BuyAskRate[] averageExchangeInBigDecimal = buyAskExchangeDto.rates().stream()
                .map(rateDto -> new BuyAskRate(
                        rateDto.effectiveDate(),
                        new BigDecimal(rateDto.buyValue()),
                        new BigDecimal(rateDto.askValue())))
                .toArray(BuyAskRate[]::new);

        // WHEN
        var result = exchangeMapper.toBuyAskExchange(buyAskExchangeDto);

        // THEN
        assertThat(result).isInstanceOf(BuyAskExchange.class);
        assertThat(result.currencyCode().getCurrencyCode()).isEqualTo(buyAskExchangeDto.currencyCode());
        assertThat(result.rates()).containsExactly(averageExchangeInBigDecimal);
    }

    private AverageExchangeDtoBuilder defaultAverageExchange() {
        return AverageExchangeDto.builder()
                .rates(List.of(
                        new AverageRateDto(DATE_2023_04_01, "22.99"),
                        new AverageRateDto(DATE_2023_04_02, "23.999")))
                .currencyCode("USD");
    }

    private BuyAskExchangeDtoBuilder defaultBuyAskExchange() {
        return BuyAskExchangeDto.builder()
                .rates(List.of(
                        new BuyAskRateDto(DATE_2023_04_01, "21.99", "20.19"),
                        new BuyAskRateDto(DATE_2023_04_02, "24.99", "24.19")))
                .currencyCode("USD");
    }
}