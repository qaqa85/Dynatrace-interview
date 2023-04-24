package com.dynatrace.currency.buyAskExchange;

import com.dynatrace.currency.buyAskExchange.dto.MajorDifferenceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/exchanges/buy-ask")
public class BuyAskExchangeController {
    private final BuyAskExchangeService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/{code}")
    MajorDifferenceDto getMajorDifference(
            @PathVariable("code") String currencyCode,
            @RequestParam("last") String quotations) {
        return service.getMajorDifference(currencyCode, quotations);
    }
}
