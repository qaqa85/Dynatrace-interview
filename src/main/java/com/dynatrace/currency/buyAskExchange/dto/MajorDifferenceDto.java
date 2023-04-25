package com.dynatrace.currency.buyAskExchange.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

@Builder
@JsonPropertyOrder({"currencyCode", "difference"})
public record MajorDifferenceDto(String currencyCode, DifferenceDto difference) {
}
