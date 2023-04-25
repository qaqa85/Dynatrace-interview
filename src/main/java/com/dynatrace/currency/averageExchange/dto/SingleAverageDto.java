package com.dynatrace.currency.averageExchange.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

@Builder
@JsonPropertyOrder({"currencyCode, average"})
public record SingleAverageDto(String currencyCode, AverageDto average) {
}
