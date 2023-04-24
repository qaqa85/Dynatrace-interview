package com.dynatrace.currency.averageExchange;

import com.dynatrace.currency.averageExchange.dto.SingleAverageExchangeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/exchanges/average")
public class AverageExchangeController {
    private final AverageExchangeService service;

    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/{code}")
    SingleAverageExchangeDto getSingleAverageExchange(
            @PathVariable("code") String currencyCode,
            @RequestParam String date) {
        return service.getSingleAverageExchangeDto(currencyCode, date);
    }
}
