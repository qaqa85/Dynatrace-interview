package com.dynatrace.currency.averageExchange;

import com.dynatrace.currency.averageExchange.dto.MinMaxAverageDto;
import com.dynatrace.currency.averageExchange.dto.SingleAverageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/exchanges/average")
public class AverageExchangeController {
    private final AverageExchangeService service;

    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/{code}/date/{date}")
    SingleAverageDto getSingleAverageExchange(
            @PathVariable("code") String currencyCode,
            @PathVariable String date) {
        return service.getSingleAverageExchangeDto(currencyCode, date);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/{code}/last/{quotations}")
    MinMaxAverageDto getMinMaxAverageExchange(
            @PathVariable("code") String currencyCode,
            @PathVariable("quotations") String quotations) {
        return service.getMinMaxAverageDto(currencyCode, quotations);
    }
}
