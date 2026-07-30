package com.mishalp789.url_shortener.url.service;


import com.mishalp789.url_shortener.auth.entity.User;
import com.mishalp789.url_shortener.auth.repository.UserRepository;
import com.mishalp789.url_shortener.common.exception.BadRequestException;
import com.mishalp789.url_shortener.common.exception.UrlNotFoundException;
import com.mishalp789.url_shortener.url.dto.CreateUrlRequest;
import com.mishalp789.url_shortener.url.dto.UpdateUrlStatusRequest;
import com.mishalp789.url_shortener.url.dto.UrlResponse;
import com.mishalp789.url_shortener.url.entity.Url;
import com.mishalp789.url_shortener.url.repository.UrlRepository;
import com.mishalp789.url_shortener.url.util.ShortCodeGenerator;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final UserRepository userRepository;
    private final ShortCodeGenerator shortCodeGenerator;

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

    public List<UrlResponse> getMyUrls(Authentication authentication){
        User user = getAuthenticatedUser(authentication);

        return urlRepository.findAllByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public String getOriginalUrl(String shortCode){
        Url url = urlRepository.findByShortCodeAndActiveTrue(shortCode)
                .orElseThrow(() ->
                        new UrlNotFoundException("Short URL not found"));
        url.setClickCount(url.getClickCount()+1);

        return url.getOriginalUrl();
    }

    public UrlResponse getUrl(Long id,
                              Authentication authentication) {

        Url url = getUserUrl(id, authentication);

        return mapToResponse(url);

    }

    public void deleteUrl(Long id,
                          Authentication authentication) {

        Url url = getUserUrl(id, authentication);

        urlRepository.delete(url);

    }

    @Transactional
    public UrlResponse updateStatus(Long id,
                                    UpdateUrlStatusRequest request,
                                    Authentication authentication) {

        Url url = getUserUrl(id, authentication);

        url.setActive(request.getActive());

        return mapToResponse(url);

    }



    private UrlResponse mapToResponse(Url url) {

        return UrlResponse.builder()
                .id(url.getId())
                .originalUrl(url.getOriginalUrl())
                .shortCode(url.getShortCode())
                .shortUrl(baseUrl + "/" + url.getShortCode())
                .clickCount(url.getClickCount())
                .active(url.getActive())
                .build();
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