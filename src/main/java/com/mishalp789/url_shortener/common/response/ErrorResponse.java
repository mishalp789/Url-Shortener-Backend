package com.mishalp789.url_shortener.common.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private List<String> details;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
