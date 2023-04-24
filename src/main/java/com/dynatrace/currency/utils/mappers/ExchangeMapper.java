package com.dynatrace.currency.utils.mappers;

import com.dynatrace.currency.averageExchange.AverageExchange;
import com.dynatrace.currency.buyAskExchange.BuyAskExchange;
import com.dynatrace.currency.nbpClient.dtos.AverageExchangeDto;
import com.dynatrace.currency.nbpClient.dtos.BuyAskExchangeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.dynatrace.currency.utils.converters.Converter.convertToCurrency;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeMapper {
    public static final String NULL_AVERAGE_EXCHANGE_DTO_MESSAGE = "Average exchange dto cannot be null";
    public static final String NULL_BUY_ASK_EXCHANGE_DTO_MESSAGE = "BuyAsk exchange cannot be null";
    public static final String NULL_RATE_LIST_MESSAGE = "Rate list cannot be null";
    private final RateMapper rateMapper;

    public AverageExchange toAverageExchange(AverageExchangeDto averageExchangeDto) {
        Objects.requireNonNull(averageExchangeDto, NULL_AVERAGE_EXCHANGE_DTO_MESSAGE);
        Objects.requireNonNull(averageExchangeDto.rates(), NULL_RATE_LIST_MESSAGE);

        return AverageExchange.builder()
                .rates(averageExchangeDto.rates().stream()
                        .map(rateMapper::toAverageRate)
                        .toList())
                .currencyCode(convertToCurrency(averageExchangeDto.currencyCode()))
                .build();
    }

    public BuyAskExchange toBuyAskExchange(BuyAskExchangeDto buyAskExchangeDto) {
        Objects.requireNonNull(buyAskExchangeDto, NULL_BUY_ASK_EXCHANGE_DTO_MESSAGE);
        Objects.requireNonNull(buyAskExchangeDto.rates(), NULL_RATE_LIST_MESSAGE);

        return BuyAskExchange.builder()
                .rates(buyAskExchangeDto.rates().stream()
                        .map(rateMapper::toBuyAskRate)
                        .toList())
                .currencyCode(convertToCurrency(buyAskExchangeDto.currencyCode()))
                .build();
    }
}
