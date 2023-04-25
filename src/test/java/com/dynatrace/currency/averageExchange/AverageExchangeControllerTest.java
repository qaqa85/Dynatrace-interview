package com.dynatrace.currency.averageExchange;

import com.dynatrace.currency.averageExchange.dto.AverageDto;
import com.dynatrace.currency.averageExchange.dto.MinMaxAverageDto;
import com.dynatrace.currency.averageExchange.dto.MinMaxAverageDto.MinMaxAverageDtoBuilder;
import com.dynatrace.currency.averageExchange.dto.SingleAverageDto;
import com.dynatrace.currency.averageExchange.dto.SingleAverageDto.SingleAverageDtoBuilder;
import com.dynatrace.currency.nbpClient.exceptions.NoDataException;
import com.dynatrace.currency.utils.exceptions.InvalidCurrencyCodeException;
import com.dynatrace.currency.utils.exceptions.InvalidDateException;
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

@WebMvcTest(AverageExchangeController.class)
class AverageExchangeControllerTest {
    private static final LocalDate DATE_FIRST = LocalDate.of(2023, 4, 1);
    private static final LocalDate DATE_SECOND = LocalDate.of(2023, 4, 2);
    @Autowired
    MockMvc mockMvc;
    @MockBean
    AverageExchangeService service;

    @Test
    @DisplayName("should return BadRequest when CurrencyCode is incorrect")
    public void getSingleAverageExchangeDtoShouldReturnBadRequestWhenCurrencyCodeIsIncorrect() throws Exception {
        // GIVEN
        given(service.getSingleAverageExchangeDto(anyString(), anyString()))
                .willThrow(new InvalidCurrencyCodeException("Invalid code"));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/exchanges/average/invalidCode/date/2023.04.22"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid code"));

    }

    @Test
    @DisplayName("should return BadRequest when date is invalid")
    public void getSingleAverageExchangeDtoShouldReturnBadRequestWhenDateIsInvalid() throws Exception {
        // GIVEN
        given(service.getSingleAverageExchangeDto(anyString(), anyString()))
                .willThrow(new InvalidDateException("Invalid date"));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/exchanges/average/USD/date/invalidDate"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid date"));
    }

    @Test
    @DisplayName("should return NotFound when no data for specific day")
    public void getSingleAverageExchangeDtoShouldReturnNotFoundWhenNoDataForSpecificDat() throws Exception {
        // GIVEN
        given(service.getSingleAverageExchangeDto(anyString(), anyString()))
                .willThrow(new NoDataException("no data"));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/exchanges/average/USD/date/2023.04.22"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("no data"));

    }

    @Test
    @DisplayName("should return SingleAverageExchangeDto on successful")
    public void shouldReturnSingleAverageExchangeDtoOnSuccessful() throws Exception {
        // GIVEN
        given(service.getSingleAverageExchangeDto(anyString(), anyString()))
                .willReturn(getSingleAverageDto().build());

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/exchanges/average/USD/date/2023.04.22"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyCode").value("USD"))
                .andExpect(jsonPath("$.average.date").value(DATE_FIRST.toString()))
                .andExpect(jsonPath("$.average.value").value(new BigDecimal("0.245")));
    }

    @Test
    @DisplayName("should return bad request when CurrencyCode is incorrect")
    public void getMinMaxAverageExchangeShouldReturnBadRequestWhenCurrencyCodeIsIncorrect() throws Exception {
        // GIVEN
        given(service.getMinMaxAverageDto(anyString(), anyString()))
                .willThrow(new InvalidCurrencyCodeException("Invalid code"));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/exchanges/average/invalidCode/last/3"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid code"));

    }

    @Test
    @DisplayName("should return BadRequest when quotation is invalid")
    public void getMinMaxAverageExchangeShouldReturnBadRequestWhenQuotationIsInvalid() throws Exception {
        // GIVEN
        given(service.getMinMaxAverageDto(anyString(), anyString()))
                .willThrow(new InvalidLastQuotationsNumberException("Quotations number out of scope"));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/exchanges/average/USD/last/invalidNumber"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Quotations number out of scope"));

    }

    @Test
    @DisplayName("should return MinMaxAverageDto on successful")
    public void shouldReturnMinMaxAverageDtoOnSuccessful() throws Exception {
        // GIVEN
        given(service.getMinMaxAverageDto(anyString(), anyString()))
                .willReturn(getMinMaxAverageDto().build());

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/exchanges/average/USD/last/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyCode").value("USD"))
                .andExpect(jsonPath("$.min.date").value(DATE_SECOND.toString()))
                .andExpect(jsonPath("$.min.value").value(new BigDecimal("1.123")))
                .andExpect(jsonPath("$.max.date").value(DATE_FIRST.toString()))
                .andExpect(jsonPath("$.max.value").value(new BigDecimal("1.451")));
    }


    private SingleAverageDtoBuilder getSingleAverageDto() {
        return SingleAverageDto.builder()
                .currencyCode(Currency.getInstance("USD"))
                .average(new AverageDto(DATE_FIRST, new BigDecimal("0.245")));
    }

    private MinMaxAverageDtoBuilder getMinMaxAverageDto() {
        return MinMaxAverageDto.builder()
                .currencyCode(Currency.getInstance("USD"))
                .min(new AverageDto(DATE_SECOND, new BigDecimal("1.123")))
                .max(new AverageDto(DATE_FIRST, new BigDecimal("1.451")));
    }
}