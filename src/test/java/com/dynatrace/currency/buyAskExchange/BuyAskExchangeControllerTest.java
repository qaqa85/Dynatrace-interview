package com.dynatrace.currency.buyAskExchange;

import com.dynatrace.currency.buyAskExchange.dto.BuyAskDto;
import com.dynatrace.currency.buyAskExchange.dto.DifferenceDto;
import com.dynatrace.currency.buyAskExchange.dto.MajorDifferenceDto;
import com.dynatrace.currency.buyAskExchange.dto.MajorDifferenceDto.MajorDifferenceDtoBuilder;
import com.dynatrace.currency.utils.exceptions.InvalidCurrencyCodeException;
import com.dynatrace.currency.utils.exceptions.InvalidLastQuotationsNumberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BuyAskExchangeController.class)
class BuyAskExchangeControllerTest {
    private static final LocalDate DATE = LocalDate.of(2023, 4, 1);
    @Autowired
    MockMvc mockMvc;
    @MockBean
    BuyAskExchangeService service;

    @Test
    @DisplayName("should return BadRequest when CurrencyCode is incorrect")
    public void shouldReturnBadRequestWhenCurrencyCodeIsIncorrect() throws Exception {
        // GIVEN
        given(service.getMajorDifference(anyString(), anyString()))
                .willThrow(new InvalidCurrencyCodeException("Invalid code"));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/exchanges/buy-ask/invalidCode/last/2"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid code"));

    }

    @Test
    @DisplayName("should return BadRequest when quotation is invalid")
    public void shouldReturnBadRequestWhenQuotationIsInvalid() throws Exception {
        // GIVEN
        given(service.getMajorDifference(anyString(), anyString()))
                .willThrow(new InvalidLastQuotationsNumberException("Quotations number out of scope"));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/exchanges/buy-ask/USD/last/invalidNumber"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Quotations number out of scope"));
    }

    @Test
    @DisplayName("should return MajorDifferenceDto on successful")
    public void shouldReturnMajorDifferenceDtoOnSuccessful() throws Exception {
        // GIVEN
        given(service.getMajorDifference(anyString(), anyString()))
                .willReturn(getMajorDifferenceDto().build());

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/exchanges/buy-ask/USD/last/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.difference.difference").value(new BigDecimal("1.0")))
                .andExpect(jsonPath("$.difference.buy.value").value(new BigDecimal("2.0")))
                .andExpect(jsonPath("$.difference.ask.value").value(new BigDecimal("3.0")))
                .andExpect(jsonPath("$.difference.buy.date").value(DATE.toString()))
                .andExpect(jsonPath("$.difference.ask.date").value(DATE.toString()));

    }

    private MajorDifferenceDtoBuilder getMajorDifferenceDto() {
        return MajorDifferenceDto.builder()
                .currencyCode(Currency.getInstance("USD"))
                .difference(DifferenceDto.builder()
                        .difference(new BigDecimal("1.00"))
                        .ask(new BuyAskDto(DATE, new BigDecimal("3.00")))
                        .buy(new BuyAskDto(DATE, new BigDecimal("2.00")))
                        .build());
    }
}