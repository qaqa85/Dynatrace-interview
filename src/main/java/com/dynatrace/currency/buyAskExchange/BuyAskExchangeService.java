package com.dynatrace.currency.buyAskExchange;

import com.dynatrace.currency.buyAskExchange.dto.BuyAskDto;
import com.dynatrace.currency.buyAskExchange.dto.DifferenceDto;
import com.dynatrace.currency.buyAskExchange.dto.MajorDifferenceDto;
import com.dynatrace.currency.nbpClient.NbpClient;
import com.dynatrace.currency.utils.exceptions.InvalidCurrencyCodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.List;

import static com.dynatrace.currency.utils.converters.Converter.*;

@Service
@RequiredArgsConstructor
class BuyAskExchangeService {
    private final NbpClient nbpClient;

    public MajorDifferenceDto getMajorDifference(String currencyCode, String lastQuotations) {
        Currency currency = getCurrencyFromStringExcludedPLN(currencyCode);
        Integer quotations = getQuotationsFromString(lastQuotations);

        return getMajorDifferenceFromExchange(nbpClient.getBuyAskExchange(currency, quotations));
    }

    private MajorDifferenceDto getMajorDifferenceFromExchange(BuyAskExchange buyAskExchange) {
        BuyAskDto highestAskDto = getHighestAskDto(buyAskExchange.rates());
        BuyAskDto lowestBuyDto = getLowestBuyDto(buyAskExchange.rates());

        return new MajorDifferenceDto(
                buyAskExchange.currencyCode(),
                DifferenceDto.builder()
                        .ask(highestAskDto)
                        .buy(lowestBuyDto)
                        .difference(highestAskDto.value().subtract(lowestBuyDto.value()))
                        .build()
                );
    }

    private BuyAskDto getHighestAskDto(List<BuyAskRate> buyAskRates) {
        int highestAskIndex = 0;
        for (int i = 1; i < buyAskRates.size(); i++) {
            if (buyAskRates.get(highestAskIndex).ask().compareTo(buyAskRates.get(i).ask()) < 0) {
                highestAskIndex = i;
            }
        }

        return new BuyAskDto(
                buyAskRates.get(highestAskIndex).date(),
                buyAskRates.get(highestAskIndex).ask());
    }

    private BuyAskDto getLowestBuyDto(List<BuyAskRate> buyAskRates) {
        int lowestBuyIndex = 0;

        for (int i = 1; i < buyAskRates.size(); i++) {
            if (buyAskRates.get(lowestBuyIndex).buy().compareTo(buyAskRates.get(i).buy()) > 0) {
                lowestBuyIndex = i;
            }
        }

        return new BuyAskDto(
                buyAskRates.get(lowestBuyIndex).date(),
                buyAskRates.get(lowestBuyIndex).buy());
    }

    private Currency getCurrencyFromStringExcludedPLN(String currencyCode) {
        if (currencyCode.equals("PLN")) {
            throw new InvalidCurrencyCodeException("Insert a foreign currency code");
        }

        return getCurrencyFromString(currencyCode);
    }
}

