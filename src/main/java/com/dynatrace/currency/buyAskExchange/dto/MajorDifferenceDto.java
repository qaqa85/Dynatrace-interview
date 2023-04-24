package com.dynatrace.currency.buyAskExchange.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.util.Currency;

@Builder
@JsonPropertyOrder({"currencyCode", "difference"})
public record MajorDifferenceDto(Currency currencyCode, DifferenceDto difference) {
}
