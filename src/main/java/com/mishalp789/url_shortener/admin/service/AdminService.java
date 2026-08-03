package com.mishalp789.url_shortener.admin.service;

import com.mishalp789.url_shortener.admin.dto.AdminUrlResponse;
import com.mishalp789.url_shortener.admin.dto.AdminUserResponse;
import com.mishalp789.url_shortener.admin.dto.PlatformDashboardResponse;
import com.mishalp789.url_shortener.auth.entity.User;
import com.mishalp789.url_shortener.auth.repository.UserRepository;
import com.mishalp789.url_shortener.common.exception.BadRequestException;
import com.mishalp789.url_shortener.url.dto.PageResponse;
import com.mishalp789.url_shortener.url.entity.Url;
import com.mishalp789.url_shortener.url.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UrlRepository urlRepository;
    private final CacheManager cacheManager;

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


    public PageResponse<AdminUserResponse> getUsers(Pageable pageable){
        Page<User> page = userRepository.findAll(pageable);

        return PageResponse.<AdminUserResponse>builder()
                .content(
                        page.getContent()
                                .stream()
                                .map(this::map)
                                .toList()
                )
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
    @Transactional(readOnly = true)
    public PageResponse<AdminUrlResponse> getUrls(
            String search,
            Pageable pageable){
        Page<Url> page;

        if(search == null || search.isBlank()){
            page = urlRepository.findAll(pageable);
        }else{
            page = urlRepository.searchAllUrls(search,pageable);
        }
        return PageResponse.<AdminUrlResponse>builder()
                .content(
                        page.getContent()
                                .stream()
                                .map(this::mapUrl)
                                .toList()
                )
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    @Transactional
    public void updateUrlStatus(
            Long id,
            boolean active
    ){
        Url url = urlRepository.findById(id)
                .orElseThrow(()->
                        new BadRequestException("Url not found"));

        url.setActive(active);

        evictUrlCache(url.getShortCode());
        if(url.getCustomAlias()!=null){
            evictUrlCache(url.getCustomAlias());
        }
    }

    @Transactional
    public void deleteUrl(Long id){
        Url url = urlRepository.findById(id)
                .orElseThrow(()->new BadRequestException("URL not found"));

        evictUrlCache(url.getShortCode());

        if(url.getCustomAlias()!=null){
            evictUrlCache(url.getCustomAlias());
        }

        urlRepository.delete(url);
    }


    private AdminUserResponse map(User user){
        return AdminUserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name().replace("ROLE_",""))
                .enabled(user.getEnabled())
                .createdAt(user.getCreatedAt())
                .build();

    }

    private AdminUrlResponse mapUrl(Url url){

        return AdminUrlResponse.builder()
                .id(url.getId())
                .originalUrl(url.getOriginalUrl())
                .shortCode(url.getShortCode())
                .customAlias(url.getCustomAlias())
                .owner(url.getUser().getEmail())
                .clickCount(url.getClickCount())
                .active(url.getActive())
                .createdAt(url.getCreatedAt())
                .expiresAt(url.getExpiresAt())
                .build();

    }

    private void evictUrlCache(String shortCode){
        Cache cache = cacheManager.getCache("urls");
        if(cache!=null){
            cache.evict(shortCode);
        }
    }

}
