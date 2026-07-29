package com.mishalp789.url_shortener.url.service;


import com.mishalp789.url_shortener.auth.entity.User;
import com.mishalp789.url_shortener.auth.repository.UserRepository;
import com.mishalp789.url_shortener.common.exception.BadRequestException;
import com.mishalp789.url_shortener.common.exception.UrlNotFoundException;
import com.mishalp789.url_shortener.url.dto.CreateUrlRequest;
import com.mishalp789.url_shortener.url.dto.UrlResponse;
import com.mishalp789.url_shortener.url.entity.Url;
import com.mishalp789.url_shortener.url.repository.UrlRepository;
import com.mishalp789.url_shortener.url.util.ShortCodeGenerator;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new BadRequestException("User not found"));

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

        return UrlResponse.builder()
                .id(saved.getId())
                .originalUrl(saved.getOriginalUrl())
                .shortCode(saved.getShortCode())
                .shortUrl(baseUrl + "/" + saved.getShortCode())
                .clickCount(saved.getClickCount())
                .build();
    }

    @Transactional
    public String getOriginalUrl(String shortCode){
        Url url = urlRepository.findByShortCodeAndActiveTrue(shortCode)
                .orElseThrow(() ->
                        new UrlNotFoundException("Short URL not found"));
        url.setClickCount(url.getClickCount()+1);

        return url.getOriginalUrl();
    }
}