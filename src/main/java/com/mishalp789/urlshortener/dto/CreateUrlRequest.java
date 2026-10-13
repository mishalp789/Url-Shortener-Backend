package com.mishalp789.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateUrlRequest(

        @NotBlank(message = "Url is required")
        @Pattern(
                regexp = "^https?://.+$",
                message = "URL must start with http:// or https://"
        )
        String originalUrl

) {}
