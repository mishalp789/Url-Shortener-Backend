package com.mishalp789.url_shortener.url.service;


import com.mishalp789.url_shortener.auth.entity.User;
import com.mishalp789.url_shortener.auth.repository.UserRepository;
import com.mishalp789.url_shortener.auth.service.CurrentUserService;
import com.mishalp789.url_shortener.common.constants.CacheConstants;
import com.mishalp789.url_shortener.common.exception.BadRequestException;
import com.mishalp789.url_shortener.common.exception.UrlNotFoundException;
import com.mishalp789.url_shortener.url.dto.*;
import com.mishalp789.url_shortener.url.entity.Url;
import com.mishalp789.url_shortener.url.mapper.UrlMapper;
import com.mishalp789.url_shortener.url.repository.UrlRepository;
import com.mishalp789.url_shortener.url.util.AliasValidator;
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
    private final AliasValidator aliasValidator;
    private final CurrentUserService currentUserService;
    private final UrlMapper urlMapper;


    @Value("${app.base-url}")
    private String baseUrl;

    public UrlResponse createShortUrl(CreateUrlRequest request,
                                      Authentication authentication) {

        User user = currentUserService.getCurrentUser(authentication);

        String shortCode;
        String customAlias = null;
        if(request.getCustomAlias()!=null &&
            !request.getCustomAlias().isBlank()){
            customAlias = normalizeAlias(request.getCustomAlias());

            if(aliasValidator.isReserved(customAlias)){
                throw new BadRequestException("Alias is Reserved");
            }
            if(urlRepository.existsByCustomAlias(customAlias)){
                throw new BadRequestException("Alias already exists");
            }
            shortCode = customAlias;
        }

        else{
            do {
                shortCode = shortCodeGenerator.generate();
            } while (urlRepository.existsByShortCode(shortCode));
        }

        Url url = Url.builder()
                .originalUrl(request.getOriginalUrl())
                .shortCode(shortCode)
                .customAlias(customAlias)
                .expiresAt(request.getExpiresAt())
                .user(user)
                .build();

        Url saved = urlRepository.save(url);

        return urlMapper.toResponse(saved);
    }

    public PageResponse<UrlResponse> getMyUrls(
            Authentication authentication,
            String search,
            Pageable pageable
    ) {
        User user = currentUserService.getCurrentUser(authentication);
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
    @Cacheable(value = CacheConstants.URL_CACHE, key = "#identifier")
    public String getOriginalUrl(String identifier){
        Url url = findUrlByIdentifier(identifier);

        return url.getOriginalUrl();
    }

    @Transactional
    public void incrementClickCount(String identifier) {
        Url url = findUrlByIdentifier(identifier);
        urlRepository.incrementClickCount(url.getShortCode());
    }

    public UrlResponse getUrl(Long id,
                              Authentication authentication) {

        Url url = getUserUrl(id, authentication);

        return urlMapper.toResponse(url);

    }
    @Transactional
    public void deleteUrl(Long id,
                          Authentication authentication) {

        Url url = getUserUrl(id, authentication);
        evictUrlCache(url.getShortCode());
        if(url.getCustomAlias()!=null){
            evictUrlCache(url.getCustomAlias());
        }

        urlRepository.delete(url);

    }


    @Transactional
    public UrlResponse updateStatus(Long id,
                                    UpdateUrlStatusRequest request,
                                    Authentication authentication) {

        Url url = getUserUrl(id, authentication);

        url.setActive(request.getActive());
        evictUrlCache(url.getShortCode());

        return urlMapper.toResponse(url);

    }

    public UrlAnalyticsResponse getAnalytics(
            Long id,
            Authentication authentication
    ){
        Url url = getUserUrl(id,authentication);
        return urlMapper.toAnalytics(url);
    }

    public DashboardResponse getDashboard(
            Authentication authentication
    ){
        User user = currentUserService.getCurrentUser(authentication);

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
                .customAlias(url.getCustomAlias())
                .shortUrl(baseUrl + "/r/" + url.getShortCode())
                .clickCount(url.getClickCount())
                .active(url.getActive())
                .build();
    }

    private void evictUrlCache(String shortCode){
        Cache cache = cacheManager.getCache("urls");
        if(cache!=null){
            cache.evict(shortCode);
        }
    }


    private Url getUserUrl(Long id, Authentication authentication) {

        User user = currentUserService.getCurrentUser(authentication);

        return urlRepository.findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new BadRequestException("URL not found"));
    }

    private Url findUrlByIdentifier(String identifier) {

        return urlRepository.findByCustomAlias(identifier)
                .filter(Url::getActive)
                .or(() -> urlRepository.findByShortCodeAndActiveTrue(identifier))
                .orElseThrow(() ->
                        new UrlNotFoundException("Short URL not found"));
    }

    public AliasAvailabilityResponse checkAliasAvailability(String alias){
        if(alias == null || alias.isBlank()){
            throw new BadRequestException("Alias cannot be empty");
        }

        alias = normalizeAlias(alias);

        if(aliasValidator.isReserved(alias)){
            return AliasAvailabilityResponse.builder()
                    .alias(alias)
                    .available(false)
                    .message("Reserved alias")
                    .build();
        }

        boolean exists =
                urlRepository.existsByCustomAlias(alias)
                        || urlRepository.existsByShortCode(alias);

        return AliasAvailabilityResponse.builder()
                .alias(alias)
                .available(!exists)
                .message(exists ? "Alias "+alias+" is already in use " : "Alias available")
                .build();

    }

    private String normalizeAlias(String alias) {
        return alias.trim().toLowerCase();
    }

}