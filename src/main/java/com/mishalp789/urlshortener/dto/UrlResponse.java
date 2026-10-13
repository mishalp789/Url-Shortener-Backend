package com.mishalp789.urlshortener.dto;

import java.time.LocalDateTime;

public record UrlResponse(
        Long id,
        String originalUrl,
        String shortCode,
        LocalDateTime createdAt,
        LocalDateTime expiresAt,
        Long clickCount,
        Boolean isActive
) {
}
