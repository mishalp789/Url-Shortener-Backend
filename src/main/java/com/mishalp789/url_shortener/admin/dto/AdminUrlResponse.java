package com.mishalp789.url_shortener.admin.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AdminUrlResponse {

    private Long id;
    private String originalUrl;
    private String shortCode;
    private String customAlias;
    private String owner;
    private Long clickCount;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

}