package com.mishalp789.url_shortener.admin.service;

import com.mishalp789.url_shortener.BaseUnitTest;
import com.mishalp789.url_shortener.admin.dto.AdminUrlResponse;
import com.mishalp789.url_shortener.admin.dto.AdminUserResponse;
import com.mishalp789.url_shortener.admin.dto.PlatformDashboardResponse;
import com.mishalp789.url_shortener.auth.entity.Role;
import com.mishalp789.url_shortener.auth.entity.User;
import com.mishalp789.url_shortener.auth.repository.UserRepository;
import com.mishalp789.url_shortener.common.exception.BadRequestException;
import com.mishalp789.url_shortener.url.dto.PageResponse;
import com.mishalp789.url_shortener.url.entity.Url;
import com.mishalp789.url_shortener.url.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.*;

import static org.mockito.Mockito.*;

public class AdminServiceTest extends BaseUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @InjectMocks
    private AdminService adminService;

    private User user;
    private Url url;

    @BeforeEach
    void setup() {

        user = User.builder()
                .id(1L)
                .fullName("Mishal")
                .username("mishal789")
                .email("mishal@example.com")
                .role(Role.ROLE_ADMIN)
                .enabled(true)
                .build();

        url = Url.builder()
                .id(1L)
                .originalUrl("https://google.com")
                .shortCode("abc123")
                .customAlias("google")
                .user(user)
                .clickCount(50L)
                .active(true)
                .build();
    }

    @Test
    void shouldReturnPlatformDashboard() {

        when(userRepository.count()).thenReturn(10L);

        when(urlRepository.count()).thenReturn(50L);

        when(urlRepository.countByActiveTrue()).thenReturn(45L);

        when(urlRepository.countByActiveFalse()).thenReturn(5L);

        when(urlRepository.countExpiredUrls()).thenReturn(2L);

        when(urlRepository.totalClicks()).thenReturn(1200L);

        PlatformDashboardResponse response =
                adminService.dashboard();

        assertEquals(10, response.getTotalUsers());

        assertEquals(50, response.getTotalUrls());

        assertEquals(1200, response.getTotalClicks());

        verify(userRepository).count();

        verify(urlRepository).count();

    }

    @Test
    void shouldReturnUsers() {

        Page<User> page =
                new PageImpl<>(List.of(user));

        when(userRepository.findAll(any(PageRequest.class)))
                .thenReturn(page);

        PageResponse<AdminUserResponse> response =
                adminService.getUsers(PageRequest.of(0,10));

        assertEquals(1, response.getContent().size());

        assertEquals(
                "ADMIN",
                response.getContent().get(0).getRole()
        );

    }

    @Test
    void shouldReturnUrls() {

        Page<Url> page =
                new PageImpl<>(List.of(url));

        when(urlRepository.findAll(any(PageRequest.class)))
                .thenReturn(page);

        PageResponse<AdminUrlResponse> response =
                adminService.getUrls(
                        "",
                        PageRequest.of(0,10)
                );

        assertEquals(1, response.getContent().size());

        assertEquals(
                "abc123",
                response.getContent().get(0).getShortCode()
        );

    }

    @Test
    void shouldSearchUrls() {

        Page<Url> page =
                new PageImpl<>(List.of(url));

        when(urlRepository.searchAllUrls(
                eq("google"),
                any(PageRequest.class)))
                .thenReturn(page);

        PageResponse<AdminUrlResponse> response =
                adminService.getUrls(
                        "google",
                        PageRequest.of(0,10)
                );

        assertEquals(1,response.getContent().size());

    }

    @Test
    void shouldUpdateUrlStatus() {

        when(urlRepository.findById(1L))
                .thenReturn(Optional.of(url));

        when(cacheManager.getCache("urls"))
                .thenReturn(cache);

        adminService.updateUrlStatus(1L,false);

        assertFalse(url.getActive());

        verify(cache).evict("abc123");

        verify(cache).evict("google");

    }

    @Test
    void shouldDeleteUrl() {

        when(urlRepository.findById(1L))
                .thenReturn(Optional.of(url));

        when(cacheManager.getCache("urls"))
                .thenReturn(cache);

        adminService.deleteUrl(1L);

        verify(urlRepository).delete(url);

        verify(cache).evict("abc123");

    }

    @Test
    void shouldThrowWhenUrlNotFound() {

        when(urlRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                BadRequestException.class,
                () -> adminService.deleteUrl(1L)
        );

    }
}
