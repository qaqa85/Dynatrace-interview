package com.dynatrace.currency.averageExchange.mappers;

import com.dynatrace.currency.averageExchange.AverageRate;
import com.dynatrace.currency.buyAskExchange.BuyAskRate;
import com.dynatrace.currency.nbpClient.dtos.AverageRateDto;
import com.dynatrace.currency.nbpClient.dtos.BuyAskRateDto;
import com.dynatrace.currency.nbpClient.exceptions.ConversionToBigDecimalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class RateMapper {

    public static final String BIGDECIMAL_CONVERSION_LOG = "Cannot convert string to BigDecimal";

    public AverageRate toAverageRate(AverageRateDto rateDto) {
        try {
            return new AverageRate(rateDto.effectiveDate(), new BigDecimal(rateDto.averageValue()));
        } catch (NumberFormatException ex) {
            log.error(BIGDECIMAL_CONVERSION_LOG);
            throw new ConversionToBigDecimalException();
        }
    }

    public BuyAskRate toBuyAskRate(BuyAskRateDto rateDto) {
        try {
            return BuyAskRate.builder()
                    .date(rateDto.effectiveDate())
                    .buy(new BigDecimal(rateDto.buyValue()))
                    .ask(new BigDecimal(rateDto.askValue()))
                    .build();
        } catch (NumberFormatException ex) {
            log.error(BIGDECIMAL_CONVERSION_LOG);
            throw new ConversionToBigDecimalException();
        }
    }
}
