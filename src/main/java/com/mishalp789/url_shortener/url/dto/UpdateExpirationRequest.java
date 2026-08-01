package com.mishalp789.url_shortener.url.dto;

import jakarta.validation.constraints.Future;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateExpirationRequest {

    @Future(message = "Expiration must be in the future")
    private LocalDateTime expiresAt;

}
