package com.dynatrace.currency.utils.mappers;

import com.dynatrace.currency.averageExchange.AverageRate;
import com.dynatrace.currency.buyAskExchange.BuyAskRate;
import com.dynatrace.currency.nbpClient.dtos.AverageRateDto;
import com.dynatrace.currency.nbpClient.dtos.BuyAskRateDto;
import org.springframework.stereotype.Component;

import static com.dynatrace.currency.utils.converters.Converter.convertToBigDecimal;

@Component
public class RateMapper {

    public AverageRate toAverageRate(AverageRateDto rateDto) {
        return new AverageRate(rateDto.effectiveDate(), convertToBigDecimal(rateDto.averageValue()));
    }

    public BuyAskRate toBuyAskRate(BuyAskRateDto rateDto) {
            return BuyAskRate.builder()
                    .date(rateDto.effectiveDate())
                    .buy(convertToBigDecimal(rateDto.buyValue()))
                    .ask(convertToBigDecimal(rateDto.askValue()))
                    .build();
    }
}
