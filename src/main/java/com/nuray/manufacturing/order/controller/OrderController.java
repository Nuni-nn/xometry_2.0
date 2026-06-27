package com.nuray.manufacturing.order.controller;

import com.nuray.manufacturing.common.dto.ApiResponse;
import com.nuray.manufacturing.order.dto.CreateOrderRequest;
import com.nuray.manufacturing.order.dto.OrderResponse;
import com.nuray.manufacturing.order.dto.UpdateOrderStatusRequest;
import com.nuray.manufacturing.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return ApiResponse.success("Order created successfully", orderService.createOrder(request));
    }

    @GetMapping({"", "/my"})
    public ApiResponse<List<OrderResponse>> getOrders() {
        return ApiResponse.success("Orders retrieved successfully", orderService.getOrders());
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> getOrder(@PathVariable UUID id) {
        return ApiResponse.success("Order retrieved successfully", orderService.getOrder(id));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<OrderResponse> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        return ApiResponse.success(
                "Order status updated successfully",
                orderService.updateStatus(id, request.status())
        );
    }
}
