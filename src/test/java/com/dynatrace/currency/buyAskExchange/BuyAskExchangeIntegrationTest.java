package com.dynatrace.currency.buyAskExchange;

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
public class BuyAskExchangeIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnRequestedDataFromNBP() throws Exception {
        mockMvc.perform(get("/api/v1/buy-ask/{currencyCode}", "USD")
                .content(MediaType.APPLICATION_JSON_VALUE)
                .param("last", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyCode").value("USD"));
    }
}
