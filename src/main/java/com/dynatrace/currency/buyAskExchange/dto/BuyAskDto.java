package com.dynatrace.currency.buyAskExchange.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BuyAskDto(LocalDate date, BigDecimal value) {
}
