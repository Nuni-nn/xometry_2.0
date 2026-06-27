package com.nuray.manufacturing.quote.controller;

import com.nuray.manufacturing.common.dto.ApiResponse;
import com.nuray.manufacturing.quote.dto.CreateQuoteRequest;
import com.nuray.manufacturing.quote.dto.QuoteResponse;
import com.nuray.manufacturing.quote.dto.ReviewQuoteRequest;
import com.nuray.manufacturing.quote.service.QuoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/quotes")
public class QuoteController {

    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<QuoteResponse> createQuote(@Valid @RequestBody CreateQuoteRequest request) {
        return ApiResponse.success("Quote request created successfully", quoteService.createQuote(request));
    }

    @GetMapping
    public ApiResponse<List<QuoteResponse>> getQuotes() {
        return ApiResponse.success("Quotes retrieved successfully", quoteService.getQuotes());
    }

    @GetMapping("/{id}")
    public ApiResponse<QuoteResponse> getQuote(@PathVariable UUID id) {
        return ApiResponse.success("Quote retrieved successfully", quoteService.getQuote(id));
    }

    @PostMapping("/{id}/submit")
    public ApiResponse<QuoteResponse> submitQuote(@PathVariable UUID id) {
        return ApiResponse.success("Quote request submitted successfully", quoteService.submitQuote(id));
    }

    @PostMapping("/{id}/calculate")
    public ApiResponse<QuoteResponse> calculateQuote(@PathVariable UUID id) {
        return ApiResponse.success("Quote price calculated successfully", quoteService.calculateQuote(id));
    }

    @PatchMapping("/{id}/review")
    public ApiResponse<QuoteResponse> reviewQuote(
            @PathVariable UUID id,
            @Valid @RequestBody ReviewQuoteRequest request
    ) {
        return ApiResponse.success("Quote reviewed successfully", quoteService.reviewQuote(id, request));
    }

    @PostMapping("/{id}/approve")
    public ApiResponse<QuoteResponse> approveQuote(@PathVariable UUID id) {
        return ApiResponse.success("Quote approved successfully", quoteService.approveQuote(id));
    }
}
