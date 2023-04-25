package com.dynatrace.currency.averageExchange.dto;

import lombok.Builder;

import java.util.Currency;

@Builder
public record MinMaxAverageDto(Currency currencyCode, AverageDto min, AverageDto max) {
}
