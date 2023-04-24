package com.dynatrace.currency.nbpClient.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BuyAskRateDto(
        LocalDate effectiveDate,
        @JsonProperty("bid") String buyValue,
        @JsonProperty("ask") String askValue) {
}