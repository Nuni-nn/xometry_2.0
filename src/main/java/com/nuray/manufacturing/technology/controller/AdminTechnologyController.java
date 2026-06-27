package com.nuray.manufacturing.technology.controller;

import com.nuray.manufacturing.common.dto.ApiResponse;
import com.nuray.manufacturing.technology.dto.CreateTechnologyRequest;
import com.nuray.manufacturing.technology.dto.TechnologyResponse;
import com.nuray.manufacturing.technology.dto.UpdateTechnologyRequest;
import com.nuray.manufacturing.technology.service.TechnologyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/v1/admin/technologies")
public class AdminTechnologyController {

    private final TechnologyService technologyService;

    public AdminTechnologyController(TechnologyService technologyService) {
        this.technologyService = technologyService;
    }

    @GetMapping
    public ApiResponse<List<TechnologyResponse>> getAllTechnologies() {
        return ApiResponse.success("Technologies retrieved successfully", technologyService.getAllTechnologies());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TechnologyResponse> createTechnology(@Valid @RequestBody CreateTechnologyRequest request) {
        return ApiResponse.success("Technology created successfully", technologyService.createTechnology(request));
    }

    @PatchMapping("/{id}")
    public ApiResponse<TechnologyResponse> updateTechnology(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTechnologyRequest request
    ) {
        return ApiResponse.success("Technology updated successfully", technologyService.updateTechnology(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deactivateTechnology(@PathVariable UUID id) {
        technologyService.deactivateTechnology(id);
        return ApiResponse.success("Technology deactivated successfully", null);
    }
}
