package com.dynatrace.currency.averageExchange.mappers;

import com.dynatrace.currency.averageExchange.AverageExchange;
import com.dynatrace.currency.buyAskExchange.BuyAskExchange;
import com.dynatrace.currency.nbpClient.dtos.AverageExchangeDto;
import com.dynatrace.currency.nbpClient.dtos.BuyAskExchangeDto;
import com.dynatrace.currency.nbpClient.exceptions.InvalidCurrencyCodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeMapper {
    public static final String NULL_AVERAGE_EXCHANGE_DTO_MESSAGE = "Average exchange dto cannot be null";
    public static final String NULL_BUY_ASK_EXCHANGE_DTO_MESSAGE = "BuyAsk exchange cannot be null";
    public static final String NULL_RATE_LIST_MESSAGE = "Rate list cannot be null";
    public static final String CURRENCY_CONVERSION_LOG = "Cannot convert currency code into Currency object";
    private final RateMapper rateMapper;

    public AverageExchange toAverageExchange(AverageExchangeDto averageExchangeDto) {
        Objects.requireNonNull(averageExchangeDto, NULL_AVERAGE_EXCHANGE_DTO_MESSAGE);
        Objects.requireNonNull(averageExchangeDto.rates(), NULL_RATE_LIST_MESSAGE);

        try {
            return AverageExchange.builder()
                    .rates(averageExchangeDto.rates().stream()
                            .map(rateMapper::toAverageRate)
                            .toList())
                    .currencyCode(Currency.getInstance(averageExchangeDto.currencyCode()))
                    .build();
        } catch (IllegalArgumentException | NullPointerException ex) {
            log.error(CURRENCY_CONVERSION_LOG);
            throw new InvalidCurrencyCodeException(ex.getMessage());
        }
    }

    public BuyAskExchange toBuyAskExchange(BuyAskExchangeDto buyAskExchangeDto) {
        Objects.requireNonNull(buyAskExchangeDto, NULL_BUY_ASK_EXCHANGE_DTO_MESSAGE);
        Objects.requireNonNull(buyAskExchangeDto.rates(), NULL_RATE_LIST_MESSAGE);

        try {
            return BuyAskExchange.builder()
                    .rates(buyAskExchangeDto.rates().stream()
                            .map(rateMapper::toBuyAskRate)
                            .toList())
                    .currencyCode(Currency.getInstance(buyAskExchangeDto.currencyCode()))
                    .build();
        } catch (IllegalArgumentException | NullPointerException ex) {
            log.error(CURRENCY_CONVERSION_LOG);
            throw new InvalidCurrencyCodeException(ex.getMessage());
        }
    }
}
