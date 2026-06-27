package com.nuray.manufacturing.quote.dto;

import com.nuray.manufacturing.quote.enums.SurfaceFinish;
import com.nuray.manufacturing.quote.enums.ToleranceLevel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateQuoteRequest(
        @NotNull(message = "Material id is required")
        UUID materialId,

        @NotNull(message = "Technology id is required")
        UUID technologyId,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity,

        @NotNull(message = "Tolerance level is required")
        ToleranceLevel toleranceLevel,

        @NotNull(message = "Surface finish is required")
        SurfaceFinish surfaceFinish,

        @Size(max = 1000, message = "Description must be shorter than 1000 characters")
        String description
) {
}
