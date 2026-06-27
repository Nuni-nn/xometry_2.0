package com.nuray.manufacturing.quote.dto;

import com.nuray.manufacturing.quote.enums.QuoteStatus;
import com.nuray.manufacturing.quote.enums.SurfaceFinish;
import com.nuray.manufacturing.quote.enums.ToleranceLevel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record QuoteResponse(
        UUID id,
        SimpleReference material,
        SimpleReference technology,
        Integer quantity,
        ToleranceLevel toleranceLevel,
        SurfaceFinish surfaceFinish,
        String description,
        QuoteStatus status,
        BigDecimal estimatedPrice,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
