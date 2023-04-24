package com.dynatrace.currency.averageExchange;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AverageRate(LocalDate date, BigDecimal value) {
}
