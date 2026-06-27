package com.nuray.manufacturing.material.dto;

import com.nuray.manufacturing.material.entity.MaterialCategory;

import java.math.BigDecimal;
import java.util.UUID;

public record MaterialResponse(
        UUID id,
        String name,
        MaterialCategory category,
        BigDecimal density,
        BigDecimal pricePerKg,
        boolean active
) {
}
