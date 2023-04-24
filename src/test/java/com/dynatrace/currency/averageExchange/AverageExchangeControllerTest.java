package com.dynatrace.currency.averageExchange;

import com.dynatrace.currency.averageExchange.dto.AverageDto;
import com.dynatrace.currency.averageExchange.dto.SingleAverageExchangeDto;
import com.dynatrace.currency.averageExchange.dto.SingleAverageExchangeDto.SingleAverageExchangeDtoBuilder;
import com.dynatrace.currency.nbpClient.exceptions.NoDataException;
import com.dynatrace.currency.utils.exceptions.InvalidCurrencyCodeException;
import com.dynatrace.currency.utils.exceptions.InvalidDateException;
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
    private static final LocalDate DATE = LocalDate.of(2023, 4, 1);
    @Autowired
    MockMvc mockMvc;
    @MockBean
    AverageExchangeService service;

    @Test
    @DisplayName("should return bad request when CurrencyCode is incorrect")
    public void getSingleAverageExchangeDtoShouldReturnBadRequestWhenCurrencyCodeIsIncorrect() throws Exception {
        // GIVEN
        given(service.getSingleAverageExchangeDto(anyString(), anyString()))
                .willThrow(new InvalidCurrencyCodeException("Invalid code"));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/exchanges/average/invalidCode?date=2023.04.22"))
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
        mockMvc.perform(get("/api/v1/exchanges/average/USD?date=invalidDate"))
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
        mockMvc.perform(get("/api/v1/exchanges/average/USD?date=2023.04.22"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("no data"));

    }

    @Test
    @DisplayName("should return SingleAverageExchangeDto on successful")
    public void shouldReturnSingleAverageExchangeDtoOnSuccessful() throws Exception {
        // GIVEN
        given(service.getSingleAverageExchangeDto(anyString(), anyString()))
                .willReturn(getSingleAverageExchangeDto().build());

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/exchanges/average/USD?date=2023.04.22"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyCode").value("USD"))
                .andExpect(jsonPath("$.average.date").value(DATE.toString()))
                .andExpect(jsonPath("$.average.value").value(new BigDecimal("0.245")));
    }

    private SingleAverageExchangeDtoBuilder getSingleAverageExchangeDto() {
        return SingleAverageExchangeDto.builder()
                .currencyCode(Currency.getInstance("USD"))
                .average(new AverageDto(DATE, new BigDecimal("0.245")));
    }
}