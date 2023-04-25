package com.dynatrace.currency.buyAskExchange;

import com.dynatrace.currency.buyAskExchange.dto.MajorDifferenceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/exchanges/buy-ask")
public class BuyAskExchangeController {
    private final BuyAskExchangeService service;

    @Operation(summary = "returns a major difference between buy and ask rate in last N quotations",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = MajorDifferenceDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(name = "Invalid foreign currency",
                                                    value = "Insert a foreign currency code"),
                                            @ExampleObject(name = "Invalid currency",
                                                    value = "Invalid currency code. Check ISO 4217 codes"),
                                            @ExampleObject(name = "Invalid quotation number",
                                                    value = "Quotations number out of scope [1-255]")
                                    }
                            ))
            }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/{code}/last/{quotations}")
    MajorDifferenceDto getMajorDifference(
            @PathVariable("code") String currencyCode,
            @PathVariable("quotations") String quotations) {
        return service.getMajorDifference(currencyCode, quotations);
    }
}
