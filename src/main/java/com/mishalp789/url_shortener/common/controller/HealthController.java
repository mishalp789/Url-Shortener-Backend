package com.mishalp789.url_shortener.common.controller;

import com.mishalp789.url_shortener.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Health")
@RestController
public class HealthController {

    @GetMapping("/actuator/health")
    public ApiResponse<String> health(){

        return ApiResponse.<String>builder()
                .success(true)
                .message("Application is running")
                .data("UP")
                .build();

    }

}
