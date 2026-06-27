package com.nuray.manufacturing.order.dto;

import com.nuray.manufacturing.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequest(@NotNull OrderStatus status) {
}
