package com.mishalp789.url_shortener.url.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUrlRequest {

    @NotBlank(message = "URL is required")
    @Pattern(
            regexp = "^(https?://).+$",
            message = "URL must start with http:// or https://"
    )
    private String originalUrl;

    @Size(
            min = 3,
            max = 100,
            message = "Alias must be between 3 and 100 characters"
    )
    @Pattern(
            regexp = "^[a-zA-Z0-9_-]*$",
            message = "Alias can contain only letters, numbers, hyphens and underscores"
    )
    private String customAlias;


}
