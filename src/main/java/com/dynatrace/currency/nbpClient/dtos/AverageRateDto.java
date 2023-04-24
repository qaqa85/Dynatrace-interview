package com.dynatrace.currency.nbpClient.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record AverageRateDto(LocalDate effectiveDate, @JsonProperty("mid") String averageValue) {
}
