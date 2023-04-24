package com.dynatrace.currency.nbpClient;

import com.dynatrace.currency.averageExchange.AverageExchange;
import com.dynatrace.currency.buyAskExchange.BuyAskExchange;
import com.dynatrace.currency.nbpClient.configuration.NbpClientConfiguration;
import com.dynatrace.currency.nbpClient.dtos.AverageExchangeDto;
import com.dynatrace.currency.nbpClient.dtos.BuyAskExchangeDto;
import com.dynatrace.currency.nbpClient.exceptions.InvalidRequestException;
import com.dynatrace.currency.nbpClient.exceptions.NoDataException;
import com.dynatrace.currency.averageExchange.mappers.ExchangeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Objects;

import static com.dynatrace.currency.utils.StatusCode.isStatusCode400;
import static com.dynatrace.currency.utils.StatusCode.isStatusCode404;

@Component
@RequiredArgsConstructor
class NbpClientImpl implements NbpClient {
    private static final String CURRENCY_CODE = "/{currencyCode}";
    private static final String DATE = "/{date}";
    private static final String TABLE_A = "/a";
    public static final String TABLE_C = "/c";
    private static final String SINGLE_CURRENCY = "/rates";
    private static final String CODE_400_EXCEPTION_MESSAGE = "Correct request but no data found";
    private static final String CODE_404_EXCEPTION_MESSAGE = "Invalid request";
    private static final String CURRENCY_VALUE_ERROR_MESSAGE = "Currency cannot be null";
    private static final String DATE_VALUE_ERROR_MESSAGE = "Date cannot be null";
    private static final String QUANTITY_ERROR_MESSAGE = "Cannot be null";
    private static final String QUANTITY = "/last/{quantity}";

    private final WebClient webClient;
    private final NbpClientConfiguration configuration;
    private final ExchangeMapper exchangeMapper;

    @Override
    public AverageExchange getAverageExchange(Currency currency, LocalDate localDate) {
        Objects.requireNonNull(currency, CURRENCY_VALUE_ERROR_MESSAGE);
        Objects.requireNonNull(localDate, DATE_VALUE_ERROR_MESSAGE);

        return webClient.get()
                .uri(getAverageExchangeURI(currency.getCurrencyCode().toLowerCase(), localDate.toString()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(isStatusCode404(), clientResponse
                        -> Mono.error(new NoDataException(CODE_404_EXCEPTION_MESSAGE)))
                .onStatus(isStatusCode400(), clientResponse
                        -> Mono.error(new InvalidRequestException(CODE_400_EXCEPTION_MESSAGE)))
                .bodyToMono(AverageExchangeDto.class)
                .map(exchangeMapper::toAverageExchange)
                .block();
    }

    @Override
    public AverageExchange getAverageExchange(Currency currency, Integer quantity) {
        Objects.requireNonNull(currency, CURRENCY_VALUE_ERROR_MESSAGE);
        Objects.requireNonNull(quantity, QUANTITY_ERROR_MESSAGE);

        return webClient.get()
                .uri(getMostRecentAverageExchangesURI(currency.getCurrencyCode(), quantity))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(isStatusCode400(), clientResponse
                        -> Mono.error(new InvalidRequestException(CODE_400_EXCEPTION_MESSAGE)))
                .bodyToMono(AverageExchangeDto.class)
                .map(exchangeMapper::toAverageExchange)
                .block();
    }

    @Override
    public BuyAskExchange getBuyAskExchange(Currency currency, Integer quantity) {
        Objects.requireNonNull(currency, CURRENCY_VALUE_ERROR_MESSAGE);
        Objects.requireNonNull(quantity, QUANTITY_ERROR_MESSAGE);

        return webClient.get()
                .uri(getMostRecentBuyAskExchangesURI(currency.getCurrencyCode(), quantity))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(isStatusCode400(), clientResponse
                        -> Mono.error(new InvalidRequestException(CODE_400_EXCEPTION_MESSAGE)))
                .bodyToMono(BuyAskExchangeDto.class)
                .map(exchangeMapper::toBuyAskExchange)
                .block();
    }


    private URI getAverageExchangeURI(String currencyCode, String date) {
        return UriComponentsBuilder.fromUriString(configuration.getUrl())
                .path(SINGLE_CURRENCY)
                .path(TABLE_A)
                .path(CURRENCY_CODE)
                .path(DATE)
                .build(currencyCode, date);
    }

    private URI getMostRecentAverageExchangesURI(String currencyCode, Integer quantity) {
        return UriComponentsBuilder.fromUriString(configuration.getUrl())
                .path(SINGLE_CURRENCY)
                .path(TABLE_A)
                .path(CURRENCY_CODE)
                .path(QUANTITY)
                .build(currencyCode, quantity);
    }

    private URI getMostRecentBuyAskExchangesURI(String currencyCode, Integer quantity) {
        return UriComponentsBuilder.fromUriString(configuration.getUrl())
                .path(SINGLE_CURRENCY)
                .path(TABLE_C)
                .path(CURRENCY_CODE)
                .path(QUANTITY)
                .build(currencyCode, quantity);
    }
}
