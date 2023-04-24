package com.dynatrace.currency.buyAskExchange;

import lombok.Builder;

import java.util.Currency;
import java.util.List;

@Builder
public record BuyAskExchange(List<BuyAskRate> rates, Currency currencyCode) {
}
