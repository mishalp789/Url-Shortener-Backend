package com.mishalp789.url_shortener.url.service;


import com.mishalp789.url_shortener.auth.entity.User;
import com.mishalp789.url_shortener.auth.repository.UserRepository;
import com.mishalp789.url_shortener.common.exception.BadRequestException;
import com.mishalp789.url_shortener.common.exception.UrlNotFoundException;
import com.mishalp789.url_shortener.url.dto.*;
import com.mishalp789.url_shortener.url.entity.Url;
import com.mishalp789.url_shortener.url.repository.UrlRepository;
import com.mishalp789.url_shortener.url.util.ShortCodeGenerator;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.cache.CacheManager;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final UserRepository userRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final CacheManager cacheManager;


    @Value("${app.base-url}")
    private String baseUrl;

    public UrlResponse createShortUrl(CreateUrlRequest request,
                                      Authentication authentication) {

        User user = getAuthenticatedUser(authentication);

        String shortCode;

        do {
            shortCode = shortCodeGenerator.generate();
        } while (urlRepository.existsByShortCode(shortCode));

        Url url = Url.builder()
                .originalUrl(request.getOriginalUrl())
                .shortCode(shortCode)
                .user(user)
                .build();

        Url saved = urlRepository.save(url);

        return mapToResponse(saved);
    }

    public PageResponse<UrlResponse> getMyUrls(
            Authentication authentication,
            String search,
            Pageable pageable
    ) {
        User user = getAuthenticatedUser(authentication);
        Page<Url> page;
        if(search == null || search.isBlank()){
            page = urlRepository.findAllByUser(user,pageable);
        }else {
            page = urlRepository
                    .searchUserUrls(user,search,pageable);
        }
        return PageResponse.<UrlResponse>builder()
                .content(
                        page.getContent()
                                .stream()
                                .map(this::mapToResponse)
                                .toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
    @Transactional(readOnly = true)
    @Cacheable(value = "urls", key = "#shortCode")
    public String getOriginalUrl(String shortCode){
        Url url = urlRepository.findByShortCodeAndActiveTrue(shortCode)
                .orElseThrow(() ->
                        new UrlNotFoundException("Short URL not found"));


        return url.getOriginalUrl();
    }

    @Transactional
    public void incrementClickCount(String shortCode) {
        urlRepository.incrementClickCount(shortCode);
    }

    public UrlResponse getUrl(Long id,
                              Authentication authentication) {

        Url url = getUserUrl(id, authentication);

        return mapToResponse(url);

    }
    @Transactional
    public void deleteUrl(Long id,
                          Authentication authentication) {

        Url url = getUserUrl(id, authentication);
        evictUrlCache(url.getShortCode());

        urlRepository.delete(url);

    }


    @Transactional
    public UrlResponse updateStatus(Long id,
                                    UpdateUrlStatusRequest request,
                                    Authentication authentication) {

        Url url = getUserUrl(id, authentication);

        url.setActive(request.getActive());
        evictUrlCache(url.getShortCode());

        return mapToResponse(url);

    }

    public UrlAnalyticsResponse getAnalytics(
            Long id,
            Authentication authentication
    ){
        Url url = getUserUrl(id,authentication);
        return UrlAnalyticsResponse.builder()
                .id(url.getId())
                .originalUrl(url.getOriginalUrl())
                .shortCode(url.getShortCode())
                .customAlias(url.getCustomAlias())
                .shortUrl(baseUrl + "/" + url.getShortCode())
                .clickCount(url.getClickCount())
                .active(url.getActive())
                .createdAt(url.getCreatedAt())
                .updatedAt(url.getUpdatedAt())
                .build();
    }

    public DashboardResponse getDashboard(
            Authentication authentication
    ){
        User user = getAuthenticatedUser(authentication);

        return DashboardResponse.builder()
                .totalUrls(urlRepository.countByUser(user))
                .activeUrls(urlRepository.countByUserAndActiveTrue(user))
                .inactiveUrls(urlRepository.countByUserAndActiveFalse(user))
                .totalClicks(urlRepository.getTotalClicks(user))
                .build();
    }




    private UrlResponse mapToResponse(Url url) {

        return UrlResponse.builder()
                .id(url.getId())
                .originalUrl(url.getOriginalUrl())
                .shortCode(url.getShortCode())
                .shortUrl(baseUrl + "/" + url.getShortCode())
                .clickCount(url.getClickCount())
                .active(url.getActive())
                .customAlias(url.getCustomAlias())
                .build();
    }

    private void evictUrlCache(String shortCode){
        Cache cache = cacheManager.getCache("urls");
        if(cache!=null){
            cache.evict(shortCode);
        }
    }

    private User getAuthenticatedUser(Authentication authentication) {

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new BadRequestException("User not found"));
    }

    private Url getUserUrl(Long id, Authentication authentication) {

        User user = getAuthenticatedUser(authentication);

        return urlRepository.findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new BadRequestException("URL not found"));
    }

}