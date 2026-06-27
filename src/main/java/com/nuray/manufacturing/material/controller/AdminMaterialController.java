package com.nuray.manufacturing.material.controller;

import com.nuray.manufacturing.common.dto.ApiResponse;
import com.nuray.manufacturing.material.dto.CreateMaterialRequest;
import com.nuray.manufacturing.material.dto.MaterialResponse;
import com.nuray.manufacturing.material.dto.UpdateMaterialRequest;
import com.nuray.manufacturing.material.service.MaterialService;
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
@RequestMapping("/api/v1/admin/materials")
public class AdminMaterialController {

    private final MaterialService materialService;

    public AdminMaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping
    public ApiResponse<List<MaterialResponse>> getAllMaterials() {
        return ApiResponse.success("Materials retrieved successfully", materialService.getAllMaterials());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MaterialResponse> createMaterial(@Valid @RequestBody CreateMaterialRequest request) {
        return ApiResponse.success("Material created successfully", materialService.createMaterial(request));
    }

    @PatchMapping("/{id}")
    public ApiResponse<MaterialResponse> updateMaterial(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateMaterialRequest request
    ) {
        return ApiResponse.success("Material updated successfully", materialService.updateMaterial(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deactivateMaterial(@PathVariable UUID id) {
        materialService.deactivateMaterial(id);
        return ApiResponse.success("Material deactivated successfully", null);
    }
}
