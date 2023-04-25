package com.dynatrace.currency.averageExchange.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.util.Currency;

@Builder
@JsonPropertyOrder({"currencyCode, average"})
public record SingleAverageDto(Currency currencyCode, AverageDto average) {
}
