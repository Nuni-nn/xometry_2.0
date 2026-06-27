package com.nuray.manufacturing.technology.dto;

import com.nuray.manufacturing.technology.entity.TechnologyCode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateTechnologyRequest(
        @NotBlank(message = "Technology name is required")
        String name,

        @NotNull(message = "Technology code is required")
        TechnologyCode code,

        @NotNull(message = "Base price is required")
        @DecimalMin(value = "0.01", message = "Base price must be greater than 0")
        BigDecimal basePrice,

        @NotNull(message = "Active flag is required")
        Boolean active
) {
}
