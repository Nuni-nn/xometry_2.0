package com.nuray.manufacturing.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateOrderRequest(
        @NotNull UUID quoteId,
        @NotBlank @Size(max = 1000) String deliveryAddress
) {
}
