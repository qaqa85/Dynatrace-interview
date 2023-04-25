package com.dynatrace.currency.averageExchange.dto;

import lombok.Builder;

@Builder
public record MinMaxAverageDto(String currencyCode, AverageDto min, AverageDto max) {
}
