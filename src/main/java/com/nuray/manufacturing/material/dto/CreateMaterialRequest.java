package com.nuray.manufacturing.material.dto;

import com.nuray.manufacturing.material.entity.MaterialCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateMaterialRequest(
        @NotBlank(message = "Material name is required")
        String name,

        @NotNull(message = "Material category is required")
        MaterialCategory category,

        @NotNull(message = "Density is required")
        @DecimalMin(value = "0.001", message = "Density must be greater than 0")
        BigDecimal density,

        @NotNull(message = "Price per kg is required")
        @DecimalMin(value = "0.01", message = "Price per kg must be greater than 0")
        BigDecimal pricePerKg
) {
}
