package com.mishalp789.url_shortener.url.controller;

import com.mishalp789.url_shortener.common.response.ApiResponse;
import com.mishalp789.url_shortener.url.dto.CreateUrlRequest;
import com.mishalp789.url_shortener.url.dto.UrlResponse;
import com.mishalp789.url_shortener.url.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
