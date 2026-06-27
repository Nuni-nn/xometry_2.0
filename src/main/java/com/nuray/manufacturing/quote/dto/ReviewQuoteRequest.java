package com.nuray.manufacturing.quote.dto;

import com.nuray.manufacturing.quote.enums.QuoteStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ReviewQuoteRequest(
        @NotNull QuoteStatus status,
        @DecimalMin(value = "0.01", message = "Estimated price must be greater than zero")
        BigDecimal estimatedPrice,
        @Size(max = 1000) String engineerComment
) {
}
