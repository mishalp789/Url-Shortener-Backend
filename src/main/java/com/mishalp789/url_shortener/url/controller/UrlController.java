package com.mishalp789.url_shortener.url.controller;

import com.mishalp789.url_shortener.common.response.ApiResponse;
import com.mishalp789.url_shortener.url.dto.*;
import com.mishalp789.url_shortener.url.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

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

    @GetMapping("/dashboard")
    public ApiResponse<DashboardResponse> dashboard(
            Authentication authentication) {

        return ApiResponse.<DashboardResponse>builder()
                .success(true)
                .message("Dashboard retrieved successfully")
                .data(urlService.getDashboard(authentication))
                .build();
    }

}
