package com.mishalp789.url_shortener.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    private Long id;
    private String fullName;
    private String email;
    private String username;
    private String role;
    private String token;
    private String tokenType;
}
