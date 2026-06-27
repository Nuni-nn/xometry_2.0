package com.nuray.manufacturing.quote.entity;

import com.nuray.manufacturing.material.entity.Material;
import com.nuray.manufacturing.quote.enums.QuoteStatus;
import com.nuray.manufacturing.quote.enums.SurfaceFinish;
import com.nuray.manufacturing.quote.enums.ToleranceLevel;
import com.nuray.manufacturing.technology.entity.Technology;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "quote_requests")
public class QuoteRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "technology_id", nullable = false)
    private Technology technology;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ToleranceLevel toleranceLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SurfaceFinish surfaceFinish;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuoteStatus status = QuoteStatus.DRAFT;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal estimatedPrice = BigDecimal.ZERO;

    @Column(length = 1000)
    private String engineerComment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Technology getTechnology() {
        return technology;
    }

    public void setTechnology(Technology technology) {
        this.technology = technology;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ToleranceLevel getToleranceLevel() {
        return toleranceLevel;
    }

    public void setToleranceLevel(ToleranceLevel toleranceLevel) {
        this.toleranceLevel = toleranceLevel;
    }

    public SurfaceFinish getSurfaceFinish() {
        return surfaceFinish;
    }

    public void setSurfaceFinish(SurfaceFinish surfaceFinish) {
        this.surfaceFinish = surfaceFinish;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public QuoteStatus getStatus() {
        return status;
    }

    public void setStatus(QuoteStatus status) {
        this.status = status;
    }

    public BigDecimal getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(BigDecimal estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public String getEngineerComment() {
        return engineerComment;
    }

    public void setEngineerComment(String engineerComment) {
        this.engineerComment = engineerComment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
