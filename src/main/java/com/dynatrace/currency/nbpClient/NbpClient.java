package com.dynatrace.currency.nbpClient;

import com.dynatrace.currency.averageExchange.AverageExchange;
import com.dynatrace.currency.averageExchange.AverageRate;
import com.dynatrace.currency.buyAskExchange.BuyAskExchange;

import java.time.LocalDate;
import java.util.Currency;

public interface NbpClient {

    /**
     * returns an object that contains a list with single {@link AverageRate} and inserted currency code.
     *
     * @param currency  Currency code in accordance with ISO 4217
     * @param localDate A date without a time-zone
     * @return the {@code AverageExchange} instance that contains requested rates and the currency code
     * @throws NullPointerException                                                if {@code currency} or {@code localDate} is null
     * @throws com.dynatrace.currency.nbpClient.exceptions.NoDataException         if request is valid but data not found
     * @throws com.dynatrace.currency.nbpClient.exceptions.InvalidRequestException if request is invalid
     */
    AverageExchange getAverageExchange(Currency currency, LocalDate localDate);

    /**
     * returns an object that contains a list of {@link AverageRate} and inserted currency code.
     *
     * @param currency Currency code in accordance with ISO 4217
     * @param quotations number of last quotations
     * @return the {@code AverageExchange} instance that contains requested rates and the currency code
     * @throws NullPointerException                                                if {@code currency} or {@code quantity} is null
     * @throws com.dynatrace.currency.nbpClient.exceptions.NoDataException         if request is valid but data not found
     * @throws com.dynatrace.currency.nbpClient.exceptions.InvalidRequestException if request is invalid
     */
    AverageExchange getAverageExchange(Currency currency, Integer quotations);


    /**
     * returns an object that contains list of {@link BuyAskExchange} and inserted currency code.
     *
     * @param currency Currency code in accordance with ISO 4217
     * @param quotations number of last quotations
     * @return the {@code BuyAskExchange} instance that contains requested rates and the currency code
     * @throws NullPointerException                                                if {@code currency} or {@code quantity} is null
     * @throws com.dynatrace.currency.nbpClient.exceptions.NoDataException         if request is valid but data not found
     * @throws com.dynatrace.currency.nbpClient.exceptions.InvalidRequestException if request is invalid
     */
    BuyAskExchange getBuyAskExchange(Currency currency, Integer quotations);
}
