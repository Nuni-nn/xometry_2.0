package com.nuray.manufacturing.material.controller;

import com.nuray.manufacturing.common.dto.ApiResponse;
import com.nuray.manufacturing.material.dto.MaterialResponse;
import com.nuray.manufacturing.material.service.MaterialService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping
    public ApiResponse<List<MaterialResponse>> getMaterials() {
        return ApiResponse.success("Materials retrieved successfully", materialService.getActiveMaterials());
    }

    @GetMapping("/{id}")
    public ApiResponse<MaterialResponse> getMaterial(@PathVariable UUID id) {
        return ApiResponse.success("Material retrieved successfully", materialService.getMaterial(id));
    }
}
