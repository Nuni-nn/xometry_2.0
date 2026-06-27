package com.nuray.manufacturing.material.service;

import com.nuray.manufacturing.common.exception.BusinessException;
import com.nuray.manufacturing.common.exception.ResourceNotFoundException;
import com.nuray.manufacturing.material.dto.CreateMaterialRequest;
import com.nuray.manufacturing.material.dto.MaterialResponse;
import com.nuray.manufacturing.material.dto.UpdateMaterialRequest;
import com.nuray.manufacturing.material.entity.Material;
import com.nuray.manufacturing.material.repository.MaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    @Transactional(readOnly = true)
    public List<MaterialResponse> getActiveMaterials() {
        return materialRepository.findAllByActiveTrueOrderByNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MaterialResponse> getAllMaterials() {
        return materialRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MaterialResponse getMaterial(UUID id) {
        return toResponse(findMaterial(id));
    }

    @Transactional
    public MaterialResponse createMaterial(CreateMaterialRequest request) {
        if (materialRepository.existsByNameIgnoreCase(request.name())) {
            throw new BusinessException("Material with this name already exists");
        }

        Material material = new Material();
        material.setName(request.name().trim());
        material.setCategory(request.category());
        material.setDensity(request.density());
        material.setPricePerKg(request.pricePerKg());
        material.setActive(true);

        return toResponse(materialRepository.save(material));
    }

    @Transactional
    public MaterialResponse updateMaterial(UUID id, UpdateMaterialRequest request) {
        Material material = findMaterial(id);

        materialRepository.findByNameIgnoreCase(request.name())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessException("Material with this name already exists");
                });

        material.setName(request.name().trim());
        material.setCategory(request.category());
        material.setDensity(request.density());
        material.setPricePerKg(request.pricePerKg());
        material.setActive(request.active());

        return toResponse(material);
    }

    @Transactional
    public void deactivateMaterial(UUID id) {
        Material material = findMaterial(id);
        material.setActive(false);
    }

    private Material findMaterial(UUID id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found"));
    }

    private MaterialResponse toResponse(Material material) {
        return new MaterialResponse(
                material.getId(),
                material.getName(),
                material.getCategory(),
                material.getDensity(),
                material.getPricePerKg(),
                material.isActive()
        );
    }
}
