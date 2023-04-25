package com.dynatrace.currency.averageExchange;

import com.dynatrace.currency.averageExchange.dto.AverageDto;
import com.dynatrace.currency.averageExchange.dto.MinMaxAverageDto;
import com.dynatrace.currency.averageExchange.dto.SingleAverageDto;
import com.dynatrace.currency.nbpClient.NbpClient;
import com.dynatrace.currency.utils.exceptions.InvalidAverageExchangeRateSizeException;
import com.dynatrace.currency.utils.exceptions.InvalidCurrencyCodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

import static com.dynatrace.currency.utils.converters.Converter.*;

@RequiredArgsConstructor
@Service
class AverageExchangeService {
    private final NbpClient nbpClient;

    public SingleAverageDto getSingleAverageExchangeDto(String currencyCode, String dateString) {
        Currency currency = getCurrencyFromStringExcludedPLN(currencyCode);
        LocalDate date = getLocalDateFromString(dateString);

        AverageExchange exchange = nbpClient.getAverageExchange(currency, date);

        return SingleAverageDto.builder()
                .currencyCode(currency.toString())
                .average(toAverageDto(exchange.rates()))
                .build();
    }

    public MinMaxAverageDto getMinMaxAverageDto(String currencyCode, String lastQuotations) {
        Currency currency = getCurrencyFromStringExcludedPLN(currencyCode);
        Integer quotations = getQuotationsFromString(lastQuotations);

        AverageExchange exchange = nbpClient.getAverageExchange(currency, quotations);
        return getMinMaxAverageFromExchange(exchange);
    }

    private MinMaxAverageDto getMinMaxAverageFromExchange(AverageExchange exchange) {
        AverageDto min = getMinAverage(exchange.rates());
        AverageDto max = getMaxAverage(exchange.rates());

        return MinMaxAverageDto.builder()
                .currencyCode(exchange.currencyCode().toString())
                .min(min)
                .max(max)
                .build();
    }

    private AverageDto getMinAverage(List<AverageRate> rates) {
        int minAverageIndex = 0;
        for (int i = 1; i < rates.size(); i++) {
            if (rates.get(minAverageIndex).value().compareTo(rates.get(i).value()) > 0) {
                minAverageIndex = i;
            }
        }

        return new AverageDto(
                rates.get(minAverageIndex).date(),
                rates.get(minAverageIndex).value());
    }

    private AverageDto getMaxAverage(List<AverageRate> rates) {
        int maxAverageIndex = 0;
        for (int i = 1; i < rates.size(); i++) {
            if (rates.get(maxAverageIndex).value().compareTo(rates.get(i).value()) < 0) {
                maxAverageIndex = i;
            }
        }

        return new AverageDto(
                rates.get(maxAverageIndex).date(),
                rates.get(maxAverageIndex).value());
    }

    private AverageDto toAverageDto(List<AverageRate> rates) {
        if (rates.size() == 0) {
            throw new InvalidAverageExchangeRateSizeException("Rate list cannot be null");
        }

        AverageRate singleRate = rates.get(0);
        return new AverageDto(singleRate.date(), singleRate.value());
    }

    private Currency getCurrencyFromStringExcludedPLN(String currencyCode) {
        if (currencyCode.equals("PLN")) {
            throw new InvalidCurrencyCodeException("Insert a foreign currency code");
        }

        return getCurrencyFromString(currencyCode);
    }
}
