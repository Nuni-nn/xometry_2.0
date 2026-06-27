package com.nuray.manufacturing.technology.dto;

import com.nuray.manufacturing.technology.entity.TechnologyCode;

import java.math.BigDecimal;
import java.util.UUID;

public record TechnologyResponse(
        UUID id,
        String name,
        TechnologyCode code,
        BigDecimal basePrice,
        boolean active
) {
}
