package com.mishalp789.url_shortener.admin.controller;

import com.mishalp789.url_shortener.admin.dto.PlatformDashboardResponse;
import com.mishalp789.url_shortener.admin.service.AdminService;
import com.mishalp789.url_shortener.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    @Operation(summary = "Platform dashboard")
    public ApiResponse<PlatformDashboardResponse> dashboard(){
        return ApiResponse.<PlatformDashboardResponse>builder()
                .success(true)
                .message("Platform dashboard retrieved successfully")
                .data(adminService.dashboard())
                .build();
    }

}
