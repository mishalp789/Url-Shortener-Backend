package com.mishalp789.url_shortener.url.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UrlResponse {

    private Long id;
    private String originalUrl;
    private String shortCode;
    private String shortUrl;
    private Long clickCount;
    private Boolean active;
    private String customAlias;
    private LocalDateTime expiresAt;


}
