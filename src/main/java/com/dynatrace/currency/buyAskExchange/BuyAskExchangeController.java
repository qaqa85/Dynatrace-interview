package com.dynatrace.currency.buyAskExchange;

import com.dynatrace.currency.buyAskExchange.dto.MajorDifferenceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/buy-ask")
public class BuyAskExchangeController {
    private final BuyAskExchangeService service;

    @GetMapping("/{code}")
    MajorDifferenceDto getMajorDifference(
            @PathVariable("code") String currencyCode,
            @RequestParam("last") String quotations) {
        return service.getMajorDifferenceIn(currencyCode, quotations);
    }
}
