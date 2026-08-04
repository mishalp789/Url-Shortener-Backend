package com.mishalp789.url_shortener.auth.service;



import com.mishalp789.url_shortener.BaseUnitTest;
import com.mishalp789.url_shortener.auth.dto.AuthenticationResponse;
import com.mishalp789.url_shortener.auth.dto.RegisterRequest;
import com.mishalp789.url_shortener.auth.entity.Role;
import com.mishalp789.url_shortener.auth.entity.User;
import com.mishalp789.url_shortener.auth.repository.UserRepository;
import com.mishalp789.url_shortener.common.exception.BadRequestException;
import com.mishalp789.url_shortener.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AuthenticationServiceTest extends BaseUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private RegisterRequest request;



    @BeforeEach
    void setup() {
        request = new RegisterRequest();
        request.setFullName("Mishal");
        request.setUsername("mishal789");
        request.setEmail("mishal@example.com");
        request.setPassword("password123");

    }

    @Test
    void shouldRegisterUserSuccessfully() {
        when(userRepository.existsByEmail(request.getEmail()))

                .thenReturn(false);
        when(userRepository.existsByUsername(request.getUsername()))
                .thenReturn(false);
        when(passwordEncoder.encode(anyString()))
                .thenReturn("encodedPassword");
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setId(1L);
                    return user;
                });
        when(jwtService.generateToken(anyString()))
                .thenReturn("jwt-token");

        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);
        AuthenticationResponse response =
                authenticationService.register(request);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertEquals(request.getEmail(), saved.getEmail());
        assertEquals("encodedPassword", saved.getPassword());
        assertEquals(Role.ROLE_USER, saved.getRole());
        assertEquals("jwt-token", response.getToken());

    }



    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);
        assertThrows(
                BadRequestException.class,
                () -> authenticationService.register(request)
        );
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);
        when(userRepository.existsByUsername(request.getUsername()))
                .thenReturn(true);

        assertThrows(
                BadRequestException.class,
                () -> authenticationService.register(request)
        );
    }

    @Test
    void shouldLoginSuccessfully() {
        User user = User.builder()
                .id(1L)
                .fullName("Mishal")
                .email(request.getEmail())
                .username(request.getUsername())
                .password("encoded")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(java.util.Optional.of(user));
        when(jwtService.generateToken(anyString()))
                .thenReturn("jwt-token");

        var loginRequest =
                new com.mishalp789.url_shortener.auth.dto.LoginRequest();

        loginRequest.setEmail(request.getEmail());
        loginRequest.setPassword("password123");

        AuthenticationResponse response =
                authenticationService.login(loginRequest);

        verify(authenticationManager)
                .authenticate(any());

        assertEquals("jwt-token", response.getToken());

    }


    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        when(userRepository.findByEmail(anyString()))
                .thenReturn(java.util.Optional.empty());

        var loginRequest =
                new com.mishalp789.url_shortener.auth.dto.LoginRequest();

        loginRequest.setEmail(request.getEmail());
        loginRequest.setPassword("password123");
        assertThrows(
                BadRequestException.class,
                () -> authenticationService.login(loginRequest)
        );

    }

}