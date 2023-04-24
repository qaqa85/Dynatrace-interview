package com.dynatrace.currency.nbpClient.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record AverageExchangeDto(List<AverageRateDto> rates, @JsonProperty("code") String currencyCode) {
}
