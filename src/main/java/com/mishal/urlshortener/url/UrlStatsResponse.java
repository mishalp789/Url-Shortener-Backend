package com.mishal.urlshortener.url;

import java.time.LocalDateTime;

public class UrlStatsResponse {
    private String shortCode;
    private String originalUrl;
    private long clickCount;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;


    public UrlStatsResponse(String shortCode,
                            String originalUrl,
                            long clickCount,
                            LocalDateTime createdAt,
                            LocalDateTime expiresAt){
                                this.shortCode = shortCode;
                                this.originalUrl = originalUrl;
                                this.clickCount = clickCount;
                                this.createdAt = createdAt;
                                this.expiresAt = expiresAt;
    }
    public String getShortCode() {
        return shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public long getClickCount() {
        return clickCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}
