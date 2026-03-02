package com.mishal.urlshortener.url;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ShortenRequest {
    @NotBlank(message="Url must not be blank")
    @Size(max=2048, message="URL length must be less than 2048 characters")
    @Pattern(regexp = "^(http|https)://.*$",message = "URL must start with http:// or https://")
    private String url;
    @Size(min = 3,max = 20,message = "Alias must be between 3 and 20 characters")
    @Pattern(
        regexp = "^[a-zA-Z0-9_-]*$",
        message = "Alias can contain only letters, numbers, hyphen and underscore"
    )
    private String customAlias;
    

    private LocalDateTime expiresAt;

    public LocalDateTime getExpiresAt(){
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt){
        this.expiresAt = expiresAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCustomAlias(){
        return customAlias;
    }
    public void setCustomAlias(String customAlias){
        this.customAlias = customAlias;
    }
}
