package com.nuray.manufacturing.order.dto;

import com.nuray.manufacturing.order.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String orderNumber,
        UUID quoteId,
        BigDecimal totalPrice,
        OrderStatus status,
        String deliveryAddress,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
