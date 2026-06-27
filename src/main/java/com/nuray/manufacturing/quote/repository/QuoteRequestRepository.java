package com.nuray.manufacturing.quote.repository;

import com.nuray.manufacturing.quote.entity.QuoteRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuoteRequestRepository extends JpaRepository<QuoteRequest, UUID> {
}
