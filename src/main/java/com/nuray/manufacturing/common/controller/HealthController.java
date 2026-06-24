package com.nuray.manufacturing.common.controller;

import com.nuray.manufacturing.common.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    @GetMapping
    public ApiResponse<Map<String, Object>> health() {
        return ApiResponse.success("Application is running", Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now()
        ));
    }
}
