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
    @Operation(
            summary = "Create a new shortened URL",
            description = "Creates a new short URL for the authenticated user. Supports custom aliases, expiration dates, and other URL creation options."
    )
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
    @Operation(
            summary = "Retrieve all URLs",
            description = "Returns a paginated list of URLs created by the authenticated user. Supports searching by alias or original URL, along with pagination and sorting."
    )
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
    @Operation(
            summary = "Retrieve URL details",
            description = "Fetches detailed information about a specific shortened URL owned by the authenticated user, including metadata and current status."
    )
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
    @Operation(
            summary = "Delete a shortened URL",
            description = "Permanently deletes a shortened URL owned by the authenticated user. This action cannot be undone."
    )
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
    @Operation(
            summary = "Update URL status",
            description = "Enables or disables a shortened URL. Disabled URLs will no longer redirect until they are re-enabled."
    )
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
    @Operation(
            summary = "Retrieve URL analytics",
            description = "Returns analytics for a specific shortened URL, including total clicks, recent activity, and other tracking metrics."
    )
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
    @Operation(
            summary = "Retrieve dashboard statistics",
            description = "Returns an overview of the authenticated user's account, including total URLs, total clicks, active links, and other dashboard metrics."
    )
    @GetMapping("/dashboard")
    public ApiResponse<DashboardResponse> dashboard(
            Authentication authentication) {

        return ApiResponse.<DashboardResponse>builder()
                .success(true)
                .message("Dashboard retrieved successfully")
                .data(urlService.getDashboard(authentication))
                .build();
    }

    @Operation(
            summary = "Check custom alias availability",
            description = "Checks whether a custom alias is available for use before creating a new shortened URL."
    )
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
