package com.mishal.urlshortener.url;


import java.time.LocalDateTime;


import com.mishal.urlshortener.analytics.UrlClickedEvent;
import com.mishal.urlshortener.auth.User;
import com.mishal.urlshortener.auth.UserRepository;
import com.mishal.urlshortener.infrastructure.id.Base62Encoder;
import com.mishal.urlshortener.infrastructure.id.SnowFlakeIdGenerator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mishal.urlshortener.common.ShortCodeCollisionException;
import com.mishal.urlshortener.common.UrlExpiredException;
import com.mishal.urlshortener.common.UrlNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UrlService {
    
    private final UrlRepository urlRepository;
    private static final Logger log = LoggerFactory.getLogger(UrlService.class);
    private final SnowFlakeIdGenerator idGenerator;
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;

    public UrlService(UrlRepository urlRepository,SnowFlakeIdGenerator idGenerator,ApplicationEventPublisher eventPublisher,UserRepository userRepository){
        this.urlRepository = urlRepository;
        this.idGenerator = idGenerator;
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
    }

    public String createShortUrl(String originalUrl,
                                 LocalDateTime expiresAt,
                                 String customAlias) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthenticated");
        }

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String shortCode;

        if (customAlias != null && !customAlias.isBlank()) {
            if (urlRepository.exists(customAlias)) {
                throw new ShortCodeCollisionException("Custom alias already in use");
            }
            shortCode = customAlias;
        } else {
            int attempts = 0;
            do {
                long id = idGenerator.nextId();
                shortCode = Base62Encoder.encode(id);
                attempts++;
            } while (urlRepository.exists(shortCode) && attempts < 3);

            if (urlRepository.exists(shortCode)) {
                throw new ShortCodeCollisionException("Failed to generate unique short code");
            }
        }

        Url url = new Url(shortCode, originalUrl, expiresAt, user);

        urlRepository.save(url);

        return shortCode;
    }
    @Cacheable(value = "urls", key = "#shortCode")
    public String loadOriginalUrl(String shortCode) {

        Url url = urlRepository.find(shortCode);

        if (url == null) {
            throw new UrlNotFoundException(shortCode);
        }

        if (url.isExpired()) {
            log.warn("Expired URL accessed: {}", shortCode);
            throw new UrlExpiredException(shortCode);
        }

        return url.getOriginalUrl();
    }

    public String getOriginalUrl(String shortCode){

        log.info("Redirect requested for shortCode={}", shortCode);

        String originalUrl = loadOriginalUrl(shortCode);

        // ALWAYS publish event (even on cache hit)
        eventPublisher.publishEvent(new UrlClickedEvent(shortCode));

        log.debug("Click event published for {}", shortCode);

        return originalUrl;
    }

    public Url getUrl(String shortCode){
        Url url = urlRepository.find(shortCode);
        if(url == null){
            throw new UrlNotFoundException(shortCode);
        }

        return url;
    }

    public Page<Url> getAllUrls(Pageable pageable){
        return urlRepository.findAll(pageable);
    }

    public Page<UrlResponse> getMyUrls(Pageable pageable) {

        String username =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();

        return urlRepository.findByUserUsername(username, pageable)
                .map(url -> UrlResponse.builder()
                        .shortCode(url.getShortCode())
                        .originalUrl(url.getOriginalUrl())
                        .createdAt(url.getCreatedAt())
                        .expiresAt(url.getExpiresAt())
                        .clickCount(url.getClickCount())
                        .build());
    }

}

