package com.dynatrace.currency.buyAskExchange;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record BuyAskRate(LocalDate date, BigDecimal buy, BigDecimal ask) {
}
