package com.mishalp789.url_shortener.url.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AliasAvailabilityResponse {
    private String alias;
    private boolean available;
    private String message;
}
