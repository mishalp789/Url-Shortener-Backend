package com.mishalp789.urlshortener.service;

import com.mishalp789.urlshortener.dto.CreateUrlRequest;
import com.mishalp789.urlshortener.dto.UrlResponse;
import com.mishalp789.urlshortener.entity.UrlEntity;
import com.mishalp789.urlshortener.entity.UserEntity;
import com.mishalp789.urlshortener.exception.ResourceNotFoundException;
import com.mishalp789.urlshortener.repository.UrlRepository;
import com.mishalp789.urlshortener.repository.UserRepository;
import com.mishalp789.urlshortener.util.ShortCodeGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final UserRepository userRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    public UrlService(
            UrlRepository urlRepository,
            UserRepository userRepository,
            ShortCodeGenerator shortCodeGenerator){
        this.urlRepository = urlRepository;
        this.userRepository = userRepository;
        this.shortCodeGenerator = shortCodeGenerator;
    }

    public UrlResponse createShortUrl(
            CreateUrlRequest request){
        String code;
        do{
            code = shortCodeGenerator.generate();
        }while(urlRepository.existsByShortCode(code));

        UrlEntity url = new UrlEntity();
        UserEntity user = userRepository.findByEmail("mishal@gmail.com");
        url.setOriginalUrl(request.originalUrl());
        url.setShortCode(code);
        url.setCreatedAt(LocalDateTime.now());
        url.setClickCount(0L);
        url.setIsActive(true);
        url.setUser(user);


        UrlEntity saved = urlRepository.save(url);

        return new UrlResponse(
                saved.getId(),
                saved.getOriginalUrl(),
                saved.getShortCode(),
                saved.getCreatedAt(),
                saved.getExpiresAt(),
                saved.getClickCount(),
                saved.getIsActive()
        );
    }

    public String getOriginalUrl(String shortCode){
        UrlEntity url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(()->new ResourceNotFoundException("Short URL not found"));
        if(Boolean.FALSE.equals(url.getIsActive())){
            throw new RuntimeException("URL is inactive");
        }
        if(url.getExpiresAt() != null && url.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Short URL has expired");

        }

        url.setClickCount(url.getClickCount()+1);
        urlRepository.save(url);

        return url.getOriginalUrl();
    }
}
