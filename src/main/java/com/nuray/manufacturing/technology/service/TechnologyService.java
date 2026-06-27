package com.nuray.manufacturing.technology.service;

import com.nuray.manufacturing.common.exception.BusinessException;
import com.nuray.manufacturing.common.exception.ResourceNotFoundException;
import com.nuray.manufacturing.technology.dto.CreateTechnologyRequest;
import com.nuray.manufacturing.technology.dto.TechnologyResponse;
import com.nuray.manufacturing.technology.dto.UpdateTechnologyRequest;
import com.nuray.manufacturing.technology.entity.Technology;
import com.nuray.manufacturing.technology.repository.TechnologyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TechnologyService {

    private final TechnologyRepository technologyRepository;

    public TechnologyService(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    @Transactional(readOnly = true)
    public List<TechnologyResponse> getActiveTechnologies() {
        return technologyRepository.findAllByActiveTrueOrderByNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TechnologyResponse> getAllTechnologies() {
        return technologyRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TechnologyResponse getTechnology(UUID id) {
        return toResponse(findTechnology(id));
    }

    @Transactional
    public TechnologyResponse createTechnology(CreateTechnologyRequest request) {
        if (technologyRepository.existsByNameIgnoreCase(request.name())) {
            throw new BusinessException("Technology with this name already exists");
        }
        if (technologyRepository.existsByCode(request.code())) {
            throw new BusinessException("Technology with this code already exists");
        }

        Technology technology = new Technology();
        technology.setName(request.name().trim());
        technology.setCode(request.code());
        technology.setBasePrice(request.basePrice());
        technology.setActive(true);

        return toResponse(technologyRepository.save(technology));
    }

    @Transactional
    public TechnologyResponse updateTechnology(UUID id, UpdateTechnologyRequest request) {
        Technology technology = findTechnology(id);

        technologyRepository.findByNameIgnoreCase(request.name())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessException("Technology with this name already exists");
                });

        technologyRepository.findByCode(request.code())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessException("Technology with this code already exists");
                });

        technology.setName(request.name().trim());
        technology.setCode(request.code());
        technology.setBasePrice(request.basePrice());
        technology.setActive(request.active());

        return toResponse(technology);
    }

    @Transactional
    public void deactivateTechnology(UUID id) {
        Technology technology = findTechnology(id);
        technology.setActive(false);
    }

    private Technology findTechnology(UUID id) {
        return technologyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Technology not found"));
    }

    private TechnologyResponse toResponse(Technology technology) {
        return new TechnologyResponse(
                technology.getId(),
                technology.getName(),
                technology.getCode(),
                technology.getBasePrice(),
                technology.isActive()
        );
    }
}
