package com.dynatrace.currency.averageExchange;

import com.dynatrace.currency.averageExchange.dto.MinMaxAverageDto;
import com.dynatrace.currency.averageExchange.dto.SingleAverageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/exchanges/average")
public class AverageExchangeController {
    private final AverageExchangeService service;

    @Operation(summary = "returns a date and a currency code provide its average exchange rate",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SingleAverageDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(name = "Invalid foreign currency",
                                                    value = "Insert a foreign currency code"),
                                            @ExampleObject(name = "Invalid currency",
                                                    value = "Invalid currency code. Check ISO 4217 codes"),
                                            @ExampleObject(name = "Invalid date",
                                                    value = "Invalid date. Insert date in format YYYY-MM-DD")
                                    }
                            )),
                    @ApiResponse(responseCode = "404", description = "Invalid input",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(name = "No data",
                                                    value = "No data for specific day. Fix request or try later"),
                                    }
                            ))
            }
    )
    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/{code}/date/{date}")
    SingleAverageDto getSingleAverageExchange(
            @PathVariable("code") String currencyCode,
            @PathVariable String date) {
        return service.getSingleAverageExchangeDto(currencyCode, date);
    }

    @Operation(summary = "returns a minimal and maximal average value in last N quotations",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request",
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = MinMaxAverageDto.class))),
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
    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/{code}/last/{quotations}")
    MinMaxAverageDto getMinMaxAverageExchange(
            @PathVariable("code") String currencyCode,
            @PathVariable("quotations") String quotations) {
        return service.getMinMaxAverageDto(currencyCode, quotations);
    }
}
