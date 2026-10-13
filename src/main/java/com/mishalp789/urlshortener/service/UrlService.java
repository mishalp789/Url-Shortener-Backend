package com.mishalp789.urlshortener.service;

import com.mishalp789.urlshortener.dto.CreateUrlRequest;
import com.mishalp789.urlshortener.entity.UrlEntity;
import com.mishalp789.urlshortener.entity.UserEntity;
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

    public UrlEntity createShortUrl(CreateUrlRequest request){
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


        return urlRepository.save(url);
    }
}
