package com.mishalp789.url_shortener.auth;

import com.mishalp789.url_shortener.BaseUnitTest;
import com.mishalp789.url_shortener.auth.dto.AuthenticationResponse;
import com.mishalp789.url_shortener.auth.dto.RegisterRequest;
import com.mishalp789.url_shortener.auth.entity.Role;
import com.mishalp789.url_shortener.auth.entity.User;
import com.mishalp789.url_shortener.auth.repository.UserRepository;
import com.mishalp789.url_shortener.auth.service.AuthenticationService;
import com.mishalp789.url_shortener.common.exception.BadRequestException;
import com.mishalp789.url_shortener.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class AuthenticationServiceTest extends BaseUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private RegisterRequest request;

    @BeforeEach
    void setup(){
        request = new RegisterRequest();
        request.setFullName("Mishal");
        request.setUsername("mishalp789");
        request.setEmail("mishal@example.com");
        request.setPassword("password123");

    }

}
