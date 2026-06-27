package com.nuray.manufacturing.quote.dto;

import java.util.UUID;

public record SimpleReference(
        UUID id,
        String name
) {
}
