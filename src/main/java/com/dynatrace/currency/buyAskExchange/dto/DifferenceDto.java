package com.dynatrace.currency.buyAskExchange.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@JsonPropertyOrder({"difference", "buy", "ask"})
public record DifferenceDto(BuyAskDto ask, BuyAskDto buy, BigDecimal difference) {
}
