package com.nuray.manufacturing.order.repository;

import com.nuray.manufacturing.order.entity.ManufacturingOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ManufacturingOrderRepository extends JpaRepository<ManufacturingOrder, UUID> {

    boolean existsByQuoteRequestId(UUID quoteId);
}
