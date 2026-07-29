package com.mishalp789.url_shortener.auth.service;

import com.mishalp789.url_shortener.auth.dto.AuthenticationResponse;
import com.mishalp789.url_shortener.auth.dto.LoginRequest;
import com.mishalp789.url_shortener.auth.dto.RegisterRequest;
import com.mishalp789.url_shortener.auth.entity.Role;
import com.mishalp789.url_shortener.auth.entity.User;
import com.mishalp789.url_shortener.auth.repository.UserRepository;
import com.mishalp789.url_shortener.common.constants.AppConstants;
import com.mishalp789.url_shortener.common.exception.BadRequestException;
import com.mishalp789.url_shortener.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new BadRequestException("Email already exists");
        }
        if (userRepository.existsByUsername(request.getUsername())){
            throw new BadRequestException("Username already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser.getEmail());

        return buildAuthenticationResponse(savedUser);
    }

    public AuthenticationResponse login(LoginRequest request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new BadRequestException("Invalid email or password"));

        String token = jwtService.generateToken(user.getEmail());

        return buildAuthenticationResponse(user);
    }

    private AuthenticationResponse buildAuthenticationResponse(User user) {

        String token = jwtService.generateToken(user.getEmail());

        return AuthenticationResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().name())
                .token(token)
                .tokenType(AppConstants.TOKEN_TYPE)
                .build();
    }
}
