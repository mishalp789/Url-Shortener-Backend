package com.mishalp789.url_shortener.admin.controller;

import com.mishalp789.url_shortener.admin.dto.AdminUpdateUrlStatusRequest;
import com.mishalp789.url_shortener.admin.dto.AdminUrlResponse;
import com.mishalp789.url_shortener.admin.dto.AdminUserResponse;
import com.mishalp789.url_shortener.admin.dto.PlatformDashboardResponse;
import com.mishalp789.url_shortener.admin.service.AdminService;
import com.mishalp789.url_shortener.common.response.ApiResponse;
import com.mishalp789.url_shortener.url.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "Platform dashboard")
    @GetMapping("/dashboard")
    public ApiResponse<PlatformDashboardResponse> dashboard(){
        return ApiResponse.<PlatformDashboardResponse>builder()
                .success(true)
                .message("Platform dashboard retrieved successfully")
                .data(adminService.dashboard())
                .build();
    }

    @Operation(summary = "Get all users")
    @GetMapping("/users")
    public ApiResponse<PageResponse<AdminUserResponse>> users(

            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ){

        return ApiResponse.<PageResponse<AdminUserResponse>>builder()
                .success(true)
                .message("Users retrieved successfully")
                .data(adminService.getUsers(pageable))
                .build();

    }

    @Operation(summary = "Get all URLs")
    @GetMapping("/urls")
    public ApiResponse<PageResponse<AdminUrlResponse>> urls(
            @RequestParam(required = false)
            String search,

            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ){
        return ApiResponse.<PageResponse<AdminUrlResponse>>builder()
                .success(true)
                .message("URLs retrieved successfully")
                .data(adminService.getUrls(search,pageable))
                .build();
    }

    @Operation(summary = "Update URL status")
    @PatchMapping("/urls/{id}/status")
    public ApiResponse<Void> updateStatus(
            @PathVariable Long id,
            @RequestBody
            AdminUpdateUrlStatusRequest request
    ){
        adminService.updateUrlStatus(id,request.getActive());

        return ApiResponse.<Void>builder()
                .success(true)
                .message("URL status updated")
                .build();

    }

    @Operation(summary = "Delete URL")
    @DeleteMapping("/urls/{id}")
    public ApiResponse<Void> deleteUrl(
            @PathVariable Long id){

        adminService.deleteUrl(id);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("URL deleted successfully")
                .build();

    }



}
