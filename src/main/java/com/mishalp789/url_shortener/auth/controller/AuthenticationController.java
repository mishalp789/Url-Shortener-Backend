package com.mishalp789.url_shortener.auth.controller;

import com.mishalp789.url_shortener.auth.dto.AuthenticationResponse;
import com.mishalp789.url_shortener.auth.dto.LoginRequest;
import com.mishalp789.url_shortener.auth.dto.RegisterRequest;
import com.mishalp789.url_shortener.auth.service.AuthenticationService;
import com.mishalp789.url_shortener.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @Operation(summary = "Register new user")
    @PostMapping("/register")
    public ApiResponse<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request){
        return ApiResponse.<AuthenticationResponse>builder()
                .success(true)
                .message("Registration successful")
                .data(authenticationService.register(request))
                .build();

    }
    @Operation(summary = "Login user")
    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ApiResponse.<AuthenticationResponse>builder()
                .success(true)
                .message("Login successful")
                .data(authenticationService.login(request))
                .build();

    }
}
