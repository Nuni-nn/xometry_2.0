package com.nuray.manufacturing.order.service;

import com.nuray.manufacturing.common.exception.BusinessException;
import com.nuray.manufacturing.order.dto.CreateOrderRequest;
import com.nuray.manufacturing.order.dto.OrderResponse;
import com.nuray.manufacturing.order.entity.ManufacturingOrder;
import com.nuray.manufacturing.order.enums.OrderStatus;
import com.nuray.manufacturing.order.repository.ManufacturingOrderRepository;
import com.nuray.manufacturing.quote.entity.QuoteRequest;
import com.nuray.manufacturing.quote.enums.QuoteStatus;
import com.nuray.manufacturing.quote.repository.QuoteRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ManufacturingOrderRepository orderRepository;

    @Mock
    private QuoteRequestRepository quoteRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrderRejectsQuoteThatIsNotApproved() {
        UUID quoteId = UUID.randomUUID();
        QuoteRequest quote = quote(quoteId, QuoteStatus.SUBMITTED);
        when(quoteRepository.findById(quoteId)).thenReturn(Optional.of(quote));

        CreateOrderRequest request = new CreateOrderRequest(quoteId, "Almaty, Abay street 10");

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> orderService.createOrder(request)
        );

        assertEquals("Only approved quotes can be converted to orders", exception.getMessage());
        verify(orderRepository, never()).saveAndFlush(any());
    }

    @Test
    void createOrderRejectsDuplicateQuote() {
        UUID quoteId = UUID.randomUUID();
        QuoteRequest quote = quote(quoteId, QuoteStatus.APPROVED);
        when(quoteRepository.findById(quoteId)).thenReturn(Optional.of(quote));
        when(orderRepository.existsByQuoteRequestId(quoteId)).thenReturn(true);

        CreateOrderRequest request = new CreateOrderRequest(quoteId, "Almaty, Abay street 10");

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> orderService.createOrder(request)
        );

        assertEquals("An order already exists for this quote", exception.getMessage());
        verify(orderRepository, never()).saveAndFlush(any());
    }

    @Test
    void updateStatusRejectsSkippedProductionStep() {
        UUID orderId = UUID.randomUUID();
        ManufacturingOrder order = order(orderId, OrderStatus.NEW);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> orderService.updateStatus(orderId, OrderStatus.QUALITY_CHECK)
        );

        assertEquals("Order status cannot change from NEW to QUALITY_CHECK", exception.getMessage());
        verify(orderRepository, never()).saveAndFlush(any());
    }

    @Test
    void updateStatusAllowsNextProductionStep() {
        UUID orderId = UUID.randomUUID();
        ManufacturingOrder order = order(orderId, OrderStatus.NEW);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.saveAndFlush(order)).thenReturn(order);

        OrderResponse response = orderService.updateStatus(orderId, OrderStatus.IN_PRODUCTION);

        assertEquals(OrderStatus.IN_PRODUCTION, response.status());
        verify(orderRepository).saveAndFlush(order);
    }

    private QuoteRequest quote(UUID id, QuoteStatus status) {
        QuoteRequest quote = new QuoteRequest();
        ReflectionTestUtils.setField(quote, "id", id);
        quote.setStatus(status);
        quote.setEstimatedPrice(BigDecimal.valueOf(180));
        return quote;
    }

    private ManufacturingOrder order(UUID id, OrderStatus status) {
        ManufacturingOrder order = new ManufacturingOrder();
        ReflectionTestUtils.setField(order, "id", id);
        order.setQuoteRequest(quote(UUID.randomUUID(), QuoteStatus.APPROVED));
        order.setOrderNumber("ORD-2026-TEST0001");
        order.setTotalPrice(BigDecimal.valueOf(180));
        order.setDeliveryAddress("Almaty, Abay street 10");
        order.setStatus(status);
        return order;
    }
}
