package com.dynatrace.currency.buyAskExchange.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Currency;

@JsonPropertyOrder({"currencyCode", "difference"})
public record MajorDifferenceDto(Currency currencyCode, DifferenceDto difference) {
}
