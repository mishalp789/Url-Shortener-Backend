package com.mishalp789.url_shortener.admin.service;

import com.mishalp789.url_shortener.admin.dto.PlatformDashboardResponse;
import com.mishalp789.url_shortener.auth.repository.UserRepository;
import com.mishalp789.url_shortener.url.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UrlRepository urlRepository;

    public PlatformDashboardResponse dashboard() {

        return PlatformDashboardResponse.builder()
                .totalUsers(userRepository.count())
                .totalUrls(urlRepository.count())
                .activeUrls(urlRepository.countByActiveTrue())
                .inactiveUrls(urlRepository.countByActiveFalse())
                .expiredUrls(urlRepository.countExpiredUrls())
                .totalClicks(urlRepository.totalClicks())
                .build();
    }
}
