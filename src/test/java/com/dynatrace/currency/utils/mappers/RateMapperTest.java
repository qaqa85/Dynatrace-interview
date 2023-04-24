package com.dynatrace.currency.utils.mappers;

import com.dynatrace.currency.averageExchange.AverageRate;
import com.dynatrace.currency.buyAskExchange.BuyAskRate;
import com.dynatrace.currency.nbpClient.dtos.AverageRateDto;
import com.dynatrace.currency.nbpClient.dtos.AverageRateDto.AverageRateDtoBuilder;
import com.dynatrace.currency.nbpClient.dtos.BuyAskRateDto;
import com.dynatrace.currency.nbpClient.dtos.BuyAskRateDto.BuyAskRateDtoBuilder;
import com.dynatrace.currency.utils.converters.exceptions.ConversionToBigDecimalException;
import com.dynatrace.currency.utils.mappers.RateMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class RateMapperTest {
    private static final LocalDate DATE_2023_04_01 = LocalDate.of(2023, 4, 1);
    RateMapper rateMapper = new RateMapper();

    @Test
    @DisplayName("should return ConversionToBigDecimalException when invalid value")
    void toAverageRateShouldReturnConversionToBigDecimalExceptionWhenInvalidValue() {
        // GIVEN
        AverageRateDto rateDto = defaultAverageRate()
                .averageValue("InvalidValue")
                .build();

        // WHEN & THEN
        assertThatThrownBy(() -> rateMapper.toAverageRate(rateDto))
                .isInstanceOf(ConversionToBigDecimalException.class)
                .hasMessageStartingWith("Can't convert");
    }

    @Test
    @DisplayName("should return rate")
    void toAverageRateShouldReturnAverageRate() {
        // GIVEN
        AverageRateDto rateDto = defaultAverageRate()
                .build();

        // WHEN
        var result = rateMapper.toAverageRate(rateDto);

        // THEN
        assertThat(result).isInstanceOf(AverageRate.class);
        assertThat(result.date()).isEqualTo(rateDto.effectiveDate());
        assertThat(result.value().toString()).isEqualTo(rateDto.averageValue());
    }

    @ParameterizedTest(name = "should return ReturnConversionToBigDecimalException when ask = [0] and buy = [1]")
    @CsvSource({"29.99, invalidValue", "invalidValue, 29.99"})
    void toBuyAskRateShouldReturnConversionToBigDecimalExceptionWhenInvalidValue(String ask, String buy) {
        // GIVEN
        BuyAskRateDto rateDto = defaultBuyAskRate()
                .askValue(ask)
                .buyValue(buy)
                .build();

        // WHEN & THEN
        assertThatThrownBy(() -> rateMapper.toBuyAskRate(rateDto))
                .isInstanceOf(ConversionToBigDecimalException.class)
                .hasMessageStartingWith("Can't convert");

    }

    @Test
    @DisplayName("should return rate")
    void toBuyAskRateShouldReturnBuyAskRate() {
        // GIVEN
        BuyAskRateDto rateDto = defaultBuyAskRate()
                .build();

        // WHEN
        var result = rateMapper.toBuyAskRate(rateDto);

        // THEN
        assertThat(result).isInstanceOf(BuyAskRate.class);
        assertThat(result.date()).isEqualTo(rateDto.effectiveDate());
        assertThat(result.ask().toString()).isEqualTo(rateDto.askValue());
        assertThat(result.buy().toString()).isEqualTo(rateDto.buyValue());
    }

    private AverageRateDtoBuilder defaultAverageRate() {
        return AverageRateDto.builder()
                .effectiveDate(DATE_2023_04_01)
                .averageValue("23.99");
    }

    private BuyAskRateDtoBuilder defaultBuyAskRate() {
        return BuyAskRateDto.builder()
                .effectiveDate(DATE_2023_04_01)
                .askValue("24.99")
                .buyValue("26.10");
    }

}