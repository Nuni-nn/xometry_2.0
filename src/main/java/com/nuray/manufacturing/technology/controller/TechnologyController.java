package com.nuray.manufacturing.technology.controller;

import com.nuray.manufacturing.common.dto.ApiResponse;
import com.nuray.manufacturing.technology.dto.TechnologyResponse;
import com.nuray.manufacturing.technology.service.TechnologyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/technologies")
public class TechnologyController {

    private final TechnologyService technologyService;

    public TechnologyController(TechnologyService technologyService) {
        this.technologyService = technologyService;
    }

    @GetMapping
    public ApiResponse<List<TechnologyResponse>> getTechnologies() {
        return ApiResponse.success("Technologies retrieved successfully", technologyService.getActiveTechnologies());
    }

    @GetMapping("/{id}")
    public ApiResponse<TechnologyResponse> getTechnology(@PathVariable UUID id) {
        return ApiResponse.success("Technology retrieved successfully", technologyService.getTechnology(id));
    }
}
