package com.dynatrace.currency.averageExchange.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AverageDto(LocalDate date, BigDecimal value) {
}
