package com.mishalp789.url_shortener.url.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUrlStatusRequest {

    @NotNull(message = "Active status is required")
    private Boolean active;

}
