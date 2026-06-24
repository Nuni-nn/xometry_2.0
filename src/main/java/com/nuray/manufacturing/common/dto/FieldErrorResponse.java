package com.nuray.manufacturing.common.dto;

public record FieldErrorResponse(
        String field,
        String message
) {
}
