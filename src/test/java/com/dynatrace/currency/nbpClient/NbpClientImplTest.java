package com.dynatrace.currency.nbpClient;

import com.dynatrace.currency.buyAskExchange.BuyAskExchange;
import com.dynatrace.currency.nbpClient.configuration.NbpClientConfiguration;
import com.dynatrace.currency.utils.mappers.ExchangeMapper;
import com.dynatrace.currency.utils.mappers.RateMapper;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Currency;

class NbpClientImplTest {

    WebClient webClient = WebClient.create();
    NbpClientConfiguration configuration = () -> "http://api.nbp.pl/api/exchangerates";
    NbpClient nbpClient = new NbpClientImpl(webClient, configuration, new ExchangeMapper(new RateMapper()));
    @Test
    void shouldGetDataFromNBP() {
        BuyAskExchange exchange = nbpClient.getBuyAskExchange(Currency.getInstance("GBP"), 10);
        System.out.println(exchange);
    }
}