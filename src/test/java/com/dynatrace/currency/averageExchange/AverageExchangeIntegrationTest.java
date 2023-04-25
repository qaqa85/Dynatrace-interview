package com.dynatrace.currency.averageExchange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AverageExchangeIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldReturnSingleCurrencyData() throws Exception {
        mockMvc.perform(get("/api/v1/exchanges/average/{currencyCode}/date/{date}",
                        "USD", "2023-04-20")
                        .content(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyCode").value("USD"));
    }

    @Test
    void shouldReturnMinMaxCurrencyDataInSpecificPeriod() throws Exception {
        mockMvc.perform(get("/api/v1/exchanges/average/{currencyCode}/last/{date}",
                        "USD", "3")
                        .content(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyCode").value("USD"));
    }
}
