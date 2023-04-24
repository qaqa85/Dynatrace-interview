package com.dynatrace.currency.averageExchange;

import com.dynatrace.currency.averageExchange.dto.AverageDto;
import com.dynatrace.currency.averageExchange.dto.SingleAverageExchangeDto;
import com.dynatrace.currency.nbpClient.NbpClient;
import com.dynatrace.currency.utils.exceptions.InvalidAverageExchangeRateSizeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

import static com.dynatrace.currency.utils.converters.Converter.getCurrencyFromString;
import static com.dynatrace.currency.utils.converters.Converter.getLocalDateFromString;

@RequiredArgsConstructor
@Service
class AverageExchangeService {
    private final NbpClient nbpClient;

    public SingleAverageExchangeDto getSingleAverageExchangeDto(String currencyCode, String dateString) {
        Currency currency = getCurrencyFromString(currencyCode);
        LocalDate date = getLocalDateFromString(dateString);

        AverageExchange exchange = nbpClient.getAverageExchange(currency, date);

        return SingleAverageExchangeDto.builder()
                .currencyCode(currency)
                .average(toAverageDto(exchange.rates()))
                .build();
    }

    private AverageDto toAverageDto(List<AverageRate> rates) {
        if (rates.size() == 0) {
            throw new InvalidAverageExchangeRateSizeException("Rate list cannot be null");
        }

        AverageRate singleRate = rates.get(0);
        return new AverageDto(singleRate.date(), singleRate.value());
    }

}
