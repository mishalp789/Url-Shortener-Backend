package com.mishalp789.url_shortener.url.controller;

import com.mishalp789.url_shortener.common.response.ApiResponse;
import com.mishalp789.url_shortener.url.dto.*;
import com.mishalp789.url_shortener.url.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@Tag(name = "URL Management")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;
    @Operation(summary = "Create Short URL")
    @PostMapping
    public ApiResponse<UrlResponse> create(
            @Valid @RequestBody CreateUrlRequest request,
            Authentication authentication
            ){
        return ApiResponse.<UrlResponse>builder()
                .success(true)
                .message("Short URL created successfully")
                .data(urlService.createShortUrl(request,authentication))
                .build();
    }
    @Operation(summary = "List User URLs")
    @GetMapping
    public ApiResponse<PageResponse<UrlResponse>> getMyUrls(
            Authentication authentication,
            @RequestParam(required = false) String search,
            @PageableDefault(
                    size=10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
    )Pageable pageable){
        return ApiResponse.<PageResponse<UrlResponse>>builder()
                .success(true)
                .message("URLs retrieved successfully")
                .data(urlService.getMyUrls(authentication,search,pageable))
                .build();
    }
    @Operation(summary = "Get URL Details")
    @GetMapping("/{id}")
    public ApiResponse<UrlResponse> getUrl(
            @PathVariable Long id,
            Authentication authentication) {

        return ApiResponse.<UrlResponse>builder()
                .success(true)
                .message("URL retrieved successfully")
                .data(urlService.getUrl(id, authentication))
                .build();

    }
    @Operation(summary = "Delete URL")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUrl(
            @PathVariable Long id,
            Authentication authentication) {

        urlService.deleteUrl(id, authentication);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("URL deleted successfully")
                .build();

    }
    @Operation(summary = "Update URL Status")
    @PatchMapping("/{id}/status")
    public ApiResponse<UrlResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUrlStatusRequest request,
            Authentication authentication) {

        return ApiResponse.<UrlResponse>builder()
                .success(true)
                .message("URL status updated successfully")
                .data(urlService.updateStatus(id, request, authentication))
                .build();

    }
    @Operation(summary = "Get URL Analytics")
    @GetMapping("/{id}/analytics")
    public ApiResponse<UrlAnalyticsResponse> getAnalytics(
            @PathVariable Long id,
            Authentication authentication) {

        return ApiResponse.<UrlAnalyticsResponse>builder()
                .success(true)
                .message("Analytics retrieved successfully")
                .data(urlService.getAnalytics(id, authentication))
                .build();
    }
    @Operation(summary = "Get Dashboard")
    @GetMapping("/dashboard")
    public ApiResponse<DashboardResponse> dashboard(
            Authentication authentication) {

        return ApiResponse.<DashboardResponse>builder()
                .success(true)
                .message("Dashboard retrieved successfully")
                .data(urlService.getDashboard(authentication))
                .build();
    }

    @Operation(summary = "Check alias availability")
    @GetMapping("/check-alias")
    public ApiResponse<AliasAvailabilityResponse> checkAlias(
            @RequestParam String alias
    ){
        return ApiResponse.<AliasAvailabilityResponse>builder()
                .success(true)
                .message("Alias availability checked")
                .data(urlService.checkAliasAvailability(alias))
                .build();
    }

}
