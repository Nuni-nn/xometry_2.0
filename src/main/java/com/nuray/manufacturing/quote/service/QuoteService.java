package com.nuray.manufacturing.quote.service;

import com.nuray.manufacturing.common.exception.BusinessException;
import com.nuray.manufacturing.common.exception.ResourceNotFoundException;
import com.nuray.manufacturing.material.entity.Material;
import com.nuray.manufacturing.material.repository.MaterialRepository;
import com.nuray.manufacturing.quote.dto.CreateQuoteRequest;
import com.nuray.manufacturing.quote.dto.QuoteResponse;
import com.nuray.manufacturing.quote.dto.ReviewQuoteRequest;
import com.nuray.manufacturing.quote.dto.SimpleReference;
import com.nuray.manufacturing.quote.entity.QuoteRequest;
import com.nuray.manufacturing.quote.enums.QuoteStatus;
import com.nuray.manufacturing.quote.repository.QuoteRequestRepository;
import com.nuray.manufacturing.technology.entity.Technology;
import com.nuray.manufacturing.technology.repository.TechnologyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
public class QuoteService {

    private final QuoteRequestRepository quoteRequestRepository;
    private final MaterialRepository materialRepository;
    private final TechnologyRepository technologyRepository;

    public QuoteService(
            QuoteRequestRepository quoteRequestRepository,
            MaterialRepository materialRepository,
            TechnologyRepository technologyRepository
    ) {
        this.quoteRequestRepository = quoteRequestRepository;
        this.materialRepository = materialRepository;
        this.technologyRepository = technologyRepository;
    }

    @Transactional
    public QuoteResponse createQuote(CreateQuoteRequest request) {
        Material material = materialRepository.findById(request.materialId())
                .orElseThrow(() -> new ResourceNotFoundException("Material not found"));
        Technology technology = technologyRepository.findById(request.technologyId())
                .orElseThrow(() -> new ResourceNotFoundException("Technology not found"));

        if (!material.isActive()) {
            throw new BusinessException("Material is not active");
        }
        if (!technology.isActive()) {
            throw new BusinessException("Technology is not active");
        }

        QuoteRequest quote = new QuoteRequest();
        quote.setMaterial(material);
        quote.setTechnology(technology);
        quote.setQuantity(request.quantity());
        quote.setToleranceLevel(request.toleranceLevel());
        quote.setSurfaceFinish(request.surfaceFinish());
        quote.setDescription(request.description());
        quote.setStatus(QuoteStatus.DRAFT);
        quote.setEstimatedPrice(calculateEstimatedPrice(quote));

        return toResponse(quoteRequestRepository.saveAndFlush(quote));
    }

    @Transactional(readOnly = true)
    public List<QuoteResponse> getQuotes() {
        return quoteRequestRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public QuoteResponse getQuote(UUID id) {
        return toResponse(findQuote(id));
    }

    @Transactional
    public QuoteResponse submitQuote(UUID id) {
        QuoteRequest quote = findQuote(id);
        if (quote.getStatus() != QuoteStatus.DRAFT) {
            throw new BusinessException("Only draft quotes can be submitted");
        }
        quote.setStatus(QuoteStatus.SUBMITTED);
        return toResponse(quoteRequestRepository.saveAndFlush(quote));
    }

    @Transactional
    public QuoteResponse calculateQuote(UUID id) {
        QuoteRequest quote = findQuote(id);
        quote.setEstimatedPrice(calculateEstimatedPrice(quote));
        return toResponse(quoteRequestRepository.saveAndFlush(quote));
    }

    @Transactional
    public QuoteResponse reviewQuote(UUID id, ReviewQuoteRequest request) {
        QuoteRequest quote = findQuote(id);
        if (quote.getStatus() != QuoteStatus.SUBMITTED
                && quote.getStatus() != QuoteStatus.UNDER_REVIEW) {
            throw new BusinessException("Only submitted quotes can be reviewed");
        }
        if (request.status() != QuoteStatus.UNDER_REVIEW
                && request.status() != QuoteStatus.QUOTED
                && request.status() != QuoteStatus.REJECTED) {
            throw new BusinessException("Review status must be UNDER_REVIEW, QUOTED, or REJECTED");
        }
        if (request.status() == QuoteStatus.QUOTED && request.estimatedPrice() == null) {
            throw new BusinessException("Estimated price is required for a quoted request");
        }

        if (request.estimatedPrice() != null) {
            quote.setEstimatedPrice(request.estimatedPrice().setScale(2, RoundingMode.HALF_UP));
        }
        quote.setEngineerComment(request.engineerComment());
        quote.setStatus(request.status());
        return toResponse(quoteRequestRepository.saveAndFlush(quote));
    }

    @Transactional
    public QuoteResponse approveQuote(UUID id) {
        QuoteRequest quote = findQuote(id);
        if (quote.getStatus() != QuoteStatus.QUOTED) {
            throw new BusinessException("Only quoted requests can be approved");
        }
        quote.setStatus(QuoteStatus.APPROVED);
        return toResponse(quoteRequestRepository.saveAndFlush(quote));
    }

    private QuoteRequest findQuote(UUID id) {
        return quoteRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quote not found"));
    }

    private BigDecimal calculateEstimatedPrice(QuoteRequest quote) {
        BigDecimal estimatedWeight = BigDecimal.valueOf(1.5);
        BigDecimal materialCost = quote.getMaterial().getPricePerKg().multiply(estimatedWeight);
        BigDecimal quantityCost = BigDecimal.valueOf(quote.getQuantity()).multiply(BigDecimal.TEN);
        BigDecimal toleranceCost = switch (quote.getToleranceLevel()) {
            case STANDARD -> BigDecimal.ZERO;
            case PRECISION -> BigDecimal.valueOf(35);
            case HIGH_PRECISION -> BigDecimal.valueOf(75);
        };
        BigDecimal finishCost = switch (quote.getSurfaceFinish()) {
            case RAW -> BigDecimal.ZERO;
            case ANODIZED, PAINTED -> BigDecimal.valueOf(25);
            case POLISHED, POWDER_COATED -> BigDecimal.valueOf(40);
        };

        return quote.getTechnology().getBasePrice()
                .add(materialCost)
                .add(quantityCost)
                .add(toleranceCost)
                .add(finishCost)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private QuoteResponse toResponse(QuoteRequest quote) {
        return new QuoteResponse(
                quote.getId(),
                new SimpleReference(quote.getMaterial().getId(), quote.getMaterial().getName()),
                new SimpleReference(quote.getTechnology().getId(), quote.getTechnology().getName()),
                quote.getQuantity(),
                quote.getToleranceLevel(),
                quote.getSurfaceFinish(),
                quote.getDescription(),
                quote.getStatus(),
                quote.getEstimatedPrice(),
                quote.getEngineerComment(),
                quote.getCreatedAt(),
                quote.getUpdatedAt()
        );
    }
}
