package com.mishalp789.url_shortener.url.service;

import com.mishalp789.url_shortener.BaseUnitTest;
import com.mishalp789.url_shortener.auth.entity.Role;
import com.mishalp789.url_shortener.auth.entity.User;
import com.mishalp789.url_shortener.auth.service.CurrentUserService;
import com.mishalp789.url_shortener.common.exception.BadRequestException;
import com.mishalp789.url_shortener.url.dto.CreateUrlRequest;
import com.mishalp789.url_shortener.url.dto.DashboardResponse;
import com.mishalp789.url_shortener.url.dto.UpdateUrlStatusRequest;
import com.mishalp789.url_shortener.url.dto.UrlResponse;
import com.mishalp789.url_shortener.url.entity.Url;
import com.mishalp789.url_shortener.url.mapper.UrlMapper;
import com.mishalp789.url_shortener.url.qr.QRCodeService;
import com.mishalp789.url_shortener.url.repository.UrlRepository;
import com.mishalp789.url_shortener.url.util.AliasValidator;
import com.mishalp789.url_shortener.url.util.ShortCodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class UrlServiceTest extends BaseUnitTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private ShortCodeGenerator shortCodeGenerator;

    @Mock
    private UrlMapper urlMapper;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private AliasValidator aliasValidator;

    @Mock
    private QRCodeService qrCodeService;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UrlService urlService;

    private User user;

    @BeforeEach
    void setup() {

        user = User.builder()
                .id(1L)
                .email("mishal@example.com")
                .username("mishal789")
                .role(Role.ROLE_USER)
                .enabled(true)
                .build();

    }


    @Test
    void shouldCreateRandomShortUrl() {

        CreateUrlRequest request = new CreateUrlRequest();
        request.setOriginalUrl("https://google.com");

        when(currentUserService.getCurrentUser(authentication))
                .thenReturn(user);

        when(shortCodeGenerator.generate())
                .thenReturn("abc123");

        when(urlRepository.existsByShortCode("abc123"))
                .thenReturn(false);

        when(urlRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Url url = Url.builder()
                .shortCode("abc123")
                .originalUrl(request.getOriginalUrl())
                .user(user)
                .build();

        UrlResponse response = UrlResponse.builder()
                .shortCode("abc123")
                .originalUrl(request.getOriginalUrl())
                .build();

        when(urlMapper.toResponse(any()))
                .thenReturn(response);

        UrlResponse result =
                urlService.createShortUrl(request, authentication);

        assertEquals("abc123", result.getShortCode());

        verify(urlRepository).save(any());

    }

    @Test
    void shouldCreateCustomAlias() {

        CreateUrlRequest request = new CreateUrlRequest();

        request.setOriginalUrl("https://spring.io");

        request.setCustomAlias("spring");

        when(currentUserService.getCurrentUser(authentication))
                .thenReturn(user);

        when(aliasValidator.isReserved("spring"))
                .thenReturn(false);

        when(urlRepository.existsByCustomAlias("spring"))
                .thenReturn(false);

        when(urlRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UrlResponse response = UrlResponse.builder()
                .shortCode("spring")
                .customAlias("spring")
                .build();

        when(urlMapper.toResponse(any()))
                .thenReturn(response);

        UrlResponse result =
                urlService.createShortUrl(request, authentication);

        assertEquals("spring", result.getShortCode());

    }

    @Test
    void shouldThrowExceptionWhenAliasExists() {

        CreateUrlRequest request = new CreateUrlRequest();

        request.setOriginalUrl("https://google.com");

        request.setCustomAlias("github");

        when(currentUserService.getCurrentUser(authentication))
                .thenReturn(user);

        when(aliasValidator.isReserved("github"))
                .thenReturn(false);

        when(urlRepository.existsByCustomAlias("github"))
                .thenReturn(true);

        assertThrows(
                BadRequestException.class,
                () -> urlService.createShortUrl(request, authentication)
        );

    }
    @Test
    void shouldReturnDashboardStatistics() {

        when(currentUserService.getCurrentUser(authentication))
                .thenReturn(user);

        when(urlRepository.countByUser(user)).thenReturn(20L);

        when(urlRepository.countByUserAndActiveTrue(user)).thenReturn(18L);

        when(urlRepository.countByUserAndActiveFalse(user)).thenReturn(2L);

        when(urlRepository.countExpiredUrls(user)).thenReturn(1L);

        when(urlRepository.getTotalClicks(user)).thenReturn(560L);

        DashboardResponse response =
                urlService.getDashboard(authentication);

        assertEquals(20, response.getTotalUrls());

        assertEquals(560, response.getTotalClicks());

    }

    @Test
    void shouldDeleteUrl() {

        Url url = Url.builder()
                .id(1L)
                .shortCode("abc123")
                .user(user)
                .active(true)
                .build();

        when(currentUserService.getCurrentUser(authentication))
                .thenReturn(user);

        when(urlRepository.findByIdAndUser(1L,user))
                .thenReturn(Optional.of(url));

        urlService.deleteUrl(1L, authentication);

        verify(urlRepository).delete(url);

    }

    @Test
    void shouldUpdateStatus() {

        Url url = Url.builder()
                .id(1L)
                .active(true)
                .user(user)
                .build();

        UpdateUrlStatusRequest request = new UpdateUrlStatusRequest();
        request.setActive(false);

        when(currentUserService.getCurrentUser(authentication))
                .thenReturn(user);

        when(urlRepository.findByIdAndUser(1L,user))
                .thenReturn(Optional.of(url));

        urlService.updateStatus(1L, request, authentication);

        assertFalse(url.getActive());

    }

    @Test
    void shouldThrowExceptionWhenUrlNotFound() {

        when(currentUserService.getCurrentUser(authentication))
                .thenReturn(user);

        when(urlRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.empty());

        assertThrows(
                BadRequestException.class,
                () -> urlService.deleteUrl(1L, authentication)
        );

        verify(urlRepository, never()).delete(any());
    }

}
