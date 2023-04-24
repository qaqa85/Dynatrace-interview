package com.dynatrace.currency.averageExchange;

import lombok.Builder;

import java.util.Currency;
import java.util.List;

@Builder
public record AverageExchange(List<AverageRate> rates, Currency currencyCode) {
}
