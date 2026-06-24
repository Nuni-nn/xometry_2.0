package com.nuray.manufacturing.common.dto;

import java.util.List;

public record ErrorResponse(
        boolean success,
        String message,
        List<FieldErrorResponse> errors
) {
    public static ErrorResponse of(String message, List<FieldErrorResponse> errors) {
        return new ErrorResponse(false, message, errors);
    }
}
