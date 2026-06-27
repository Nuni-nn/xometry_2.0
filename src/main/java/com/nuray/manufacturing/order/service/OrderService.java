package com.nuray.manufacturing.order.service;

import com.nuray.manufacturing.common.exception.BusinessException;
import com.nuray.manufacturing.common.exception.ResourceNotFoundException;
import com.nuray.manufacturing.order.dto.CreateOrderRequest;
import com.nuray.manufacturing.order.dto.OrderResponse;
import com.nuray.manufacturing.order.entity.ManufacturingOrder;
import com.nuray.manufacturing.order.enums.OrderStatus;
import com.nuray.manufacturing.order.repository.ManufacturingOrderRepository;
import com.nuray.manufacturing.quote.entity.QuoteRequest;
import com.nuray.manufacturing.quote.enums.QuoteStatus;
import com.nuray.manufacturing.quote.repository.QuoteRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final ManufacturingOrderRepository orderRepository;
    private final QuoteRequestRepository quoteRepository;

    public OrderService(
            ManufacturingOrderRepository orderRepository,
            QuoteRequestRepository quoteRepository
    ) {
        this.orderRepository = orderRepository;
        this.quoteRepository = quoteRepository;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        QuoteRequest quote = quoteRepository.findById(request.quoteId())
                .orElseThrow(() -> new ResourceNotFoundException("Quote not found"));

        if (quote.getStatus() != QuoteStatus.APPROVED) {
            throw new BusinessException("Only approved quotes can be converted to orders");
        }
        if (orderRepository.existsByQuoteRequestId(quote.getId())) {
            throw new BusinessException("An order already exists for this quote");
        }

        ManufacturingOrder order = new ManufacturingOrder();
        order.setQuoteRequest(quote);
        order.setOrderNumber(generateOrderNumber());
        order.setTotalPrice(quote.getEstimatedPrice());
        order.setStatus(OrderStatus.NEW);
        order.setDeliveryAddress(request.deliveryAddress().trim());

        return toResponse(orderRepository.saveAndFlush(order));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders() {
        return orderRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(UUID id) {
        return toResponse(findOrder(id));
    }

    @Transactional
    public OrderResponse updateStatus(UUID id, OrderStatus newStatus) {
        ManufacturingOrder order = findOrder(id);
        if (!isAllowedTransition(order.getStatus(), newStatus)) {
            throw new BusinessException(
                    "Order status cannot change from " + order.getStatus() + " to " + newStatus
            );
        }

        order.setStatus(newStatus);
        return toResponse(orderRepository.saveAndFlush(order));
    }

    private ManufacturingOrder findOrder(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    private boolean isAllowedTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        if (newStatus == OrderStatus.CANCELLED) {
            return currentStatus == OrderStatus.NEW
                    || currentStatus == OrderStatus.IN_PRODUCTION
                    || currentStatus == OrderStatus.QUALITY_CHECK;
        }

        return switch (currentStatus) {
            case NEW -> newStatus == OrderStatus.IN_PRODUCTION;
            case IN_PRODUCTION -> newStatus == OrderStatus.QUALITY_CHECK;
            case QUALITY_CHECK -> newStatus == OrderStatus.SHIPPED;
            case SHIPPED -> newStatus == OrderStatus.COMPLETED;
            case COMPLETED, CANCELLED -> false;
        };
    }

    private String generateOrderNumber() {
        String suffix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "ORD-" + Year.now().getValue() + "-" + suffix;
    }

    private OrderResponse toResponse(ManufacturingOrder order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getQuoteRequest().getId(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getDeliveryAddress(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
